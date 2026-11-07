# 更新日志 / Changelog
## 2026-10-20
- 移植客户端配置：`Config` 由 MDK 模板（`logDirtBlock` / `magicNumber` / `items`）改写为真实配置，按 `ModConfig.Type.CLIENT` 注册，生成 `config/arknights_endfield-client.toml`（对应 Fab 版的 `ModClientConfig`，键名保持一致）
- 新增 `equipmentRenderDistance`（工业设备渲染距离，默认 `256`，范围 `0~1024`）：43 个已注册的设备渲染器（GeckoLib 设备、输电塔/供电桩的复合渲染器、流体泵/储液罐等）统一覆盖 `getViewDistance()` 读取该值，因此调低后可显著降低大型工厂场景的渲染开销
- 新增 `beltRenderLayer`（传送带渲染层级，默认 `TRANSLUCENT`，可改 `CUTOUT`）：客户端初始化时据此为 `ModBlocks.BELT` 注册 `RenderType.translucent()` / `RenderType.cutout()`；顺带补上了此前 Forge 侧缺失的传送带渲染层注册
- 传送带渲染层级在客户端启动时读取，修改后需重启游戏生效；渲染距离随配置重载即时生效
- Ported the client config: `Config` is no longer the MDK template (`logDirtBlock` / `magicNumber` / `items`) and is now a real spec registered as `ModConfig.Type.CLIENT`, writing `config/arknights_endfield-client.toml` (the Fabric counterpart of `ModClientConfig`, with identical key names)
- Added `equipmentRenderDistance` (render distance for industrial equipment, default `256`, range `0-1024`): all 43 registered equipment renderers (GeckoLib devices, the composite pylon/relay renderers, pumps and tanks) now override `getViewDistance()`, so lowering it noticeably cuts render cost in large factory areas
- Added `beltRenderLayer` (belt render layer, default `TRANSLUCENT`, optionally `CUTOUT`): at client setup `ModBlocks.BELT` is registered with `RenderType.translucent()` / `RenderType.cutout()`; this also fills in the belt render layer registration that the Forge port was missing
- The belt render layer is read at client startup and needs a game restart to change, while the render distance applies on config reload
- 配置项注释改为中英双语（`---- 中文 ----` / `---- English ----` 两段），方便非中文用户直接阅读 `toml`；枚举取值 `TRANSLUCENT` / `CUTOUT` 在注释中也有对应解释
- Config comments are now bilingual (`---- 中文 ----` / `---- English ----` blocks) so non-Chinese players can read the generated `toml` directly; both `TRANSLUCENT` and `CUTOUT` are explained in the comment
- 移植`晶簇`（Druse）方块家族：新增 `AmethystDruseBlock`、`FerriumDruseBlock`、`OriginiumDruseBlock` 三个类，随机刻有 50% 概率在正上方一格生成对应晶簇（上方须为空气）
- 三块矿脉（`amethyst_/ferrium_/originium_mineral_vein_block`）由普通 `Block` 换成对应的 Druse 类，因此矿脉现在会自己「长」出晶簇
- `ModBlocks` 补齐缺失的 `AMETHYST_DRUSE` / `FERRIUM_DRUSE` / `ORIGINIUM_DRUSE` 三个注册（`strength(2f, 2f).noOcclusion()`，自带方块物品），并按 Fab 顺序放进创造栏矿物分区末尾
- 资源侧（blockstate、方块/物品模型、战利品表、贴图、中英语言）此前已随资源迁移到位，本次只补 Java 代码
- Ported the `Druse` block family: added `AmethystDruseBlock`, `FerriumDruseBlock` and `OriginiumDruseBlock`, each with a 50% chance per random tick to grow the matching druse one block above (requires air)
- The three mineral veins (`amethyst_`/`ferrium_`/`originium_mineral_vein_block`) now use those classes instead of plain `Block`, so veins grow their druses on their own
- `ModBlocks` gained the three missing registrations `AMETHYST_DRUSE` / `FERRIUM_DRUSE` / `ORIGINIUM_DRUSE` (`strength(2f, 2f).noOcclusion()`, block items included), listed at the end of the ore section of the creative tab in Fabric order
- Assets (blockstates, block/item models, loot tables, textures, EN/ZH lang) had already been carried over with the resource migration, so this pass only added the Java side
- 移植`废水处理机`（Water Treatment Unit）：三格结构（主机 + 正上方两格高的不可见侧方块），接入电网，工作时每 tick 消耗 50 EFU，每 40 tick 完成一个周期并销毁槽内至多 1,000 mB 流体
- 废水槽容量 50,000 mB，**接受任意流体**（无配方、无物品槽、无产物），任意面都能由管道注入；流体交互走 `ForgeCapabilities.FLUID_HANDLER`
- GUI 为 16×16 流体槽 + 进度箭头 + 右侧储能条 + 启停开关，流体按类型取色并带水量/储能提示，容器数据 8 项
- 放置时检查正上方两格是否可替换（对应 Fab 的 `canPlaceAt` 空间检查）；`SwitchPacket` 增加该机器分支以支持 GUI 开关
- Ported the `Water Treatment Unit`: 3-block structure (main block plus an invisible side block two blocks directly above), grid-powered, costing 50 EFU per tick and destroying up to 1,000 mB from its tank at the end of each 40-tick cycle
- The waste tank holds 50,000 mB and **accepts any fluid** (no recipe, no item slots, no output); any side can be filled by pipes through `ForgeCapabilities.FLUID_HANDLER`
- The GUI shows a 16x16 fluid gauge, a progress arrow, the stored-power bar and an on/off toggle, with per-fluid tinting plus fluid and power tooltips; 8 container data slots
- Placement now checks that the two blocks above are replaceable (matching Fabric's `canPlaceAt` space check), and `SwitchPacket` gained a branch for this machine so the GUI toggle works
- 移植`水驱矿机`（Hydro Mining Rig）：三格结构（主机 + 正面两格高的不可见侧方块），tier 4，可开采全部等级矿脉
- 与电驱矿机共用 `BaseRigBlockEntity` 骨架但不接电网：`getPowerCostPerTick()` 恒为 0，改为**消耗水**——水箱 50,000 mB、只接受水（`FluidTank` 校验器拦住其它流体），每次开采固定扣 1,000 mB，与矿脉种类无关
- 独立 tick：不走基类那条「电网判定 + 向传送带推物品」的路径，按配方匹配（含水量）推进进度，产物留在内部槽定期提交到全局仓储
- 水箱以 `ForgeCapabilities.FLUID_HANDLER` 全向暴露（对应 Fab 的 `getFluidStorage(side)`），侧方块也转发；管道/水泵可直接灌水
- GUI 继承 `BaseRigScreen`（进度箭头 + 启停开关），额外在左侧留白画竖向水罐 + 水量提示；输出槽只出不进
- Ported the `Hydro Mining Rig`: 3-block structure (main block plus an invisible side block two blocks up in front), tier 4, able to mine every vein tier
- It shares `BaseRigBlockEntity` with the electric rig but is **not** on the power grid: `getPowerCostPerTick()` is 0 and it consumes water instead - a 50,000 mB tank that only accepts water (enforced by the `FluidTank` validator), draining a flat 1,000 mB per craft regardless of the vein
- Custom tick: skips the base class's "grid check + push to belt" path, advances progress when the recipe matches (water included) and periodically submits output to the global storage
- The water tank is exposed on all sides through `ForgeCapabilities.FLUID_HANDLER` (matching Fabric's `getFluidStorage(side)`), and the side block forwards it, so pipes and pumps can fill it directly
- The GUI extends `BaseRigScreen` (progress arrow + on/off toggle) and additionally draws a vertical water tank with a tooltip; the output slot is take-only
- 移植`二型耐酸水泵`（Acid-Resistant Pump Mk.II）：与一型水泵同构的三格结构（主机 + 正面两格高的不可见侧方块），接入电网，单次抽水耗电 400 EFU（一型的 2 倍）
- 二型与一型的关键差异是**不做酸性过滤**：凡是归入 `minecraft:water` 标签的流体都抽，因此沉积酸也能采集；一型仍会拒绝酸性流体
- 两台水泵共用同一个取液网络包（新增 `BucketFillable` 接口做服务端分发），GUI 用空桶取液时按罐内流体给出对应的桶物品
- 二型的 GUI 复用一型水泵的贴图与布局（流体按类型取色 + 储能条 + 流体/储能提示），只换了菜单类型与方块实体
- 补齐注册与资源：方块 / 方块实体 / 物品（GeckoLib）/ 菜单 / 方块实体渲染器 / 创造栏，以及缺失的侧方块模型（blockstate、战利品表、配方与手册条目此前已就位）
- Ported the `Acid-Resistant Pump Mk.II`: same 3-block structure as the Mk.I (main block plus an invisible side block two blocks up in front), grid-powered, but each pumping action costs 400 EFU (twice the Mk.I)
- The key difference from the Mk.I is that the Mk.II does **no acid filtering** - anything in the `minecraft:water` tag gets pumped, so precipitation acid can be collected, while the Mk.I still refuses acidic fluids
- Both pumps now share one bucket-filling packet (a new `BucketFillable` interface dispatches on the server); filling a bucket in the GUI hands out the bucket matching the stored fluid
- The Mk.II GUI reuses the Mk.I texture and layout (per-fluid tinting, stored-power bar and tooltips) and only differs in menu type and block entity
- Added all registrations and assets: block, block entity, GeckoLib item, menu, block entity renderer, creative tab entry, and the missing side-block model (blockstate, loot table, recipe and guidebook entries were already in place)
- `水泵`与`储液罐`按 Fab 版重做为三格结构：主机 + 不可见侧方块（水泵 1 个、储液罐 2 个），放置时自动生成、破坏主机时一并清理；侧方块可右键开主机 GUI、拾取给回本体
- `水泵`接入电网：30 格内需要供电桩/中继器，或自身还有缓冲电量；每 20 tick 从「顺朝向第 2 格的下方」抽 1000 mB 水并消耗 200 EFU，酸性流体直接拒绝
- `水泵`输出罐只在朝向面暴露为 `ForgeCapabilities.FLUID_HANDLER`（与 Fab 的 `getFluidStorage(side)` 一致），管道从正面取水
- 新增 `PumpFluidUtil`：水泵/储罐 GUI 中用空桶取液时，按流体类型给出对应的桶物品（水/岩浆 + 7 种模组流体）
- `水泵` GUI 补上按流体取色的流体层、储能条与储能提示，容器数据扩到 4 项（存量/容量/储能/流体 ID）；`储液罐` GUI 也改为按流体取色
- 两个方块的渲染包围盒放大（模型约 4 格高），避免贴边观察时整块模型被剔除；方块属性对齐 Fab（0.5 硬度）
- `Fluid Tank` and `Fluid Pump` are now 3-block structures like the Fabric version: a main block plus invisible side blocks (1 for the pump, 2 for the tank) that are created on placement, removed with the main block, forward their GUI on right-click, and give back the main item when pick-blocked
- The `Fluid Pump` is back on the power grid: it needs a pylon/relay within 30 blocks (or buffered power) and pumps 1000 mB of water every 20 ticks from the block two steps behind and one below, costing 200 EFU; acidic fluids are rejected
- The pump's output tank is exposed through `ForgeCapabilities.FLUID_HANDLER` on its facing side only, matching Fabric's `getFluidStorage(side)`
- Added `PumpFluidUtil` so the pump/tank GUIs hand out the matching bucket when filling one (water, lava and the 7 modded fluids)
- The pump GUI gained per-fluid tinting, a stored-power bar with tooltip and 4 container data slots; the tank GUI now tints by fluid type too
- Both machines got an enlarged render bounding box (their models are ~4 blocks tall) and now use Fabric's 0.5 hardness
- `流体管道`改为 Fab 版的`传送带变种`形态：每格管道只承载 1 管流体（2,000 mB），沿管道形状逐格传递，不再有 MEK 式的内胆缓冲罐
- 管道自身以 `ForgeCapabilities.FLUID_HANDLER` 暴露这一管流体：只有形状两端的连接面可`fill`（且在空管时），只有与入口相对的`出口端`可`drain`
- `管道桥`、`管道分流器`、`管道汇流器`沿用「本身不 tick，由管道推进时直接调用」的传送带式逻辑；修正水泵向管道注入时 capability 的 `side` 传反的问题
- 管道方块属性对齐 Fab（0.5/5 硬度、无碰撞），并补上 `cutout` 渲染层；手册中管道的容量与工作原理描述同步更新
- The `Fluid Pipe` now uses the Fabric version's `Belt-variant` form: each pipe block carries a single slug of fluid (2,000 mB) and moves it one block at a time along the pipe shape - the MEK-style inner buffer tank is gone
- The pipe itself exposes that single slug through `ForgeCapabilities.FLUID_HANDLER`: only the two connected ends can `fill` (and only while empty), and only the end opposite the inlet can `drain`
- `Pipe Bridge`, `Pipe Splitter` and `Pipe Converger` keep the belt-like logic (no ticker of their own, driven by the pipe that pushes into them); fixed the inverted `side` the Fluid Pump passed when injecting into a pipe
- Pipe block properties now match Fabric (0.5/5 hardness, no collision) and the `cutout` render layer was added; the guidebook entry now documents the new capacity and behaviour
- 同步 Fab 版`精炼炉`改动：新增`流体模式`开关（GUI 按钮 + 网络包 `fluid_mode_change`），开启后左右两侧生成流体接口
- 流体接口改为左右两面通用：任意一侧都能灌入原料罐（仅流体模式开启时）或抽出污水罐，仍以 `ForgeCapabilities.FLUID_HANDLER` 实现
- GUI 新增罐体外框、按流体类型取色、储能条与储能提示；关闭流体模式时清空原料与污水罐
- Synced `Refining Unit` changes from the Fabric version: added the `Fluid Mode` toggle (GUI button + `fluid_mode_change` packet) which spawns fluid ports on both sides
- The fluid ports are now side-agnostic: either side can fill the material tank (only while fluid mode is on) or drain the sewage tank, still implemented with `ForgeCapabilities.FLUID_HANDLER`
- GUI now draws the tank frames, per-fluid tinting, the stored power bar and its tooltip; switching fluid mode off clears both tanks
- 修复`精炼炉`破坏后流体模式生成的两个流体端口方块残留的问题
- 同步 Fab 版`种植机`与`灌装机`：两者都在原有基础上新增`流体模式`（原料罐仅可注入、左右两侧生成流体端口、GUI 按钮 + 网络包），种植机按模式过滤"纯物品配方"与"种子+流体配方"
- `灌装机`流体模式下下方物品输入槽转为流体输入槽（槽内流体直接渲染），切换模式时槽内物品返还玩家；加工时间随配方类型在 200t / 40t 间切换
- Fixed leftover fluid-port blocks after breaking a `Refining Unit` while fluid mode was on
- Synced `Planting Unit` and `Filling Unit` from the Fabric version: both gained `Fluid Mode` (insert-only material tank, fluid ports on both sides, GUI button + packet); the Planting Unit now filters "item only" vs "seed + fluid" recipes by mode
- On the `Filling Unit`, fluid mode converts the lower item input slot into a fluid input slot (the fluid is rendered right inside the slot) and returns its contents to the player when toggling; processing time switches between 200t and 40t depending on the recipe
- 全局仓储（Global Storage）同步 Fab 版重做：存储键从「物品」改为「物品 + NBT 变体」，带 NBT 的物品不再与普通物品混在一起，也不会在存取时丢掉 NBT
- `StorageEntry` 增加 `tag` 字段与 `normalizeTag` / `matches` / `isAnyOf` / `toStack(int)` / 拷贝构造；`StorageState` 由 `Map<Item, StorageEntry>` 改为有序 `List<StorageEntry>`，落盘新增 `tag` 键（旧存档无 `tag` 时按普通物品正常读取）
- 入库先按变体量容量再扣物品（`getFreeSpace`），容量满时不再吞掉玩家背包里的物品；`DepositItemPacket` 只扣与点击格同一 NBT 变体的栈
- 出库新增 `extract(ItemStack prototype, long)`，按精确变体取出并还原 NBT；`SyncStoragePacket` / `OpenStoragePacket` / `RequestItemPacket` / `DepositItemPacket` 全部带上 `CompoundTag`
- 仓储 GUI 用带 NBT 的栈刷新槽位与提示（自定义名称、附魔可正常显示），条目变少时自动把滚动位置拉回合法区间，避免 `subList` 越界
- `/storage deposit` 改为逐栈按各自 NBT 变体插入（容量满不吞物品），`/storage withdraw` 支持 `item{nbt}` 语法精确取物；两条命令改用缓存的 `GlobalStorageManager`，执行后已打开的仓储界面会即时刷新
- Synced the Global Storage rework from the Fabric version: entries are now keyed by "item + NBT variant" instead of just item, so NBT items no longer mix with plain ones and never lose their NBT when stored or retrieved
- `StorageEntry` gained a `tag` field plus `normalizeTag` / `matches` / `isAnyOf` / `toStack(int)` and a copy constructor; `StorageState` switched from `Map<Item, StorageEntry>` to an ordered `List<StorageEntry>` and persists a `tag` key (old saves without it still load as plain items)
- Inserting now measures the variant's free space first (`getFreeSpace`) and only then takes the items, so a full variant no longer swallows stacks; `DepositItemPacket` only removes stacks whose NBT matches the clicked slot
- Extraction gained `extract(ItemStack prototype, long)`, which matches the exact variant and restores its NBT; `SyncStoragePacket` / `OpenStoragePacket` / `RequestItemPacket` / `DepositItemPacket` all carry a `CompoundTag` now
- The storage GUI refreshes slots and tooltips with the NBT-carrying stacks (custom names and enchantments show up correctly), and clamps the scroll offset when entries shrink so `subList` cannot go out of bounds
- `/storage deposit` now inserts stack by stack using each stack's own NBT variant (a full store no longer eats items), `/storage withdraw` accepts `item{nbt}` for exact retrieval, and both commands use the cached `GlobalStorageManager` so already-open storage screens refresh immediately

## 2025-12-08 -> 2026-03-06
- 一系列更新，参见Fab版的更新日志
- A series of updates, see the changelog for the Fabric version

## 2025-11-23
- 优化供电桩逻辑，现在供电桩只会在放置时，只进行一次搜索，搜索半径30格范围内的`协议核心`或`中继器`，如果找到，则会把顶端的连接线渲染出来
- Optimized the logic of Electric Pylon. Now, the Electric Pylon will only perform a one-time search upon placement, searching for `Protocol Anchor Core` or `Relay Tower` within a 30-block radius. If found, it will render the top connection line.
- 进一步优化`协议核心`的性能，移除了方块本身的tick方法，全部交给全局电网管理系统处理
- Further optimized the performance of `Protocol Anchor Core` by removing the block's own tick method and delegating all processing to the Global Power Grid Management System.

## 2025-11-11
- 测试验证`FE转换单元`
- Tested and verified `FE Converter`

## 2025-11-09
- 修正配方问题
- Fixed recipe issues

## 2025-11-02
- 调整了矿物生成的概率
- Adjusted the probability of mineral generation
- 牛、绵羊、猪可以掉落`兽肉`，骷髅和凋零骷髅可以掉落`异香石块`等系列方块
- Cows, sheep, and pigs can drop `Fillet`, while skeletons and wither skeletons can drop `Aggagrit Blocks` and related blocks

## 2025-11-01
- 优化`协议核心端口`，可以实现物品筛选
- Optimized `Protocol Anchor Core Port` to support item filtering
- 增加食物
- Added food items
- `工业爆炸物`可以投掷，落地爆炸
- `Industrial Explosives` can be thrown and will explode upon landing
- 添加了一些配方
- Added some recipes

## 2025-10-29
- 修正各设备的渲染问题，以确保在安装`Embeddium`等优化模组时不会出现渲染异常
- Fixed rendering issues of various devices to ensure no rendering anomalies occur when optimization mods like `Embeddium` are installed
- 调整`供电桩`供电逻辑
- Adjusted the power supply logic of `Electric Pylon`

## 2025-10-28
- 优化`协议核心`tick方法，提高游戏性能
- Optimized the tick method of `Protocol Anchor Core` to improve game performance
- 添加`协议核心端口`，用于输入物品
- Added `Protocol Anchor Core Port` for item input

## 2025-10-27
- 为`协议核心`添加中央仓库，用于存储物品
- Added a central warehouse to `Protocol Anchor Core` for item storage

## 2025-10-25 -> 2025-10-26
- 添加`成就`
- Added `Achievements`

## 2025-10-24
- 修复`制造台`刷物品的特性（bug）
- Fixed the item duplication bug in `Crafter`

## 2025-10-18
- 更新`协议核心`GUI界面
- Updated `Protocol Anchor Core` GUI interface

## 2025-10-17
- `制造台`现在关闭GUI时，会自动返回输入槽中未使用的物品
- When closing the GUI of `Crafter`, unused items in the input slots will be automatically returned

## 2025-10-16
- 添加`全局电网管理系统`，统一调配全世界范围内的电力
- Added `Global Power Grid Management System` to centrally manage power distribution worldwide

## 2025-10-15
- 临时追加`晶体外壳`的熔炉和高炉配方，以保证游戏正常推进
- Temporarily added furnace and blast furnace recipes for `Origocrust` to ensure normal game progression
- 追加`热能池`配方
- Added recipe for `Thermal Bank`

## 2025-10-12 -> 2025-10-13
- `装备原件机`、`研磨机`、`灌装机`、`配件机`、`塑形机`、`封装机`、`种植机`、`采种机`、`粉碎机`移植完成
- Ported `Gearing Unit`, `Grinding Unit`, `Filling Unit`, `Fitting Unit`, `Moulding Unit`, `Packaging Unit`, `Planting Unit`, `Seed Picking Unit`, and `Shredding Unit`
- 完成`JEI`适配
- Completed `JEI` integration
- 调整各个工业设备的生产时间
- Adjusted production times for various industrial devices

## 2025-10-11
- `中继器`、`供电桩`、`电驱矿机`、`二型电驱矿机`、`精炼炉`移植
- Ported `Relay Tower`, `Electric Pylon`, `Electric Mining Rig`, `Electric Mining Rig Mk II`, and `Refining Unit`
- 完成对其他工业模组的兼容，如`机械动力`、`通用机械`等
- Completed compatibility with other industrial mods, such as `Create`, `Mekanism`, etc.

## 2025-10-10
- 修正`制造台`合成时扣除物品数量异常的问题
- Fixed the issue of abnormal item quantity deduction when crafting with `Crafter`
- `便携源石矿机`、`协议核心`、`热能池`移植完成
- `Portable Originium Rig`, `Protocol Anchor Core`, and `Thermal Bank` porting completed

## 2025-10-08
- 移植`制造台`
- Ported `Crafter`

## 2025-10-07
- `Forge`版本开始移植
- The `Forge` version begins to be ported
- 更新世界生成
- Updated world generation