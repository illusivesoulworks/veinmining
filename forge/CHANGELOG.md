# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to the format [MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH](https://mcforge.readthedocs.io/en/1.16.x/conventions/versioning/).

## [1.18.2-0.19] - 2022.07.28
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

## [1.18.2-0.18] - 2022.03.02
### Changed
- Updated to Minecraft 1.18.2

## [1.18.1-0.17] - 2022.02.09
### Fixed
- Fixed ConcurrentModificationException on world load [#46](https://github.com/TheIllusiveC4/VeinMining/issues/46)
- Fixed certain items not being enchantable even though they are listed in the configuration file
- Fixed default Quark item in configuration list listed as `quark:flamarang` instead of `quark:flamerang`

## [1.18.1-0.16] - 2021.12.23
### Changed
- Updated zh_cn localization (thanks EnterFor!) [#37](https://github.com/TheIllusiveC4/VeinMining/pull/37)
### Fixed
- Fixed crash with certain modded tools [#38](https://github.com/TheIllusiveC4/VeinMining/issues/38)

## [1.18.1-0.15] - 2021.12.16
### Changed
- Updated to Minecraft 1.18.1
### Fixed
- Fixed infinite loop with certain mods using capabilities [#36](https://github.com/TheIllusiveC4/VeinMining/issues/36)

## [1.18-0.14] - 2021.12.02
### Changed
- Updated to Minecraft 1.18
- Updated to Forge 38+
