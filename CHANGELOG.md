# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).
Prior to version 1.0.0, this project used MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.

## [2.0.0+1.20.2] - 2023.10.16
### Changed
- Updated to Minecraft 1.20.2

## [1.2.1+1.20.1] - 2023.10.11
### Changed
- Updated SpectreLib to 0.13.14
- [Fabric] Updated to Fabric Loader 0.14.23
### Fixed
- Fixed blocks being destroyed when they shouldn't [#102](https://github.com/illusivesoulworks/veinmining/issues/102)
- Fixed configuration files being reset while loaded into a world [#98](https://github.com/illusivesoulworks/veinmining/issues/98)

## [1.2.0+1.20.1] - 2023.06.17
### Added
- Added `requiredDestroySpeed` to `veinmining-server.toml` to determine the needed speed on a used tool to successfully
vein mine blocks
- Added in-game configuration GUI
### Changed
- Updated to Minecraft 1.20.1
- Configuration options have been renamed with new updated comments
- `Activate Vein Mining` keybinding will instead display `(Disabled by Configuration)` if the current configuration does
not use the keybinding
### Removed
- Removed `maxDistanceBase` and `maxDistancePerLevel` configuration options

## [1.1.2+1.19.4] - 2023.04.21
### Changed
- Updated to Minecraft 1.19.4

## [1.1.2+1.19.3] - 2023.04.20
### Changed
- [Fabric/Quilt] Experience orb drops now relocate with block drops if `relocateDrops` is true [#92](https://github.com/illusivesoulworks/veinmining/issues/92)

## [1.1.1+1.19.3] - 2023.03.09
### Fixed
- Fixed potential NPE crash

## [1.1.0+1.19.3] - 2023.03.08
### Added
- Added `fr_fr` localization (thanks Calvineries!) [#80](https://github.com/illusivesoulworks/veinmining/pull/80)
### Changed
- Updated `pt_br` localization (thanks FITFC!) [#74](https://github.com/illusivesoulworks/veinmining/pull/74)
### Fixed
- Fixed configuration loading errors by applying workarounds to avoid crashes or invalid data
- Fixed vein mining ignoring block-specific and item-specific behavior

## [1.0.1+1.19.3] - 2022.12.22
### Added
- Added Quilt support
### Changed
- Updated to Minecraft 1.19.3
- [Forge] Updated to Forge 44+
- [Fabric] Updated to Fabric API 0.69.0+

## [1.0.0+1.19.2] - 2022.12.22
### Fixed
- Fixed vein mining processing incorrect blocks in certain instances [#73](https://github.com/illusivesoulworks/veinmining/issues/73)

## [1.0.0-beta.5+1.19.2] - 2022.10.05
### Changed
- Items that are unbreakable or cannot be damaged now bypass the `limitedByDurability` configuration [#67](https://github.com/illusivesoulworks/veinmining/issues/67)
### Fixed
- [Fabric] Fixed mixin mod compatibility issue with EnchantmentHelper [#68](https://github.com/illusivesoulworks/veinmining/issues/68)

## [1.0.0-beta.4+1.19.2] - 2022.09.25
### Fixed
- [Fabric] Fixed crash with Fabric ASM and some mods trying to use mixin accessors [#64](https://github.com/illusivesoulworks/veinmining/issues/64)
[#66](https://github.com/illusivesoulworks/veinmining/issues/66)

## [1.0.0-beta.3+1.19.2] - 2022.09.22
### Fixed
- [Fabric] Fixed crash with other mods accessing configuration values before they are loaded [#63](https://github.com/illusivesoulworks/veinmining/issues/63)

## [1.0.0-beta.2+1.19.2] - 2022.09.16
### Fixed
- [Forge] Fixed potential crash when using other mods with SpectreLib [#62](https://github.com/illusivesoulworks/veinmining/issues/62)

## [1.0.0-beta.1+1.19.2] - 2022.09.16
### Added
- [Fabric] Added enchantment configurations for compatible items and allowing book enchanting
### Changed
- Merged Forge and Fabric versions of the project together using the [MultiLoader template](https://github.com/jaredlll08/MultiLoader-Template)
- Configuration system is now provided by SpectreLib
- Configuration files are now located in the root folder's `config` folder
- Changed to Semantic Versioning
- Updated to Minecraft 1.19.2
- [Forge] Updated to Forge 43+
- [Fabric] Updated to Fabric API 0.61.0+
- [Fabric] Changed vein mining logic to use Fabric API block break events for increased mod compatibility

## [1.18.2-0.0.0.20] - 2022.09.01
### Changed
- `maxBlocksBase` and `maxBlocksPerLevel` now account for the origin block [#60](https://github.com/TheIllusiveC4/VeinMining/issues/60)

## [1.18.2-0.0.0.19] - 2022.07.28
### Added
- Added `activationStateWithoutEnchantment` configuration option, defaulted to `"KEYBINDING"`, which controls the
activation method if `maxBlocksBase` and `maxDistanceBase` values are greater than 0 (which would mean that the 
enchantment is not required for vein mining functionality)
### Changed
- Now requires Forge 40.1.0 or above
- Changed vein mining logic to accommodate for custom harvest logic from modded tools
- As a side effect of the above change, vein mining now works in Creative as well
### Removed
- Removed mixins

## [1.18.2-0.0.0.18] - 2022.03.02
### Changed
- Updated to Minecraft 1.18.2

## [1.18.1-0.0.0.17] - 2022.02.09
### Fixed
- Fixed ConcurrentModificationException on world load [#46](https://github.com/TheIllusiveC4/VeinMining/issues/46)
- Fixed certain items not being enchantable even though they are listed in the configuration file
- Fixed default Quark item in configuration list listed as `quark:flamarang` instead of `quark:flamerang`

## [1.18.1-0.0.0.16] - 2021.12.23
### Changed
- Updated zh_cn localization (thanks EnterFor!) [#37](https://github.com/TheIllusiveC4/VeinMining/pull/37)
### Fixed
- Fixed crash with certain modded tools [#38](https://github.com/TheIllusiveC4/VeinMining/issues/38)

## [1.18.1-0.0.0.15] - 2021.12.16
### Changed
- Updated to Minecraft 1.18.1
### Fixed
- Fixed infinite loop with certain mods using capabilities [#36](https://github.com/TheIllusiveC4/VeinMining/issues/36)

## [1.18-0.0.0.14] - 2021.12.02
### Changed
- Updated to Minecraft 1.18
- Updated to Forge 38+

## [1.17.1-0.0.0.16] - 2021.12.23
### Fixed
- Fixed crash with certain modded tools [#38](https://github.com/TheIllusiveC4/VeinMining/issues/38)

## [1.17.1-0.0.0.15] - 2021.12.16
### Fixed
- Fixed infinite loop with certain mods using capabilities [#36](https://github.com/TheIllusiveC4/VeinMining/issues/36)

## [1.17.1-0.0.0.14] - 2021.11.12
### Fixed
- Fixed `canApplyAtEnchantingTable` config not being applied correctly [#33](https://github.com/TheIllusiveC4/VeinMining/issues/33)

## [1.17.1-0.0.0.13] - 2021.11.08
### Changed
- Changed `compatibleItems` logic to support more types of modded items
### Fixed
- Fixed `preventToolDestruction` config not being applied correctly [#32](https://github.com/TheIllusiveC4/VeinMining/issues/32)

## [1.17.1-0.0.0.12] - 2021.09.24
### Changed
- Updated to Minecraft 1.17.1
- Updated to Forge 37.0.59+

## [1.16.5-0.0.0.12] - 2021.06.22
### Added
- Added config option for `items` that the enchantment can be applied to [#16](https://github.com/TheIllusiveC4/VeinMining/issues/16)
- Added Quark compatibility for Pickarang and Flamarang [#15](https://github.com/TheIllusiveC4/VeinMining/issues/15)

## [1.16.5-0.0.0.11] - 2021.06.15
### Fixed
- Fixed certain blocks such as Nether Quartz Ore not working properly with the `requireEffectiveTool` config option [#21](https://github.com/TheIllusiveC4/VeinMining/issues/21)

## [1.16.5-0.0.0.10] - 2021.03.20
### Fixed
- Fixed certain blocks such as Redstone Ore not working properly with the `requireEffectiveTool` config option [#12](https://github.com/TheIllusiveC4/VeinMining/issues/12)

## [1.16.5-0.0.0.9] - 2021.03.16
### Added
- Added `KEYBINDING` to activation method config option [#7](https://github.com/TheIllusiveC4/VeinMining/issues/7)
### Fixed
- Fixed vein mining sometimes resulting in duplicate blocks [#11](https://github.com/TheIllusiveC4/VeinMining/issues/11)

## [1.16.5-0.0.0.8] - 2021.01.31
### Added
- Added `incompatibleEnchantments` config option

## [1.16.5-0.0.0.7] - 2021.01.31
### Added
- Added Chinese translation (thanks EnterFor!)
### Fixed
- Fixed `requireEffectiveTool` config option not working properly for blocks that do not specify a harvest tool

## [1.16.4-0.0.0.6] - 2021.01.12
### Added
- Added Brazilian Portuguese translation (thanks Mikeliro!)
### Fixed
- Fixed Vein Mining enchantment book not appearing in Creative search

## [1.16.4-0.0.0.5] - 2021.01.10
### Added
- Added configuration option for requireEffectiveTool to limit vein mining only to blocks that the respective tool is effective on

## [1.16.4-0.0.0.4] - 2021.01.01
### Fixed
- Fixed Vein Mining appearing as an enchantment option for non-tools

## [1.16.4-0.0.0.3] - 2020.12.30
### Added
- Added configuration options for maxBlocksBase and maxDistanceBase for granting vein mining abilities without the enchantment

## [1.16.4-0.0.0.2] - 2020.12.29
### Fixed
- Fixed NullPointerException with Quark [#1](https://github.com/TheIllusiveC4/VeinMining/issues/1)

## [1.16.4-0.0.0.1] - 2020.12.28
Initial beta release
