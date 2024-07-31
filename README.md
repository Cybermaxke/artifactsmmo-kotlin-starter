# Artifacts MMO - Kotlin Starter

## Features
- Generates http clients based on the open api spec provided by Artifacts MMO
- Use kotlin coroutines to interact with the http clients

## Getting started
- Create a file src/main/resources/token and put the token from your [account](https://artifactsmmo.com/account) in it.
- Run Main.kt to test it out

## Notes
- Enum types in the open api spec are interpreted as strings
- Deprecated fields in the open api spec will be ignored
- To force update the open api spec, delete openapi.json and build again
