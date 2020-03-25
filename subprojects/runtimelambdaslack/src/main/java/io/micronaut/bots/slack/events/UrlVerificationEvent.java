package io.micronaut.bots.slack.events;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class UrlVerificationEvent {
    public static final String TYPE_URL_VERIFICATION = "url_verification";
    private String token;
    private String challenge;
    private String type;

    public UrlVerificationEvent() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
