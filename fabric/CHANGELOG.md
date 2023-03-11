# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](https://semver.org/).

## [0.0.17-1.18.2] - 2023.03.11
### Fixed
- Fixed vein mining ignoring block-specific and item-specific behavior

## [0.0.16-1.18.2] - 2022.12.30
### Changed
- Changed the localization of the activation keybinding from `Activate` to `Activate Vein Mining` for clarity of purpose

## [0.0.15-1.18.2] - 2022.10.05
### Changed
- Items that are unbreakable or cannot be damaged now bypass the `limitedByDurability` configuration [#67](https://github.com/illusivesoulworks/veinmining/issues/67)

## [0.0.14-1.18.2] - 2022.09.01
### Added
- Added `activationStateWithoutEnchantment` configuration option, defaulted to `"KEYBINDING"`, which controls the
  activation method if `maxBlocksBase` and `maxDistanceBase` values are greater than 0 (which would mean that the
  enchantment is not required for vein mining functionality)
### Changed
- `maxBlocksBase` and `maxBlocksPerLevel` now account for the origin block [#60](https://github.com/TheIllusiveC4/VeinMining/issues/60)

## [0.0.13-1.18.2] - 2022.06.30
### Changed
- Changed `requireEffectiveTool` configuration feature so that any time blocks can be harvest correctly is counted as being effective
### Fixed
- Fixed threading issue with Dimensional Threading mod

## [0.0.12-1.18.2] - 2022.03.01
### Changed
- Updated to Minecraft 1.18.2

## [0.0.11-1.18] - 2021.12.02
### Changed
- Updated to Minecraft 1.18
