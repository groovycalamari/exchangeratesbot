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
