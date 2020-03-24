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
package groovycalamari.exchangeratesbot.googlechat;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.ChatBotMessageSend;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.googlechat.core.Event;
import io.micronaut.bots.googlechat.core.GoogleChatBot;
import io.micronaut.bots.googlechat.core.GoogleChatButton;
import io.micronaut.bots.googlechat.core.GoogleChatCard;
import io.micronaut.bots.googlechat.core.GoogleChatCardSection;
import io.micronaut.bots.googlechat.core.GoogleChatCards;
import io.micronaut.bots.googlechat.core.GoogleChatFormAction;
import io.micronaut.bots.googlechat.core.GoogleChatOnClick;
import io.micronaut.bots.googlechat.core.GoogleChatTextButton;
import io.micronaut.bots.googlechat.core.GoogleChatTextParagraph;
import io.micronaut.bots.googlechat.core.GoogleChatWidget;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class InlineKeyboardHandler implements CommandHandler {

    @Override
    public <T extends ChatBotMessageSend> Optional<T> handle(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive chatUpdate) {


        if (!(chatUpdate instanceof Event) || !(bot instanceof GoogleChatBot)) {
            return Optional.empty();
        }

        GoogleChatWidget widget = new GoogleChatWidget(buttons(bot), new GoogleChatTextParagraph(messageText(chatUpdate)));
        GoogleChatCardSection section = new GoogleChatCardSection(null, Collections.singletonList(widget));
        GoogleChatCard card = new GoogleChatCard();
        card.setSections(Collections.singletonList(section));
        GoogleChatCards googleChatCards = new GoogleChatCards(Collections.singletonList(card));
        Optional<ChatBotMessageSend> chatBotResponseOptional = Optional.of(googleChatCards);
        return (Optional) chatBotResponseOptional;
    }

    protected GoogleChatButton button(String text, String callback) {
        GoogleChatButton btn = new GoogleChatButton();
        btn.setTextButton(textButton(text, callback));
        return btn;
    }

    protected GoogleChatTextButton textButton(String text, String callback) {
        GoogleChatTextButton btn = new GoogleChatTextButton();
        GoogleChatOnClick onClick = new GoogleChatOnClick(new GoogleChatFormAction(callback));
        btn.setOnClick(onClick);
        btn.setText(text);
        return btn;
    }

    @NonNull
    protected abstract String messageText(@NonNull ChatBotMessageReceive chatUpdate);

    protected abstract List<GoogleChatButton> buttons(@NonNull ChatBot bot);
}
