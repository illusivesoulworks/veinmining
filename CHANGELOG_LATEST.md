The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

This is a copy of the changelog for the most recent version. For the full version history, go [here](https://github.com/illusivesoulworks/veinmining/blob/1.20.x/CHANGELOG.md).

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
