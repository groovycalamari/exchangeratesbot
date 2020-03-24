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
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Settings {
    @NonNull
    private Currency base;

    @NonNull
    private Currency target;

    public Settings() {
    }

    @NonNull
    public Currency getBase() {
        return base;
    }

    public void setBase(@NonNull Currency base) {
        this.base = base;
    }

    @NonNull
    public Currency getTarget() {
        return target;
    }

    public void setTarget(@NonNull Currency target) {
        this.target = target;
    }
}
