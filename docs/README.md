# Vein Mining [![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg?&style=flat-square)](https://www.gnu.org/licenses/lgpl-3.0) [![ko-fi](https://img.shields.io/badge/Support%20Me-Ko--fi-%23FF5E5B?style=flat-square)](https://ko-fi.com/C0C1NL4O)

Vein Mining is a mod that adds the titular Vein Mining enchantment, which allows the enchanted tool
to break matching connected blocks. The enchantment and mining logic are highly configurable, letting
players and modpack developers find their preferred method of balance.

[![BisectHosting](https://i.postimg.cc/prDcRzJ8/logo-final.png)](https://bisecthosting.com/illusive)

## Features

### Enchantment
![](https://i.ibb.co/q7g1qRd/veinminingenchantment.png)

**Configuration Options**
- Rarity
- Max Level
- Treasure
- Randomly Enchantable
- Available on Books
- Base Enchanting Power
- Enchanting Power per Level
- (_Forge_) Villager Trade
- (_Forge_) Lootable

### Vein Mining

![](https://i.ibb.co/4NC5JcR/veinmining.gif)

**Configuration Options**
- Max Blocks Base (without enchantment)
- Max Distance Base (without enchantment)
- Max Blocks per Enchantment Level
- Max Distance per Enchantment Level
- Diagonal Mining
- Relocate Drops
- Limit Mining by Tool Durability
- Prevent Tool Breaking
- Tool Damage
- Tool Damage Multiplier
- Player Exhaustion
- Player Exhaustion Multiplier
- Activation Method (sneak or stand)
- Blocks Whitelist/Blacklist
- Custom Block Groups (Tags + IDs)

## Downloads

**CurseForge**
- [Vein Mining for Forge](https://www.curseforge.com/minecraft/mc-mods/vein-mining/files)
- [Vein Mining for Fabric](https://www.curseforge.com/minecraft/mc-mods/vein-mining-fabric/files)

## Developing

**Help! I'm getting Mixin crashes when I try to launch in development on Forge!**

Vein Mining uses Mixins to implement its core features. This may cause issues when depending on
Vein Mining for your project inside a development environment since ForgeGradle/MixinGradle do not yet
support this natively like on the Fabric toolchain. As a workaround, please disable the refmaps in
development by setting the `mixin.env.disableRefMap` JVM argument to `true` in your run
configuration.

## Support

Please report all bugs, issues, and feature requests to the
[issue tracker](https://github.com/TheIllusiveC4/VeinMining/issues).

For non-technical support and questions, join the developer's [Discord](https://discord.gg/JWgrdwt).

## License

All source code and assets are licensed under LGPL 3.0.

## Donations

Donations to the developer can be sent through [Ko-fi](https://ko-fi.com/C0C1NL4O).
