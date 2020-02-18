# Dungeon
[![Master](https://github.com/PssbleTrngle/Dungeon/workflows/Test/badge.svg?branch=develop&event=pull_request)](https://github.com/PssbleTrngle/Dungeon/actions?query=workflow%3ATest)
[![Develop](https://img.shields.io/github/workflow/status/PssbleTrngle/Dungeon/Test/develop?label=Develop&logo=github)](https://github.com/PssbleTrngle/Dungeon/actions?query=workflow%3ATest)
[![](https://img.shields.io/github/last-commit/PssbleTrngle/Dungeon)](https://github.com/PssbleTrngle/Dungeon/commits/develop)

Dungeon is a Minecraft Mod for [Forge](http://files.minecraftforge.net/).
When loaded, a new world type will be available generating an endless dungeon, streching across multiple floors.

Encountered a bug? [Tell me](https://github.com/PssbleTrngle/Dungeon/issues/new/choose)

## Development

### Generate resources
When running `genIntellijRuns`, there will be created a `runData` task.
Executing this will generate the `data` and `assets` used for this mod,
specifically the loot, blockstates & models.
These will be placed in the `src/generated` folder
