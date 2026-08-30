# The Thunder Mod

A tiny NeoForge **1.21.1** mod that lets you summon a thunderstorm by building a simple structure.

## How it works

Build a complete **3x3 dirt platform**, then place **one cobblestone block directly above the center dirt block**.

When the structure is completed:

- Lightning strikes the center cobblestone.
- A thunderstorm starts immediately.
- The storm lasts **6 minutes** (7200 game ticks).
- Nothing is printed in chat.
- There is no cooldown. Break/rebuild the trigger and you can use it again as many times as you want.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.235 or newer 21.1.x build compatible with Minecraft 1.21.1
- Java 21

## Building locally

This repository uses NeoGradle. With Gradle 9.2.1 installed, run:

`gradle build`

The compiled mod will be placed in `build/libs/`.

## Automatic builds and releases

Every push to `main` runs the GitHub Actions build pipeline. If the build succeeds, GitHub automatically creates a release named after that workflow run and attaches the compiled JAR.

You can grab the newest JAR from the repository's **Releases** page.

## License

MIT. See [LICENSE](LICENSE).
