package com.example.structure;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;

public record VanillaStructure(
    List<BlockState> palette,
    List<BlockData> blocks
) {

    public record BlockData(
            int x,
            int y,
            int z,
            int state
    ) {}

    @SuppressWarnings("unchecked")
    public static VanillaStructure of(CompoundTag tag) {
        List<BlockState> palette = new ArrayList<>();
        List<BlockData> blocks = new ArrayList<>();

        ListTag<CompoundTag> paletteList = tag.getListTag("palette").asCompoundTagList();
        for(CompoundTag subtag : paletteList) {
            palette.add(BlockState.fromModernVanillaFormat(subtag));
        }

        ListTag<CompoundTag> blockList = tag.getListTag("blocks").asCompoundTagList();
        for(CompoundTag subtag : blockList) {
            blocks.add(new BlockData(
                    subtag.getListTag("pos").asIntTagList().get(0).asInt(),
                    subtag.getListTag("pos").asIntTagList().get(1).asInt(),
                    subtag.getListTag("pos").asIntTagList().get(2).asInt(),
                    subtag.getInt("state")
            ));
        }

        return new VanillaStructure(palette, blocks);
    }
}
