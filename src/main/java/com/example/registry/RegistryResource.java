package com.example.registry;

import org.jspecify.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistryResource {
//    public Registry<ItemType> itemTypes = loadFromClasspath(ItemType.class, "item_type");

    public Registry<Registry<?>> allRegistries = new Registry<Registry<?>>() {
        @Override
        public @Nullable Registry<?> getOrNull(String key) {
            return switch (key) {
                case "registries" -> allRegistries;
//                case "item_type" -> itemTypes;
                default -> null;
            };
        }

        @Override
        public List<String> keys() {
            return List.of("item_type", "mob_type");
        }
    };

    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static final JsonMapper mapper = new JsonMapper();

    private static <T> MappedRegistry<T> loadFromClasspath(Class<T> clazz, String registry) {
        try {
            Map<String, T> out = new HashMap<>();

            String leadingDir = registry + "/";
            Resource[] resources = resolver.getResources("classpath:" + leadingDir + "**/*.*");

            for (Resource r : resources) {
                if (!r.isReadable()) continue;

                String url = r.getURL().toString();
                String relative = url.substring(url.lastIndexOf(leadingDir) + leadingDir.length());

                try (InputStream in = r.getInputStream()) {
                    String inputData = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    T data = mapper.readValue(inputData, clazz);
                    out.put(relative.replace(".json", ""), data);
                }
            }

            return new MappedRegistry<>(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
