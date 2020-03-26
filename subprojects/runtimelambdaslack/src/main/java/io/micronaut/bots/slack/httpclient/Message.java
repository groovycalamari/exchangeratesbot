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
package io.micronaut.bots.slack.httpclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Message {

    /**
     * Authentication token bearing required scopes.
     * Example: xxxx-xxxxxxxxx-xxxx
     */
    @NonNull
    private String token;

    /**
     * Channel, private group, or IM channel to send message to. Can be an encoded ID, or a name. See below for more details.
     * Example: C1234567890
     */
    @NonNull
    private String channel;


    /**
     * How this field works and whether it is required depends on other fields you use in your API call. See below for more detail.
     * Example: Hello world
     */
    @NonNull
    private String text;

    /**
     * Pass true to post the message as the authed user, instead of as a bot. Defaults to false. See authorship below. This argument may not be used with newer bot tokens.
     */
    @Nullable
    @JsonProperty("as_user")
    private Boolean asUser;

    /**
     * A JSON-based array of structured attachments, presented as a URL-encoded string.
     * Example: [{"pretext": "pre-hello", "text": "text-world"}]
     */
    @Nullable
    private String attachments;


    /**
     * A JSON-based array of structured blocks, presented as a URL-encoded string.
     * Example: [{"type": "section", "text": {"type": "plain_text", "text": "Hello world"}}]
     */
    @Nullable
    private String blocks;

    /**
     * Emoji to use as the icon for this message. Overrides icon_url. Must be used in conjunction with as_user set to false, otherwise ignored. See authorship below. This argument may not be used with newer bot tokens.
     * Example: :chart_with_upwards_trend:
     */
    @Nullable
    @JsonProperty("icon_emoji")
    private String iconEmoji;

    /**
     * URL to an image to use as the icon for this message. Must be used in conjunction with as_user set to false, otherwise ignored. See authorship below. This argument may not be used with newer bot tokens.
     * Example: http://lorempixel.com/48/48
     */
    @Nullable
    @JsonProperty("icon_url")
    private String iconUrl;

    /**
     * Find and link channel names and usernames.
     */
    @Nullable
    @JsonProperty("link_names")
    private Boolean linkNames;

    /**
     * Disable Slack markup parsing by setting to false. Enabled by default.
     */
    @Nullable
    private Boolean mrkdwn;

    /**
     * Change how messages are treated. Defaults to none. See below.
     * Example: full
     */
    @Nullable
    private String parse;

    /**
     * Used in conjunction with thread_ts and indicates whether reply should be made visible to everyone in the channel or conversation. Defaults to false.
     */
    @Nullable
    @JsonProperty("reply_broadcast")
    private Boolean replyBroadcast;

    /**
     * Provide another message's ts value to make this message a reply. Avoid using a reply's ts value; use its parent instead.
     * Example: 1234567890.123456
     */
    @Nullable
    @JsonProperty("thread_ts")
    private String threadTs;

    /**
     * Pass true to enable unfurling of primarily text-based content.
     */
    @Nullable
    @JsonProperty("unfurl_links")
    private Boolean unfurlLinks;

    /**
     * Pass false to disable unfurling of media content.
     */
    @JsonProperty("unfurl_media")
    @Nullable
    private Boolean unfurlMedia;

    /**
     * Set your bot's user name. Must be used in conjunction with as_user set to false, otherwise ignored. See authorship below.
     * Example: My Bot
     */
    @Nullable
    private String username;

    public Message() {
    }

    @NonNull
    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    @NonNull
    public String getChannel() {
        return channel;
    }

    public void setChannel(@NonNull String channel) {
        this.channel = channel;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @Nullable
    public Boolean getAsUser() {
        return asUser;
    }

    public void setAsUser(@Nullable Boolean asUser) {
        this.asUser = asUser;
    }

    @Nullable
    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(@Nullable String attachments) {
        this.attachments = attachments;
    }

    @Nullable
    public String getBlocks() {
        return blocks;
    }

    public void setBlocks(@Nullable String blocks) {
        this.blocks = blocks;
    }

    @Nullable
    public String getIconEmoji() {
        return iconEmoji;
    }

    public void setIconEmoji(@Nullable String iconEmoji) {
        this.iconEmoji = iconEmoji;
    }

    @Nullable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(@Nullable String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Nullable
    public Boolean getLinkNames() {
        return linkNames;
    }

    public void setLinkNames(@Nullable Boolean linkNames) {
        this.linkNames = linkNames;
    }

    @Nullable
    public Boolean getMrkdwn() {
        return mrkdwn;
    }

    public void setMrkdwn(@Nullable Boolean mrkdwn) {
        this.mrkdwn = mrkdwn;
    }

    @Nullable
    public String getParse() {
        return parse;
    }

    public void setParse(@Nullable String parse) {
        this.parse = parse;
    }

    @Nullable
    public Boolean getReplyBroadcast() {
        return replyBroadcast;
    }

    public void setReplyBroadcast(@Nullable Boolean replyBroadcast) {
        this.replyBroadcast = replyBroadcast;
    }

    @Nullable
    public String getThreadTs() {
        return threadTs;
    }

    public void setThreadTs(@Nullable String threadTs) {
        this.threadTs = threadTs;
    }

    @Nullable
    public Boolean getUnfurlLinks() {
        return unfurlLinks;
    }

    public void setUnfurlLinks(@Nullable Boolean unfurlLinks) {
        this.unfurlLinks = unfurlLinks;
    }

    @Nullable
    public Boolean getUnfurlMedia() {
        return unfurlMedia;
    }

    public void setUnfurlMedia(@Nullable Boolean unfurlMedia) {
        this.unfurlMedia = unfurlMedia;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }
}
