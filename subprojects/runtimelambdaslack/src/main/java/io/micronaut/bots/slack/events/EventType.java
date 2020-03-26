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
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class EventType {

    /**
     * The specific name of the event described by its adjacent fields. This field is included with every inner event type.
     * Examples: reaction_added, messages.channel, team_join
     */
    @NonNull
    private String type;

    /**
     * The timestamp of the event. The combination of event_ts, team_id, user_id, or channel_id is intended to be unique. This field is included with every inner event type.
     * Example: 1469470591.759709
     */
    @JsonProperty("event_ts")
    @NonNull
    private String eventTs;

    /**
     * StringThe user ID belonging to the user that incited this action. Not included in all events as not all events are controlled by users. See the top-level callback object's authed_users if you need to calculate event visibility by user.
     * Example: U061F7AUR
     */
    @Nullable
    private String user;

    /**
     * The timestamp of what the event describes, which may occur slightly prior to the event being dispatched as described by event_ts. The combination of ts, team_id, user_id, or channel_id is intended to be unique.
     * Example: 1469470591.759709
     */
    private String ts;

    /**
     * Data specific to the underlying object type being described. Often you'll encounter abbreviated versions of full objects. For instance, when file objects are referenced, only the file's ID is presented. See each individual event type for more detail.
     */
    private String item;

    public EventType() {
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getEventTs() {
        return eventTs;
    }

    public void setEventTs(@NonNull String eventTs) {
        this.eventTs = eventTs;
    }

    @Nullable
    public String getUser() {
        return user;
    }

    public void setUser(@Nullable String user) {
        this.user = user;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
