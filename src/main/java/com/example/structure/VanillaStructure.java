package com.example.structure;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record VanillaStructure(
    List<BlockState> palette,
    List<BlockData> blocks
) {
    public record BlockState(
            String name,
            Map<String, String> properties
    ) {}

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
            CompoundTag properties = subtag.getCompoundTag("properties");
            System.out.println(properties);

            if(properties != null) {
                palette.add(new BlockState(
                        subtag.getString("id"),
                        Map.ofEntries(
                                properties.entrySet()
                                        .stream().map(x -> Map.entry(
                                                x.getKey(),
                                                x.getValue()
                                                        .valueToString()
                                                        .replace("\"", "")))
                                        .toArray(Map.Entry[]::new)
                        )
                ));
            } else {
                palette.add(new BlockState(
                        subtag.getString("id"),
                        Map.of()
                ));
            }

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
