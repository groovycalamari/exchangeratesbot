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
package groovycalamari.exchangeratesbot

import io.micronaut.bots.core.FileCommandHandler
import io.micronaut.inject.qualifiers.Qualifiers

class HelpCommandSpec extends ApplicationContextSpecification {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + [
                'telegram.commands.help.parse-mode': 'Markdown',
                'telegram.commands.help.path': 'classpath:help.md',
        ]
    }

    void "help command defined"() {
        expect:
        applicationContext.containsBean(FileCommandHandler, Qualifiers.byName("help"))
    }
}
