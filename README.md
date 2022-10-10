# SeriousMap

A work-in-progress legal map for dungeons in Hypixel Skyblock with puzzle detection: approximately a Forge port of the excellent LegalMap ChatTriggers module by UnclaimedBloom6.

Requires Forge 1.8.9 with Essential. Spins by default!

<img width="250" alt="Screen Shot 2022-10-08 at 2 00 12 PM" src="https://user-images.githubusercontent.com/16139460/194727649-abc1d4b4-c653-46e2-babb-42599ca85883.png">

### Current features
* Map rendering, including opened wither doors
* GUI for moving and scaling map features quickly
* Puzzle detection via tab menu
* Player rendering via world and map information
* Player names shown when using spirit leap
* Optional information below map

### Possible future features
* Further background and border customization (RGB?)
* Allow editing map outside dungeons
* Score calculation
* Rare 1x1 highlighting
* Watcher done check (e.g. IllegalMap)
* Custom spirit leap menu (e.g. BetterMap)

### Todo
* Clean up code! 
* Fix the massive DungeonMap class that handles literally everything.

### Credits
* [UnclaimedBloom6/IllegalMap](https://github.com/UnclaimedBloom6/IllegalMap): player updating; puzzle detection
* [Harry282/FunnyMap](https://github.com/Harry282/FunnyMap): map data representation, virtually all rendering code, and more
* [Skytils/SkytilsMod](https://github.com/Skytils/SkytilsMod): tab list utilities
