package pepjebs.ruined_equipment;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RuinedEquipmentMod implements ModInitializer {

    public static final String MOD_ID = "ruined_equipment";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final String RUINED_PREFIX = "ruined_";

    public static final Item RUINED_DIAMOND_PICK = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Map<Item, Item> VANILLA_ITEM_MAP = new HashMap<Item, Item>() {{
        put(RUINED_DIAMOND_PICK, Items.DIAMOND_PICKAXE);
        put(RUINED_GOLDEN_PICKAXE, Items.GOLDEN_PICKAXE);
        put(RUINED_IRON_PICKAXE, Items.IRON_PICKAXE);
        put(RUINED_NETHERITE_PICKAXE, Items.NETHERITE_PICKAXE);
        put(RUINED_STONE_PICKAXE, Items.STONE_PICKAXE);
        put(RUINED_WOODEN_PICKAXE, Items.WOODEN_PICKAXE);
    }};

    public static final Map<Item, String> ITEM_ID_MAP = new HashMap<Item, String>() {{
        put(RUINED_DIAMOND_PICK, "diamond_pickaxe");
        put(RUINED_GOLDEN_PICKAXE, "golden_pickaxe");
        put(RUINED_IRON_PICKAXE, "iron_pickaxe");
        put(RUINED_NETHERITE_PICKAXE, "netherite_pickaxe");
        put(RUINED_STONE_PICKAXE, "stone_pickaxe");
        put(RUINED_WOODEN_PICKAXE, "wooden_pickaxe");
    }};

    @Override
    public void onInitialize() {
        for (Map.Entry<Item, String> item : ITEM_ID_MAP.entrySet()) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID,
                    RUINED_PREFIX + item.getValue()), item.getKey());
        }
    }
}
