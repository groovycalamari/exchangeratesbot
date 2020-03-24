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

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class AmountAtDateHandlerSpec extends Specification {

    @Subject
    @Shared
    AmountAtDateHandler handler = new AmountAtDateHandler(null, null, null, null)

    void "matches 2020-01-02 1000"() {
        expect:
        handler.matches('2020-01-02 1000')

        and:
        handler.matches('2020-01-02 10.99')

        and:
        !handler.matches('2020-01-02 foo')
    }

    void "parseAmount 2020-01-02 4750"() {
        expect:
        handler.parseAmount("2020-01-02 4750").get() == 4750.0

        and:
        handler.parseAmount("2020-01-02 10.99").get() == 10.99

        and:
        handler.parseAmount("2020-01-02 10.5").get() == 10.5
    }

    void "parseDtsr 2020-01-02 4750"() {
        expect:
        handler.parseDateStr("2020-01-02 4750").get() == '2020-01-02'
    }
}
