# Vein Mining

Vein Mining is a mod that adds the titular Vein Mining enchantment, which allows the enchanted tool
to break matching connected blocks. The enchantment and mining logic are highly configurable, letting
players and modpack developers find their preferred method of balance.

## Features

### Enchantment
![](https://i.ibb.co/q7g1qRd/veinminingenchantment.png)

**Configuration Options**
- Rarity
- Max Level
- Treasure
- Available from Enchantment Table
- Available on Books
- Base Enchanting Power
- Enchanting Power per Level
- Incompatible Enchantments
- Villager Tradeable
- Lootable
- Compatible Items

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
- Activation Method
- Activation Method (without enchantment)
- Blocks Whitelist/Blacklist
- Custom Block Groups (Tags + IDs)

### Configuration Files

Configuration files are located in the Minecraft instance's root folder's `config` folder as `veinmining-client.toml`,
`veinmining-common.toml`, and `veinmining-server.toml`.

## Downloads

**CurseForge**
- [![](http://cf.way2muchnoise.eu/short_vein-mining_downloads%20on%20Forge.svg)](https://www.curseforge.com/minecraft/mc-mods/vein-mining/files) [![](http://cf.way2muchnoise.eu/versions/vein-mining.svg)](https://www.curseforge.com/minecraft/mc-mods/vein-mining)
- [![](http://cf.way2muchnoise.eu/short_vein-mining-fabric_downloads%20on%20Fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/vein-mining-fabric/files) [![](http://cf.way2muchnoise.eu/versions/vein-mining-fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/vein-mining-fabric)

## Developing

**Help! I'm getting Mixin crashes when I try to launch in development on Forge!**

Vein Mining uses Mixins to implement its core features. This may cause issues when depending on
Vein Mining for your project inside a development environment since ForgeGradle/MixinGradle do not yet
support this natively like on the Fabric toolchain. As a workaround, please disable the refmaps in
development by setting the `mixin.env.disableRefMap` JVM argument to `true` in your run
configuration.

## Support

Please report all bugs, issues, and feature requests to the
[issue tracker](https://github.com/illusivesoulworks/veinmining/issues).

For non-technical support and questions, join the developer's [Discord](https://discord.gg/JWgrdwt).

## License

All source code and assets are licensed under LGPL-3.0-or-later.

## Donations

Donations to the developer can be sent through [Ko-fi](https://ko-fi.com/C0C1NL4O).

## Affiliates

[![BisectHosting](https://i.ibb.co/1G4QPdc/bh-illusive.png)](https://bisecthosting.com/illusive)
