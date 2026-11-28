package com.example.structure;

import dev.akarah.ucgt.web.util.BlockPos;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

import java.util.*;

public record LitematicaStructure(
        List<Region> regions
) {
    public record Region(
            BlockPos position,
            BlockPos size,
            List<BlockState> palette,
            Map<BlockPos, Integer> blockStateIndices
    ) {}

    public static LitematicaStructure of(CompoundTag tag) {
        Collection<Tag<?>> regionTags = tag.getCompoundTag("Regions").values();

        List<Region> regions = new ArrayList<>();
        for(Tag<?> subtag : regionTags) {
            regions.add(parseRegion((CompoundTag) subtag));
        }

        return new LitematicaStructure(regions);
    }

    private static Region parseRegion(CompoundTag tag) {
        BlockPos position = BlockPos.fromCompoundXYZ(tag.getCompoundTag("Position"));
        BlockPos size = BlockPos.fromCompoundXYZ(tag.getCompoundTag("Size"));

        List<BlockState> palette = new ArrayList<>();
        ListTag<CompoundTag> blockStatePalette = tag.getListTag("BlockStatePalette").asCompoundTagList();
        for(CompoundTag subtag : blockStatePalette) {
            palette.add(BlockState.fromLegacyVanillaFormat(subtag));
        }

        Map<BlockPos, Integer> blockStateIndicesMap = new HashMap<>();
        long[] blockStateIndices = tag.getLongArray("BlockStates");
        // Litematica: bits = max(2, bits needed to represent paletteSize - 1)
        int bitsPerEntry = Math.max(2, 32 - Integer.numberOfLeadingZeros(Math.max(1, palette.size() - 1)));
        // Litematica region sizes may be negative (region extends in the negative direction)
        int sizeX = Math.abs(size.x());
        int sizeY = Math.abs(size.y());
        int sizeZ = Math.abs(size.z());
        for(int y = 0; y < sizeY; y++) {
            for(int z = 0; z < sizeZ; z++) {
                for(int x = 0; x < sizeX; x++) {
                    int entryIdx = y * (sizeX * sizeZ) + z * sizeX + x;
                    int paletteIdx = (int) readBitEntry(blockStateIndices, entryIdx, bitsPerEntry);
                    blockStateIndicesMap.put(new BlockPos(x, y, z), paletteIdx);
                }
            }
        }

        return new Region(
                position,
                size,
                palette,
                blockStateIndicesMap
        );
    }

    private static long readBitEntry(long[] arr, int index, int bits) {
        long mask = (1L << bits) - 1;
        long startBit = (long) index * bits;
        int startLong = (int) (startBit >>> 6);              // /64
        int endLong   = (int) (((index + 1L) * bits - 1) >>> 6);
        int offset    = (int) (startBit & 63);               // %64

        if (startLong == endLong) {
            return (arr[startLong] >>> offset) & mask;
        } else {                                             // spans two longs
            return ((arr[startLong] >>> offset) | (arr[endLong] << (64 - offset))) & mask;
        }
    }
}
