package pepjebs.ruined_equipment.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class RuinedEquipmentItems {

    public static final Item RUINED_DIAMOND_PICK = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_PICKAXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_SWORD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_SHOVEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_AXE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_STONE_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_WOODEN_HOE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_BOW = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_FISHING_ROD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_SHEARS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_TRIDENT = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Map<Item, Item> VANILLA_ITEM_MAP = new HashMap<Item, Item>() {{
        put(RUINED_DIAMOND_PICK, Items.DIAMOND_PICKAXE);
        put(RUINED_GOLDEN_PICKAXE, Items.GOLDEN_PICKAXE);
        put(RUINED_IRON_PICKAXE, Items.IRON_PICKAXE);
        put(RUINED_NETHERITE_PICKAXE, Items.NETHERITE_PICKAXE);
        put(RUINED_STONE_PICKAXE, Items.STONE_PICKAXE);
        put(RUINED_WOODEN_PICKAXE, Items.WOODEN_PICKAXE);

        put(RUINED_DIAMOND_SWORD, Items.DIAMOND_SWORD);
        put(RUINED_GOLDEN_SWORD, Items.GOLDEN_SWORD);
        put(RUINED_IRON_SWORD, Items.IRON_SWORD);
        put(RUINED_NETHERITE_SWORD, Items.NETHERITE_SWORD);
        put(RUINED_STONE_SWORD, Items.STONE_SWORD);
        put(RUINED_WOODEN_SWORD, Items.WOODEN_SWORD);

        put(RUINED_DIAMOND_SHOVEL, Items.DIAMOND_SHOVEL);
        put(RUINED_GOLDEN_SHOVEL, Items.GOLDEN_SHOVEL);
        put(RUINED_IRON_SHOVEL, Items.IRON_SHOVEL);
        put(RUINED_NETHERITE_SHOVEL, Items.NETHERITE_SHOVEL);
        put(RUINED_STONE_SHOVEL, Items.STONE_SHOVEL);
        put(RUINED_WOODEN_SHOVEL, Items.WOODEN_SHOVEL);

        put(RUINED_DIAMOND_AXE, Items.DIAMOND_AXE);
        put(RUINED_GOLDEN_AXE, Items.GOLDEN_AXE);
        put(RUINED_IRON_AXE, Items.IRON_AXE);
        put(RUINED_NETHERITE_AXE, Items.NETHERITE_AXE);
        put(RUINED_STONE_AXE, Items.STONE_AXE);
        put(RUINED_WOODEN_AXE, Items.WOODEN_AXE);

        put(RUINED_DIAMOND_HOE, Items.DIAMOND_HOE);
        put(RUINED_GOLDEN_HOE, Items.GOLDEN_HOE);
        put(RUINED_IRON_HOE, Items.IRON_HOE);
        put(RUINED_NETHERITE_HOE, Items.NETHERITE_HOE);
        put(RUINED_STONE_HOE, Items.STONE_HOE);
        put(RUINED_WOODEN_HOE, Items.WOODEN_HOE);

        put(RUINED_BOW, Items.BOW);
        put(RUINED_FISHING_ROD, Items.FISHING_ROD);
        put(RUINED_SHEARS, Items.SHEARS);
        put(RUINED_TRIDENT, Items.TRIDENT);
    }};
}
