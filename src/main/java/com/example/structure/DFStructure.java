package com.example.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record DFStructure(
        List<String> paletteData,
        List<String> blockData
) {
    public static DFStructure of(VanillaStructure structure) {
        List<StringBuilder> paletteStr = new ArrayList<>();
        paletteStr.add(new StringBuilder());

        for(VanillaStructure.BlockState state : structure.palette()) {
            paletteStr.getLast().append(state.name())
                    .append(";")
                    .append(state.properties().entrySet().stream()
                            .map(x -> x.getKey() + "=" + x.getValue())
                            .collect(Collectors.joining(",")))
                    .append("|");
            if(paletteStr.getLast().length() > 9000) {
                paletteStr.add(new StringBuilder());
            }
        }

        List<StringBuilder> dataStr = new ArrayList<>();
        dataStr.add(new StringBuilder());
        for(VanillaStructure.BlockData block : structure.blocks()) {
            StringBuilder last = dataStr.getLast();
            last.append(block.state())
                    .append(",")
                    .append(block.x())
                    .append(",")
                    .append(block.y())
                    .append(",")
                    .append(block.z())
                    .append(";");
            if(last.length() > 9000) {
                dataStr.add(new StringBuilder());
            }
        }

        return new DFStructure(
                paletteStr.stream().map(StringBuilder::toString).toList(),
                dataStr.stream().map(StringBuilder::toString).toList()
        );
    }
}
