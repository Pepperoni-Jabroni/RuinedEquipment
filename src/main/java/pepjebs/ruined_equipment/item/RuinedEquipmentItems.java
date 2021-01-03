package pepjebs.ruined_equipment.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static final Item RUINED_FLINT_AND_STEEL = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_CROSSBOW = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_SHIELD = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_HELMET = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_HELMET = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_HELMET = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_HELMET = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_CHAINMAIL_HELMET = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_LEATHER_HELMET = new RuinedDyeableEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_TURTLE_HELMET = new RuinedDyeableEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_CHESTPLATE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_CHESTPLATE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_CHESTPLATE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_CHESTPLATE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_CHAINMAIL_CHESTPLATE = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_LEATHER_CHESTPLATE = new RuinedDyeableEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_LEGGINGS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_LEGGINGS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_LEGGINGS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_LEGGINGS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_CHAINMAIL_LEGGINGS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_LEATHER_LEGGINGS = new RuinedDyeableEquipmentItem(new Item.Settings().maxCount(1));

    public static final Item RUINED_DIAMOND_BOOTS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_GOLDEN_BOOTS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_IRON_BOOTS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_NETHERITE_BOOTS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_CHAINMAIL_BOOTS = new RuinedEquipmentItem(new Item.Settings().maxCount(1));
    public static final Item RUINED_LEATHER_BOOTS = new RuinedDyeableEquipmentItem(new Item.Settings().maxCount(1));

    public static final Map<Item, Item> VANILLA_STD_ITEM_MAP = new HashMap<Item, Item>() {{
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
        put(RUINED_FLINT_AND_STEEL, Items.FLINT_AND_STEEL);
        put(RUINED_CROSSBOW, Items.CROSSBOW);
        put(RUINED_SHIELD, Items.SHIELD);

        put(RUINED_DIAMOND_HELMET, Items.DIAMOND_HELMET);
        put(RUINED_GOLDEN_HELMET, Items.GOLDEN_HELMET);
        put(RUINED_IRON_HELMET, Items.IRON_HELMET);
        put(RUINED_NETHERITE_HELMET, Items.NETHERITE_HELMET);
        put(RUINED_CHAINMAIL_HELMET, Items.CHAINMAIL_HELMET);
        put(RUINED_TURTLE_HELMET, Items.TURTLE_HELMET);

        put(RUINED_DIAMOND_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
        put(RUINED_GOLDEN_CHESTPLATE, Items.GOLDEN_CHESTPLATE);
        put(RUINED_IRON_CHESTPLATE, Items.IRON_CHESTPLATE);
        put(RUINED_NETHERITE_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
        put(RUINED_CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE);

        put(RUINED_DIAMOND_LEGGINGS, Items.DIAMOND_LEGGINGS);
        put(RUINED_GOLDEN_LEGGINGS, Items.GOLDEN_LEGGINGS);
        put(RUINED_IRON_LEGGINGS, Items.IRON_LEGGINGS);
        put(RUINED_NETHERITE_LEGGINGS, Items.NETHERITE_LEGGINGS);
        put(RUINED_CHAINMAIL_LEGGINGS, Items.CHAINMAIL_LEGGINGS);

        put(RUINED_DIAMOND_BOOTS, Items.DIAMOND_BOOTS);
        put(RUINED_GOLDEN_BOOTS, Items.GOLDEN_BOOTS);
        put(RUINED_IRON_BOOTS, Items.IRON_BOOTS);
        put(RUINED_NETHERITE_BOOTS, Items.NETHERITE_BOOTS);
        put(RUINED_CHAINMAIL_BOOTS, Items.CHAINMAIL_BOOTS);
    }};

    public static final Map<Item, Item> VANILLA_DYEABLE_ITEM_MAP = new HashMap<Item, Item>() {{
        put(RUINED_LEATHER_HELMET, Items.LEATHER_HELMET);
        put(RUINED_LEATHER_CHESTPLATE, Items.LEATHER_CHESTPLATE);
        put(RUINED_LEATHER_LEGGINGS, Items.LEATHER_LEGGINGS);
        put(RUINED_LEATHER_BOOTS, Items.LEATHER_BOOTS);
    }};

    public static final Map<Item, Item> VANILLA_ITEM_MAP =
            Stream.concat(
                    VANILLA_STD_ITEM_MAP.entrySet().stream(),
                    VANILLA_DYEABLE_ITEM_MAP.entrySet().stream()
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
