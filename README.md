# Nickname

 The Nickname module allows users to set their own, or other players' nickname(s).  
 Nicknames are available as a Moda Placeholder (`{NICKNAME}`) for use in other modules.

## Commands

 | command                         | description                                                      | aliases                     | permission                        |
 | :------------------------------ | :--------------------------------------------------------------- | :-------------------------- | :-------------------------------- |
 | `/nickname`                     | Displays your nickname and information & usage about `/nickname` | `/nick`                     | `moda.module.nickname.set`        |
 | `/nickname <nickname>`          | sets your own nickname to `<nickname>`.                          | `/nick <nickname>`          | `moda.module.nickname.set`        |
 | `/nickname <player>`            | opens a GUI to set `<player>`'s nickname.                        | `/nick <player>`            | `moda.module.nickname.set.others` |
 | `/nickname <player> <nickname>` | sets `<player>`'s nickname to `<nickname>`.                      | `/nick <player> <nickname>` | `moda.module.nickname.set.others` |

## Permissions

 | permission                        | description                                                             |
 | :-------------------------------- | :---------------------------------------------------------------------- |
 | `moda.module.nickname.set`        | Allows a player to set their nickname.                                  |
 | `moda.module.nickname.set.color`  | Allows a player to use colors in their nickname.                        |
 | `moda.module.nickname.set.format` | Allows a player to use formatting in their nickname.                    |
 | `moda.module.nickname.set.others` | Allows a player to set another player's nickname.                       |
 | `moda.module.nickname.set.taken`  | Allows a player to set a nickname that's already set by another player. |

---

## Recommended Modules

- **[ChatFormat](https://github.com/ModaPlugin/ChatFormat "lets admins make the chat look fancy!")**
- **[ChatColor](https://github.com/orgs/ModaPlugin/projects/2#card-35374379 "lets players use colors and formatting to spice up their chat.")** (coming soon)
- **[PlayerInfo](https://github.com/orgs/ModaPlugin/projects/2#card-35437336 "stores and is able to display information about a player's whereabouts, login date etc.")** (coming soon)
