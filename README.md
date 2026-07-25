# EMC Command Extension v0.3.0

## Introduction
**EMC Command Extension** is a Minecraft mod that adds a comprehensive favorites system to manage lists of towns, nations, and players. It supports special favorites like **hate** (for players – displays names in red and warns when within 100m) and **show_border** (for towns – renders town borders). All favorites can be viewed in interactive lists, and commands allow you to create, edit, and use them easily.

## Parameters

### Type Parameters `-t` `-n` `-p`
Specify the favorite list type (required where a type is expected).
- `-t` – Town
- `-n` – Nation
- `-p` – Player

### Query Parameters `-i` `-N`
Specify how to identify a favorite list (default `-N`).
- `-i` – By ID
- `-N` – By name

### Search Parameters `-t` `-n` `-p`
Specify the type of object to search for when adding/removing (default matches the type of the favorite list being used).
- If operating on a town favorite, default search is `-t` (towns).
- Nation favorite → default `-n` (nations).
- Player favorite → default `-p` (players).

## Commands

### `/favorites <type> [subcommand...]`
Without a subcommand, lists all favorites of the given type. The displayed entries can be clicked for interaction.

#### Subcommands:

- **create `<name> [id]`**  
  Creates a new favorite list of the specified type with the given name. If an ID is provided, it is used; otherwise one is assigned automatically.  
  *Examples:*  
  `/favorites -t create MyTowns`  
  `/favorites -p create "VIP Players" 42`

- **delete `[query] <favorite>`**  
  Deletes a favorite list.  
  `<favorite>` is the name or ID (depending on the query flag).  
  *Examples:*  
  `/favorites -p delete -i 3`  
  `/favorites -n delete OldList`

- **add `[query] <favorite> [search] <object>`**  
  Adds an object to a favorite list.  
  `[query]` – how to find the favorite (default `-N` by name).  
  `[search]` – type of object to add (default matches the favorite type).  
  `<object>` – name or ID of the object.  
  *Examples:*  
  `/favorites -p add Friends Steve`  
  `/favorites -t add -i 5 -i 120`  
  `/favorites -n add -N Guild -n "Red Empire"`

- **remove `[query] <favorite> [search] <object>`**  
  Removes an object from a favorite list. Same logic as `add`.  
  *Examples:*  
  `/favorites -p remove Friends Steve`  
  `/favorites -t remove -i 5 -i 120`

- **show `[query] <favorite>`**  
  Displays the contents of a favorite list.  
  *Example:*  
  `/favorites -n show Enemies`

- **clear `[query] <favorite>`**  
  Removes all entries from a favorite list.  
  *Example:*  
  `/favorites -p clear Suspicious`

### `/page <pages>`
Used to navigate multi-page displays for any command that supports pagination. `<pages>` is the page number to jump to.

### `/resx`
Player-related shortcuts (operates on player favorites `-p`).

- **hate `[search] <object>`**  
  Adds a player to the **hate** list. If no search parameter, defaults to player by name.  
  *Example:* `/resx hate Steve`

- **unhate `[search] <object>`**  
  Removes a player from the **hate** list.  
  *Example:* `/resx unhate Steve`

- **favorites**  
  Same as `/favorites -p`. All subcommands (`create`, `delete`, `add`, etc.) are available.

- **set `<option> <value>`**  
  Sets a configuration option for the player module (equivalent to `/options set`).

### `/tx`
Town-related shortcuts (operates on town favorites `-t`).

- **border show `[search] <object>`**  
  Adds a town to the **show_border** list, making its borders visible.  
  *Examples:* `/tx border show MyTown`  
  `/tx border show -t -i 42`

- **border hide `[search] <object>`**  
  Removes a town from the **show_border** list.

- **favorites**  
  Same as `/favorites -t`.

- **set `<option> <value>`**  
  Sets a configuration option for the town module.

### `/nx`
Nation-related shortcuts (operates on nation favorites `-n`).

- **favorites**  
  Same as `/favorites -n`.

- **set `<option> <value>`**  
  Sets a configuration option for the nation module.

### `/options`
Global configuration management.

- **set `<option> <value>`**  
  Sets a configuration option to the specified value.

- **reset `<option>`**  
  Resets the specified option to its default. Use `all` to reset every option.

- **reload**  
  Reloads options from `config.json` and resets any invalid options.

## Special Lists

- **hate** (player type)  
  Players in this list will have their names displayed in red, and a warning will appear when they are within 100 meters.

- **show_border** (town type)  
  Towns in this list will have their borders rendered on the client.

---

# EMC Command Extension v0.3.0

## 简介
**EMC Command Extension** 是一款 Minecraft 模组，添加了完善的收藏夹系统，用于管理城镇、国家和玩家列表。支持特殊收藏夹，例如 **hate**（玩家——红色名称，100米内警告）和 **show_border**（城镇——显示边界）。所有收藏夹可在交互式列表中查看，通过命令可以方便地创建、编辑和使用它们。

## 参数

### 类型参数 `-t` `-n` `-p`
指定收藏夹类型（需要类型参数时必填）。
- `-t` – 城镇
- `-n` – 国家
- `-p` – 玩家

### 查询参数 `-i` `-N`
指定如何定位一个收藏夹（默认 `-N`）。
- `-i` – 按ID
- `-N` – 按名称

### 搜索参数 `-t` `-n` `-p`
指定添加/移除对象时搜索的对象类型（默认与所操作收藏夹的类型相同）。
- 操作城镇收藏夹时默认搜索 `-t`（城镇）。
- 国家收藏夹 → 默认 `-n`（国家）。
- 玩家收藏夹 → 默认 `-p`（玩家）。

## 命令

### `/favorites <类型> [子命令...]`
不接子命令时，列出给定类型的所有收藏夹，列表中的条目可点击互动。

#### 子命令：

- **create `<名称> [ID]`**  
  创建一个指定类型的新收藏夹。若提供ID则使用，否则自动分配。  
  *示例：*  
  `/favorites -t create 我的城镇`  
  `/favorites -p create "重要玩家" 42`

- **delete `[查询] <收藏夹>`**  
  删除一个收藏夹。  
  `<收藏夹>` 为名称或ID（取决于查询参数）。  
  *示例：*  
  `/favorites -p delete -i 3`  
  `/favorites -n delete 旧列表`

- **add `[查询] <收藏夹> [搜索] <对象>`**  
  向收藏夹添加一个对象。  
  `[查询]` – 查找收藏夹的方式（默认 `-N` 按名称）。  
  `[搜索]` – 对象类型（默认与收藏夹类型相同）。  
  `<对象>` – 对象的名称或ID。  
  *示例：*  
  `/favorites -p add 好友 Steve`  
  `/favorites -t add -i 5 -i 120`  
  `/favorites -n add -N 公会 -n "红色帝国"`

- **remove `[查询] <收藏夹> [搜索] <对象>`**  
  从收藏夹移除一个对象。参数逻辑同 `add`。  
  *示例：*  
  `/favorites -p remove 好友 Steve`  
  `/favorites -t remove -i 5 -i 120`

- **show `[查询] <收藏夹>`**  
  显示一个收藏夹的内容。  
  *示例：*  
  `/favorites -n show 敌对`

- **clear `[查询] <收藏夹>`**  
  清空收藏夹中的所有条目。  
  *示例：*  
  `/favorites -p clear 可疑人物`

### `/page <页码>`
用于有分页显示的命令，翻到指定页码。

### `/resx`
玩家相关的快捷命令（基于玩家收藏夹 `-p`）。

- **hate `[搜索] <对象>`**  
  将玩家添加到 **hate** 列表。无搜索参数时默认按名称搜索玩家。  
  *示例：* `/resx hate Steve`

- **unhate `[搜索] <对象>`**  
  将玩家从 **hate** 列表移除。  
  *示例：* `/resx unhate Steve`

- **favorites**  
  等同于 `/favorites -p`，可使用所有子命令。

- **set `<选项> <值>`**  
  设置玩家模块的配置项（同 `/options set`）。

### `/tx`
城镇相关的快捷命令（基于城镇收藏夹 `-t`）。

- **border show `[搜索] <对象>`**  
  将城镇添加到 **show_border** 列表，使其边界可见。  
  *示例：* `/tx border show 我的小镇`  
  `/tx border show -t -i 42`

- **border hide `[搜索] <对象>`**  
  将城镇从 **show_border** 列表移除。

- **favorites**  
  等同于 `/favorites -t`。

- **set `<选项> <值>`**  
  设置城镇模块的配置项。

### `/nx`
国家相关的快捷命令（基于国家收藏夹 `-n`）。

- **favorites**  
  等同于 `/favorites -n`。

- **set `<选项> <值>`**  
  设置国家模块的配置项。

### `/options`
全局配置管理。

- **set `<选项> <值>`**  
  设置指定配置项的值。

- **reset `<选项>`**  
  重置指定选项为默认值，使用 `all` 可重置所有选项。

- **reload**  
  从 `config.json` 重新加载配置，并重置任何非法选项。

## 特殊列表

- **hate**（玩家类型）  
  位于此列表中的玩家名称显示为红色，当其进入100米范围内时会发出警告。

- **show_border**（城镇类型）  
  位于此列表中的城镇将显示其边界。