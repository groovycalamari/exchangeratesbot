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
import groovycalamari.exchangeratesbot.ChooseBaseCommand;
import groovycalamari.exchangeratesbot.ChooseTargetCommand;
import groovycalamari.exchangeratesbot.Settings;
import groovycalamari.exchangeratesbot.UserRepository;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.googlechat.core.GoogleChatButton;
import io.micronaut.context.annotation.Requires;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Requires(classes = GoogleChatButton.class)
@Named(SettingsCommandHandler.COMMAND_SETTINGS)
@Singleton
public class SettingsCommandHandler extends InlineKeyboardHandler {

    public static final String COMMAND_SETTINGS = "settings";
    public static final String BASE_CURRENCY = "Base Currency";
    public static final String TARGET_CURRENCY = "Target Currency";
    private final UserRepository userRepository;
    protected final ChatBotMessageParser messageParser;

    protected SettingsCommandHandler(ChatBotMessageParser messageParser,
                                     UserRepository userRepository) {
        this.messageParser = messageParser;
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    protected String messageText(@NonNull ChatBotMessageReceive chatUpdate) {
        String defaultText = "Configure your default base and target currencies";
        Optional<Serializable> userIdOptional = messageParser.parseUserId(chatUpdate);
        if (userIdOptional.isPresent()) {
            Serializable userId = userIdOptional.get();
            Optional<Settings> settingsOptional = userRepository.findByUserId(userId);
            if (!settingsOptional.isPresent()) {
                userRepository.save(userId);
                settingsOptional = userRepository.findByUserId(userId);
            }
            if (settingsOptional.isPresent()) {
                Settings settings = settingsOptional.get();
                return "Your base currency is: " + settings.getBase().getCode() + " and your target currency is: " + settings.getTarget().getCode() + "\n" + defaultText;
            }
        }
        return defaultText;
    }

    @Override
    protected List<GoogleChatButton> buttons(@NonNull ChatBot bot) {
        return Arrays.asList(
                button(BASE_CURRENCY, baseCallback(bot)),
                button(TARGET_CURRENCY, targetCallback(bot)));
    }


    private String baseCallback(@NonNull ChatBot bot) {
        return bot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseBaseCommand.COMMAND_CHOOSEBASE;
    }

    private String targetCallback(@NonNull ChatBot bot) {
        return bot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseTargetCommand.COMMAND_CHOOSETARGET;
    }
}
