package com.example.structure;

import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

public record BlockState(
        String name,
        Map<String, String> properties
) {
    public static BlockState fromModernVanillaFormat(CompoundTag tag) {
        return fromKeyed(tag, "id", "properties");
    }

    public static BlockState fromLegacyVanillaFormat(CompoundTag tag) {
        return fromKeyed(tag, "Name", "Properties");
    }

    @SuppressWarnings("unchecked")
    private static BlockState fromKeyed(CompoundTag tag, String idKey, String propertiesKey) {
        CompoundTag properties = tag.getCompoundTag(propertiesKey);
        if(properties != null) {
            return new BlockState(
                    tag.getString(idKey),
                    Map.ofEntries(
                            properties.entrySet()
                                    .stream().map(x -> Map.entry(
                                            x.getKey(),
                                            x.getValue()
                                                    .valueToString()
                                                    .replace("\"", "")))
                                    .toArray(Map.Entry[]::new)
                    )
            );
        } else {
            return new BlockState(
                    tag.getString(idKey),
                    Map.of()
            );
        }
    }
}
