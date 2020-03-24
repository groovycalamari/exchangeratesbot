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
import io.exchangeratesapi.Currency;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.core.MessageComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;

@Singleton
@Named(SetTargetCommandHandler.COMMAND_SETTARGET)
public class SetTargetCommandHandler extends SetCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SetTargetCommandHandler.class);

    public static final String COMMAND_SETTARGET = "settarget";

    private final UserRepository userRepository;
    public SetTargetCommandHandler(ChatBotMessageParser messageParser,
                                   MessageComposer messageComposer,
                                   UserRepository userRepository) {
        super(messageParser, messageComposer);
        this.userRepository = userRepository;
    }

    @Override
    protected String successMessage(@NonNull Currency currency) {
        return "Target currency set to " + currency.getCode();
    }

    protected String getCommandName() {
        return CommandHandler.COMMAND_PREFIX + COMMAND_SETTARGET;
    }

    @Override
    protected void updateAttribute(@NonNull Serializable userId, @NonNull Currency currency) {
        userRepository.updateTarget(userId, currency);
    }


}
