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

import java.io.Serializable;
import java.util.Optional;

public interface UserRepository {
    static final Currency DEFAULT_BASE = Currency.EUR;
    static final Currency DEFAULT_TARGET = Currency.USD;

    default Settings defaultSettings() {
        Settings settings = new Settings();
        settings.setBase(DEFAULT_BASE);
        settings.setTarget(DEFAULT_TARGET);
        return settings;
    }

    Optional<Settings> findByUserId(@NonNull Serializable userid);

    void updateBase(@NonNull Serializable userid, @NonNull Currency base);

    void updateTarget(@NonNull Serializable userid, @NonNull Currency target);

    void save(@NonNull Serializable userid, @NonNull Currency base, @NonNull Currency target);

    void save(@NonNull Serializable userId);
}
