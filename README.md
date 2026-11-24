# Unified Java-Terracotta Template
Welcome to what is probably DF's most cursed repository.

Note that the following explanations assume you have a knowledge of
Terracotta and Java. If you do not, go learn those.

# So what is this?
This repo contains:
- `src/main/java` - The source code for a **Spring Boot** webserver
- `src/main/terracotta` - The source code for a DiamondFire plot written in **Terracotta**
- `Dockerfile` - A dockerfile to use for deployment. When using docker compose, point a `build` line at your plot's repository from this template, and it should deploy as you expect.

# Utilities
This repository also has several utilities.

## Enqueue
You will likely need to send more than one web request at a time.

`enqueue.tc` provides a framework for batching web requests. The system for this implemented in `QueueController.java`. You can call `enqueue_request_blocking` to make a web request the current thread will block on, or call `enqueue_request_and_forget`, to not block and drop the result.

## Registries
You may need to synchronize static data between your web server and your plot. You can use the registry system to do so.

- In `RegistryResource.java`, register new registries there through the sample.
- In `loading.tc`, add in new registries in the 2 comment locations specified, so the plot can pull the registries down.