package com.example.registry;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/registry")
public class RegistryController {
    private final RegistryResource registries;
    private static int PAGE_SIZE = 10;

    public RegistryController(RegistryResource registries) {
        this.registries = registries;
    }

    @GetMapping("/count/{registry}")
    public int count(@PathVariable String registry) {
        return registries.allRegistries.getOrThrow(registry).size();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/dump/{registryId}")
    public Map<String, ?> dump(@PathVariable String registryId, @RequestParam int page, @RequestParam int amount) {
        Registry<?> registry = registries.allRegistries.getOrThrow(registryId);

        Map.Entry<String, ?>[] mapEntries = registry.keys()
                .subList(Math.min(page * amount, registry.keys().size()), Math.min((page * amount) + amount, registry.keys().size()))
                .stream()
                .map(x -> Map.entry(x, registry.getOrThrow(x)))
                .toArray(Map.Entry[]::new);

        return Map.ofEntries(mapEntries);
    }
}
