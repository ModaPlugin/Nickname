# Nickname
 The Nickname module allows users to set their own, or other players' nickname(s).  
 Nicknames are available as a Moda Placeholder (`{NICKNAME}`) for use in other modules.

## Commands
 | command                           | description                                                                                                                                                       | aliases                          | permission                        |
 | :-------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------- | :------------------------------- | :-------------------------------- |
 | `/nickname`                       | opens a GUI to set a nickname.                                                                                                                                    | `/nick`                          | `moda.module.nickname.set`        |
 | `/nickname <nickname>`            | sets your own nickname to `<nickname>`.                                                                                                                           | `/nick <nickname>`               | `moda.module.nickname.set`        |
 | `/nickname <player>`              | opens a GUI to set `<player>`'s nickname.                                                                                                                         | `/nick <player>`                 | `moda.module.nickname.set.others` |
 | `/nickname <player> <nickname>`   | sets `<player>`'s nickname to `<nickname>`.                                                                                                                       | `/nick <player> <nickname>`      | `moda.module.nickname.set.others` |
 | `/whois <username|nickname> [id]` | finds and displays info about the player(s) that are using `<username>` or `<nickname>`, <br> use `[id]` to specify which player if more than 1 player is listed. | `/find <username|nickname> [id]` | `moda.module.nickname.find`       |

## Permissions
 | permission                          | description                                                             |
 | :---------------------------------- | :---------------------------------------------------------------------- |
 | `moda.module.nickname.set`          | Allows a player to set their nickname.                                  |
 | `moda.module.nickname.set.color`    | Allows a player to use colors in their nickname.                        |
 | `moda.module.nickname.set.format`   | Allows a player to use formatting in their nickname.                    |
 | `moda.module.nickname.set.others`   | Allows a player to set another player's nickname.                       |
 | `moda.module.nickname.set.taken`    | Allows a player to set a nickname that's already set by another player. |
 | `moda.module.nickname.find`         | Allows a player to find out what username(s) belong(s) to a nickname.   |
 | `moda.module.nickname.reset`        | Allows a player to reset/remove their nickname.                         |
 | `moda.module.nickname.reset.others` | Allows a player to reset/remove another player's nickname.              |

---
## Recommended Modules
- **[ChatFormat](https://github.com/ModaPlugin/ChatFormat "lets admins make the chat look fancy!")**
- **ChatColor (coming soon)**
  - lets players use colors and formatting to spice up their chat.