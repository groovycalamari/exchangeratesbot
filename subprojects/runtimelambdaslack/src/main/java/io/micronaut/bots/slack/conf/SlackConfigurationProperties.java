package io.micronaut.bots.slack.conf;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("slack.apps")
public class SlackConfigurationProperties implements SlackConfiguration {
    private String signingSecret;
    private final String name;

    public SlackConfigurationProperties(@Parameter String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String getSigningSecret() {
        return signingSecret;
    }

    public void setSigningSecret(String signingSecret) {
        this.signingSecret = signingSecret;
    }

    public String getName() {
        return name;
    }
}