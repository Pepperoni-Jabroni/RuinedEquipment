package pepjebs.ruined_equipment;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.ruined_equipment.config.RuinedEquipmentConfig;
import pepjebs.ruined_equipment.item.RuinedAshesItem;
import pepjebs.ruined_equipment.item.RuinedDyeableEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentCraftRepair;

import java.util.HashMap;
import java.util.Map;

public class RuinedEquipmentMod implements ModInitializer {

    public static final String MOD_ID = "ruined_equipment";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final String RUINED_MAX_ENCHT_TAG = "IsUpgrading";
    public static final String RUINED_PREFIX = "ruined_";
    public static RuinedAshesItem RUINED_ASHES_ITEM = null;

    public static RuinedEquipmentConfig CONFIG = null;

    public static SpecialRecipeSerializer<RuinedEquipmentCraftRepair> RUINED_CRAFT_REPAIR_RECIPE;

    public static final HashMap<String, Pair<Integer, ItemStack>> ruinedEquipmentSetter = new HashMap<>();

    @Override
    public void onInitialize() {
        if(FabricLoader.getInstance().isModLoaded("cloth-config")) {
            AutoConfig.register(RuinedEquipmentConfig.class, JanksonConfigSerializer::new);
            CONFIG = AutoConfig.getConfigHolder(RuinedEquipmentConfig.class).getConfig();
        }

        RUINED_CRAFT_REPAIR_RECIPE = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier(MOD_ID, "ruined_repair"),
                new SpecialRecipeSerializer<>(RuinedEquipmentCraftRepair::new));

        Map<Item, Item> vanillaItemMap = new HashMap<>();
        Item.Settings set = new Item.Settings().maxCount(1);
        for (Item i : RuinedEquipmentItems.SUPPORTED_VANILLA_ITEMS) {
            if (Registries.ITEM.getId(i).getPath().contains("leather")) {
                vanillaItemMap.put(new RuinedDyeableEquipmentItem(set), i);
            } else {
                vanillaItemMap.put(new RuinedEquipmentItem(set), i);
            }
        }
        for (Map.Entry<Item, Item> item : vanillaItemMap.entrySet()) {
            String vanillaItemIdPath = Registries.ITEM.getId(item.getValue()).getPath();
            Registry.register(Registries.ITEM, new Identifier(MOD_ID,
                    RUINED_PREFIX + vanillaItemIdPath), item.getKey());
        }
        RUINED_ASHES_ITEM = Registry.register(Registries.ITEM, new Identifier(MOD_ID,
                "ruined_item_ashes"), new RuinedAshesItem(new Item.Settings().maxCount(1)));

        ServerTickEvents.START_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                String key = player.getName().getString();
                if (ruinedEquipmentSetter.containsKey(key)) {
                    Pair<Integer, ItemStack> entry = ruinedEquipmentSetter.get(key);
                    int slot = entry.getLeft();
                    ItemStack ruinedItem = entry.getRight();
                    boolean didRemove = false;
                    if (slot == 0) {
                        if (player.getInventory().offHand.get(slot).isEmpty()){
                            player.getInventory().offHand.set(slot, ruinedItem);
                            didRemove = true;
                        }
                    } else {
                        slot--;
                        if (player.getInventory().main.get(slot).isEmpty()) {
                            player.getInventory().main.set(slot, ruinedItem);
                            didRemove = true;
                        }
                    }
                    if (didRemove) ruinedEquipmentSetter.remove(key);
                }
            }
        });
    }
}
