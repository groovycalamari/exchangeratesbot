/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.bots.slack.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.slack.conf.SlackConfiguration;
import io.micronaut.bots.slack.events.UrlVerificationEvent;
import io.micronaut.bots.slack.security.RequestVerifier;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SlackWebhookHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(SlackWebhookHandler.class);

    @Inject
    RequestVerifier requestVerifier;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Method: {} Path: {}", input.getHttpMethod(), input.getPath());
            for (String header : input.getHeaders().keySet()) {
                LOG.trace("H {}: {}", header, input.getHeaders().get(header));
            }
            LOG.trace("body: {}", input.getBody());

        }
        String slackRequestTimeStamp = input.getHeaders().get(RequestVerifier.HEADER_SLACK_REQUEST_TIMESTAMP);
        if (slackRequestTimeStamp == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("header {} not found", RequestVerifier.HEADER_SLACK_REQUEST_TIMESTAMP);
            }
            return badRequest("header " + RequestVerifier.HEADER_SLACK_REQUEST_TIMESTAMP + " not found");
        }

        String slackSignature = input.getHeaders().get(RequestVerifier.HEADER_SLACK_SIGNATURE);
        if (slackSignature == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("header {} not found", RequestVerifier.HEADER_SLACK_SIGNATURE);
            }
            return badRequest("header " + RequestVerifier.HEADER_SLACK_SIGNATURE + " not found");
        }

        Optional<SlackConfiguration> slackConfigurationOptional = requestVerifier.verify(slackSignature, slackRequestTimeStamp, input.getBody());
        if (slackConfigurationOptional.isPresent()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("verified slack app: {}", slackConfigurationOptional.get().getName());
            }
            String body = input.getBody();
            if (body.contains(UrlVerificationEvent.TYPE_URL_VERIFICATION)) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("event url verification received");
                }
                UrlVerificationEvent challenge = null;
                try {
                    challenge = objectMapper.readValue(body, UrlVerificationEvent.class);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("challenge received {}", challenge.toString());
                    }
                } catch (JsonProcessingException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("error binding body {} to {}", body, UrlVerificationEvent.class.getSimpleName());
                    }
                    return plainText(HttpStatus.BAD_REQUEST, "error binding body " + body + " to " + UrlVerificationEvent.class.getSimpleName());
                }
                return plainText(HttpStatus.OK, challenge.getChallenge());
            }
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("slack request verification failed");
            }
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(HttpStatus.OK.getCode());
        return response;
    }

    @Nonnull
    @Override
    protected ApplicationContextBuilder newApplicationContextBuilder() {
        ApplicationContextBuilder builder = super.newApplicationContextBuilder();
        builder.environments(ENVIRONMENT_LAMBDA);
        builder.eagerInitConfiguration(true);
        builder.eagerInitSingletons(true);
        return builder;
    }

    @NonNull
    protected APIGatewayProxyResponseEvent badRequest(@NonNull String msg) {
        return plainText(HttpStatus.BAD_REQUEST, msg);
    }

    @NonNull
    protected APIGatewayProxyResponseEvent plainText(@NonNull  HttpStatus status, @NonNull String msg) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        response.setHeaders(headers);
        response.setBody(msg);
        response.setStatusCode(status.getCode());
        if (LOG.isTraceEnabled()) {
            LOG.trace("response: {} content type: {} message {}",
                    status.getCode(),
                    headers.get(HttpHeaders.CONTENT_TYPE),
                    msg);
        }
        return response;
    }
}
