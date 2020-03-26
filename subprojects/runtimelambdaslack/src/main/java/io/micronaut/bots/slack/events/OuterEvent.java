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
package io.micronaut.bots.slack.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

/**
 * JSON object containing the event that happened itself.
 * @see <a href="https://api.slack.com/events-api#receiving_events">Callback field overview</a>
 */
@Introspected
public class OuterEvent {

    /**
     * The shared-private callback token that authenticates this callback to the application as having come from Slack. Match this against what you were given when the subscription was created. If it does not match, do not process the event and discard it.
     * Example: JhjZd2rVax7ZwH7jRYyWjbDl
     */
    public String token;

    /**
     * The unique identifier for the workspace/team where this event occurred.
     * Example: T461EG9ZZ
     */
    @JsonProperty("team_id")
    private String teamId;

    /**
     * The unique identifier for the application this event is intended for. Your application's ID can be found in the URL of the your application console. If your Request URL manages multiple applications, use this field along with the token field to validate and route incoming requests.
     * Example: A4ZFV49KK
     */
    @JsonProperty("api_app_id")
    private String apiAppId;

    /**
     * Contains the inner set of fields representing the event that's happening.
     */
    private EventType event;

    /**
     * This reflects the type of callback you're receiving. Typically, that is event_callback. You may encounter url_verification during the configuration process. The `event` fields "inner event" will also contain a type field indicating which event type lurks within (down below).
     */
    private String type;

    /**
     * An array of string-based User IDs. Each member of the collection represents a user that has installed your application/bot and indicates the described event would be visible to those users. You'll receive a single event for a piece of data intended for multiple users in a workspace, rather than a message per user.
     */
    @JsonProperty("authed_users")
    private List<String> authedUser;

    /**
     * A unique identifier for this specific event, globally unique across all workspaces.
     */
    @JsonProperty("event_id")
    private String eventId;

    /**
     * The epoch timestamp in seconds indicating when this event was dispatched.
     */
    @JsonProperty("event_time")
    private Integer eventTime;

    public OuterEvent() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAuthedUser() {
        return authedUser;
    }

    public void setAuthedUser(List<String> authedUser) {
        this.authedUser = authedUser;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getEventTime() {
        return eventTime;
    }

    public void setEventTime(Integer eventTime) {
        this.eventTime = eventTime;
    }
}
