# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to the format [MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH](https://mcforge.readthedocs.io/en/1.16.x/conventions/versioning/).

## [1.17.1-0.12] - 2021.09.24
### Changed
- Updated to Minecraft 1.17.1
- Updated to Forge 37.0.59+

## [1.16.5-0.12] - 2021.06.22
### Added
- Added config option for `items` that the enchantment can be applied to [#16](https://github.com/TheIllusiveC4/VeinMining/issues/16)
- Added Quark compatibility for Pickarang and Flamarang [#15](https://github.com/TheIllusiveC4/VeinMining/issues/15)

## [1.16.5-0.11] - 2021.06.15
### Fixed
- Fixed certain blocks such as Nether Quartz Ore not working properly with the `requireEffectiveTool` config option [#21](https://github.com/TheIllusiveC4/VeinMining/issues/21)

## [1.16.5-0.10] - 2021.03.20
### Fixed
- Fixed certain blocks such as Redstone Ore not working properly with the `requireEffectiveTool` config option [#12](https://github.com/TheIllusiveC4/VeinMining/issues/12)

## [1.16.5-0.9] - 2021.03.16
### Added
- Added `KEYBINDING` to activation method config option [#7](https://github.com/TheIllusiveC4/VeinMining/issues/7)
### Fixed
- Fixed vein mining sometimes resulting in duplicate blocks [#11](https://github.com/TheIllusiveC4/VeinMining/issues/11)

## [1.16.5-0.8] - 2021.01.31
### Added
- Added `incompatibleEnchantments` config option

## [1.16.5-0.7] - 2021.01.31
### Added
- Added Chinese translation (thanks EnterFor!)
### Fixed
- Fixed `requireEffectiveTool` config option not working properly for blocks that do not specify a
harvest tool

## [1.16.4-0.6] - 2021.01.12
### Added
- Added Brazilian Portuguese translation (thanks Mikeliro!)
### Fixed
- Fixed Vein Mining enchantment book not appearing in Creative search

## [1.16.4-0.5] - 2021.01.10
### Added
- Added configuration option for requireEffectiveTool to limit vein mining only to blocks that the
respective tool is effective on

## [1.16.4-0.4] - 2021.01.01
### Fixed
- Fixed Vein Mining appearing as an enchantment option for non-tools

## [1.16.4-0.3] - 2020.12.30
### Added
- Added configuration options for maxBlocksBase and maxDistanceBase for granting vein mining
abilities without the enchantment

## [1.16.4-0.2] - 2020.12.29
### Fixed
- Fixed NullPointerException with Quark [#1](https://github.com/TheIllusiveC4/VeinMining/issues/1)

## [1.16.4-0.1] - 2020.12.28
Initial beta release
