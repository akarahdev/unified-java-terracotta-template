package com.example.structure;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class StructureManager {
    private Map<String, DFStructure> nbtStructures = new HashMap<>();
    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public StructureManager() {
        this.loadFromClasspath();
    }

    private void loadFromClasspath() {
        try {
            String leadingDir = "structure/";
            org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:" + leadingDir + "**/*.*");

            for (org.springframework.core.io.Resource r : resources) {
                if (!r.isReadable()) continue;

                String url = r.getURL().toString();
                String relative = url.substring(url.lastIndexOf(leadingDir) + leadingDir.length());

                try (InputStream in = r.getInputStream()) {
                    String id = relative.replace(".nbt", "").replace(".litematic", "");
                    File tmp = File.createTempFile("structures", id);

                    try(FileOutputStream os = new FileOutputStream(tmp)) {
                        os.write(in.readAllBytes());
                    }

                    // vanilla structure
                    if(relative.endsWith(".nbt")) {
                        CompoundTag data = (CompoundTag) NBTUtil.read(tmp).getTag();
                        VanillaStructure vanilla = VanillaStructure.of(data);
                        DFStructure dfStructure = DFStructure.of(vanilla);
                        nbtStructures.put(id, dfStructure);
                    }
                    if(relative.endsWith(".litematic")) {
                        CompoundTag data = (CompoundTag) NBTUtil.read(tmp).getTag();
                        LitematicaStructure vanilla = LitematicaStructure.of(data);
                        DFStructure dfStructure = DFStructure.of(vanilla);
                        nbtStructures.put(id, dfStructure);
                    }

                    System.out.println(id);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DFStructure getStructure(String id) {
        System.out.println(this.nbtStructures.keySet());
        return this.nbtStructures.get(id);
    }
}
