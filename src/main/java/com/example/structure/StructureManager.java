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
    private Map<String, CompoundTag> nbtStructures = new HashMap<>();
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
                    String id = relative.replace(".nbt", "");
                    File tmp = File.createTempFile("structures", id);

                    try(FileOutputStream os = new FileOutputStream(tmp)) {
                        os.write(in.readAllBytes());
                    }

                    CompoundTag data = (CompoundTag) NBTUtil.read(tmp).getTag();
                    nbtStructures.put(id, data);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompoundTag getStructure(String id) {
        System.out.println(this.nbtStructures.keySet());
        return this.nbtStructures.get(id);
    }
}
