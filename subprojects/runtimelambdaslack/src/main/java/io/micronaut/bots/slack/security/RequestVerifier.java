package io.micronaut.bots.slack.security;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.slack.conf.SlackConfiguration;
import io.micronaut.context.annotation.DefaultImplementation;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@DefaultImplementation(DefaultRequestVerifier.class)
public interface RequestVerifier {
    String HEADER_SLACK_REQUEST_TIMESTAMP = "X-Slack-Request-Timestamp";
    String HEADER_SLACK_SIGNATURE = "X-Slack-Signature";

    Optional<SlackConfiguration> verify(@NonNull @NotBlank String slackSignature,
                                        @NonNull @NotBlank String slackRequestTimestamp,
                                        @NonNull @NotBlank String requestBody);
}
