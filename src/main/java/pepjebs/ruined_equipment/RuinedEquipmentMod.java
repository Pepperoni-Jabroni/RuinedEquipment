package pepjebs.ruined_equipment;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.ruined_equipment.config.RuinedEquipmentConfig;
import pepjebs.ruined_equipment.item.RuinedDyeableEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentCraftRepair;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RuinedEquipmentMod implements ModInitializer {

    public static final String MOD_ID = "ruined_equipment";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final String RUINED_PREFIX = "ruined_";

    public static RuinedEquipmentConfig CONFIG = null;

    public static SpecialRecipeSerializer<RuinedEquipmentCraftRepair> RUINED_CRAFT_REPAIR_RECIPE;
    public static RuinedEquipmentSmithingEmpowerRecipe.Serializer RUINED_SMITH_SET_EMPOWER;

    public static final HashMap<String, Pair<Integer, ItemStack>> ruinedEquipmentSetter = new HashMap<>();

    @Override
    public void onInitialize() {
        AutoConfig.register(RuinedEquipmentConfig.class, JanksonConfigSerializer::new);

        RuinedEquipmentConfig config = AutoConfig.getConfigHolder(RuinedEquipmentConfig.class).getConfig();
        CONFIG = config;
        if (config.enableCraftingGridRuinedRepair) {
            RUINED_CRAFT_REPAIR_RECIPE = Registry.register(
                    Registry.RECIPE_SERIALIZER,
                    new Identifier(MOD_ID, "ruined_repair"),
                    new SpecialRecipeSerializer<>(RuinedEquipmentCraftRepair::new));
        }
        if (config.enableSmithingRuinedEmpowered) {
            RUINED_SMITH_SET_EMPOWER = Registry.register(
                    Registry.RECIPE_SERIALIZER,
                    new Identifier(MOD_ID, "ruined_set_empower"),
                    new RuinedEquipmentSmithingEmpowerRecipe.Serializer());
        }

        ItemGroup itemGroup = ItemGroup.MISC;
        if (config.enableCreativeInventoryTab) {
            itemGroup = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "ruined_items"))
                    .icon(() -> new ItemStack(Registry.ITEM.get(new Identifier(MOD_ID, "ruined_diamond_pickaxe"))))
                    .appendItems(stacks -> {
                        for (Item item : RuinedEquipmentItems.getVanillaItemMap().keySet().stream()
                                .sorted(RuinedEquipmentUtils::compareItemsById).collect(Collectors.toList())) {
                            stacks.add(new ItemStack(item));
                        }
                    })
                    .build();
        }

        Map<Item, Item> vanillaItemMap = new HashMap<>();
        Item.Settings set = new Item.Settings().maxCount(1).group(itemGroup);
        for (Item i : RuinedEquipmentItems.SUPPORTED_VANILLA_ITEMS) {
            if (Registry.ITEM.getId(i).getPath().contains("leather")) {
                vanillaItemMap.put(new RuinedDyeableEquipmentItem(set), i);
            } else {
                vanillaItemMap.put(new RuinedEquipmentItem(set), i);
            }
        }
        for (Map.Entry<Item, Item> item : vanillaItemMap.entrySet()) {
            String vanillaItemIdPath = Registry.ITEM.getId(item.getValue()).getPath();
            Registry.register(Registry.ITEM, new Identifier(MOD_ID,
                    RUINED_PREFIX + vanillaItemIdPath), item.getKey());
        }

        ServerTickEvents.START_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                String key = player.getName().getString();
                if (ruinedEquipmentSetter.containsKey(key)) {
                    RuinedEquipmentMod.LOGGER.info("ServerTickEvents.START_SERVER_TICK: " + key);
                    Pair<Integer, ItemStack> entry = ruinedEquipmentSetter.get(key);
                    int slot = entry.getLeft();
                    ItemStack ruinedItem = entry.getRight();
                    boolean didRemove = false;
                    if (slot == 0) {
                        if (player.inventory.offHand.get(slot).isEmpty()){
                            player.inventory.offHand.set(slot, ruinedItem);
                            didRemove = true;
                        }
                    } else {
                        slot--;
                        if (player.inventory.main.get(slot).isEmpty()) {
                            player.inventory.main.set(slot, ruinedItem);
                            didRemove = true;
                        }
                    }
                    if (didRemove) ruinedEquipmentSetter.remove(key);
                }
            }
        });
    }
}
