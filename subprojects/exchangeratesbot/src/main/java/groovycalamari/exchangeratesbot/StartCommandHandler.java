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
package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.FileCommandHandler;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.core.ParseMode;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Optional;

@Named(StartCommandHandler.COMMAND_START)
@Singleton
public class StartCommandHandler extends FileCommandHandler {

    public static final String COMMAND_START = "start";
    private final UserRepository userRepository;
    private final ChatBotMessageParser messageParser;

    public StartCommandHandler(MessageComposer messageComposer,
                               ChatBotMessageParser messageParser,
                               UserRepository userRepository) {
        super(ParseMode.MARKDOWN, "classpath:help.md", messageComposer);
        this.userRepository = userRepository;
        this.messageParser = messageParser;
    }

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull ChatBot bot,
                                           @NonNull ChatBotMessageReceive messageReceive) {
        Optional<String> reply = super.replyUpdate(bot, messageReceive);

        Optional<Serializable> userIdOptional = messageParser.parseUserId(messageReceive);
        if (userIdOptional.isPresent()) {
            Serializable userId = userIdOptional.get();
            if (!userRepository.findByUserId(userId).isPresent()) {
                userRepository.save(userId);
            }
        }
        return reply;
    }
}
