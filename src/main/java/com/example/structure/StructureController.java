package com.example.structure;

import dev.akarah.ucgt.web.structure.DFStructure;
import dev.akarah.ucgt.web.structure.StructureManager;
import dev.akarah.ucgt.web.structure.VanillaStructure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StructureController {
    private StructureManager structureManager;

    public StructureController(StructureManager manager) {
        this.structureManager = manager;
    }

    @GetMapping("/v1/structure/{id}")
    public DFStructure get(@PathVariable String id) {
        return DFStructure.of(VanillaStructure.of(structureManager.getStructure(id)));
    }
}
