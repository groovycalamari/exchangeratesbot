package io.micronaut.bots.slack.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class RateLimitingEvent {

    public static final String TYPE_APP_RATED_LIMITED = "app_rate_limited";

    /**
     * the same shared token used to verify other events in the Events API.
     */
    private String token;

    /**
     * this specific event type, app_rate_limited
     */
    private String type;

    /**
     * a rounded epoch time value indicating the minute your application became rate limited for this workspace. 1518467820 is at 2018-02-12 20:37:00 UTC.
     */
    @JsonProperty("minute_rate_limited")
    private Long minuteRateLimited;

    /**
     * subscriptions between your app and the workspace with this ID are being rate limited.
     */
    @JsonProperty("team_id")
    private String teamId;

    /**
     * your application's ID, especially useful if you have multiple applications working with the Events API.
     */
    @JsonProperty("api_app_id")
    private String apiAppId;

    public RateLimitingEvent() {
    }

    public static String getTypeAppRatedLimited() {
        return TYPE_APP_RATED_LIMITED;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMinuteRateLimited() {
        return minuteRateLimited;
    }

    public void setMinuteRateLimited(Long minuteRateLimited) {
        this.minuteRateLimited = minuteRateLimited;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getApiAppId() {
        return apiAppId;
    }

    public void setApiAppId(String apiAppId) {
        this.apiAppId = apiAppId;
    }
}
