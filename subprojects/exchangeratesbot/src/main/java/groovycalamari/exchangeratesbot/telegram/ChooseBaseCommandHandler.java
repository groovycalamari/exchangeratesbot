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
package groovycalamari.exchangeratesbot.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import groovycalamari.exchangeratesbot.ChooseBaseCommand;
import groovycalamari.exchangeratesbot.SetBaseCommandHandler;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.context.annotation.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Requires(classes = Update.class)
@Named(ChooseBaseCommand.COMMAND_CHOOSEBASE)
@Singleton
public class ChooseBaseCommandHandler extends ChooseCommandHandler {
        private static final Logger LOG = LoggerFactory.getLogger(ChooseBaseCommandHandler.class);


    protected ChooseBaseCommandHandler(ObjectMapper objectMapper,
                                       MessageComposer messageComposer) {
        super(objectMapper, messageComposer);
    }

    @NonNull
    @Override
    protected String messageText(@NonNull TelegramBot telegramBot, @NonNull Update update) {
        return "Choose your default base currency";
    }

    @Override
    protected String nextCommand() {
        return CommandHandler.COMMAND_PREFIX + SetBaseCommandHandler.COMMAND_SETBASE;
    }
}
