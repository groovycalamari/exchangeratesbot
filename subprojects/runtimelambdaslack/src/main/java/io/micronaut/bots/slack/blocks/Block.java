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
package io.micronaut.bots.slack.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;

@Introspected
public abstract class Block {

    /**
     * The type of block. For a file block, type is always file.
     */
    @NonNull
    private String type;

    /**
     * A string acting as a unique identifier for a block. You can use this block_id when you receive an interaction payload to identify the source of the action. If not specified, a block_id will be generated. Maximum length for this field is 255 characters.
     */
    @Nullable
    @JsonProperty("block_id")
    private String blockId;

    public Block() {
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @Nullable
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(@Nullable String blockId) {
        this.blockId = blockId;
    }
}
