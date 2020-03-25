package io.micronaut.bots.slack.conf;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface SlackConfiguration {

    @NonNull
    String getSigningSecret();
}
