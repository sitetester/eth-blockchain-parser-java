- `Infura` projectId should be defined as an `env` variable
- DB Schema is defined in `/resources/db/schema.sql`

- This will run multiple HTTP requests using Java 8 CompletableFuture.
- Entry point - ImportManager, update `var isFreshSetup = false;` for subsequent runs