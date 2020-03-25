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
package io.micronaut.bots.slack.security

import io.micronaut.bots.slack.conf.SlackConfiguration
import io.micronaut.bots.slack.conf.SlackConfigurationProperties
import spock.lang.Specification

class DefaultRequestVerifierSpec extends Specification {

    void "test Slack Request Verification"() {
        given:
        String slackSignature = 'v0=a2114d57b48eac39b9ad189dd8316235a7b4a8d21a10bd27519666489c69b503'
        String signingSecret = '8f742231b10e8888abcd99yyyzzz85a5'
        String body = "token=xyzz0WbapA4vBCDEFasx0q6G&team_id=T1DC2JH3J&team_domain=testteamnow&channel_id=G8PSS9T3V&channel_name=foobar&user_id=U2CERLKJA&user_name=roadrunner&command=%2Fwebhook-collect&text=&response_url=https%3A%2F%2Fhooks.slack.com%2Fcommands%2FT1DC2JH3J%2F397700885554%2F96rGlfmibIGlgcZRskXaIFfN&trigger_id=398738663015.47445629121.803a0bc887a14d10d2c447fce8b6703c"
        String timestamp = "1531420618"

        List<SlackConfiguration> slackConfigurations = new ArrayList<>()
        SlackConfiguration conf = new SlackConfigurationProperties("exchangerates")
        conf.setSigningSecret(signingSecret)
        slackConfigurations.add(conf)
        RequestVerifier requestVerifier = new DefaultRequestVerifier(slackConfigurations)

        when:
        Optional<SlackConfiguration> optionalSlackConfiguration = requestVerifier.verify(slackSignature, timestamp, body)

        then:
        optionalSlackConfiguration.isPresent()
        optionalSlackConfiguration.get().getSigningSecret().equals(signingSecret)
    }
}
