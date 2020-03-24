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
import io.micronaut.context.annotation.Secondary;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Optional;

@Secondary
@Singleton
public class FallbackUserRepository implements UserRepository {
    @Override
    public Optional<Settings> findByUserId(@NonNull Serializable userid) {
        return Optional.of(defaultSettings());
    }

    @Override
    public void updateBase(@NonNull Serializable userid, @NonNull Currency base) {

    }

    @Override
    public void updateTarget(@NonNull Serializable userid, @NonNull Currency target) {

    }

    @Override
    public void save(@NonNull Serializable userid, @NonNull Currency base, @NonNull Currency target) {

    }

    @Override
    public void save(@NonNull Serializable userId) {

    }
}
