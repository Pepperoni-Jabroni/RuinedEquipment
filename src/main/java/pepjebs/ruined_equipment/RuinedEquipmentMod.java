package pepjebs.ruined_equipment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
    }};

    public static final ItemGroup RUINED_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "ruined_items"))
            .icon(() -> new ItemStack(RUINED_DIAMOND_PICK))
            .appendItems(stacks -> {
                for (Map.Entry<Item, Item> item : VANILLA_ITEM_MAP.entrySet()) {
                    stacks.add(new ItemStack(item.getKey()));
                }
            })
            .build();

    @Override
    public void onInitialize() {
        for (Map.Entry<Item, Item> item : VANILLA_ITEM_MAP.entrySet()) {
            String vanillaItemIdPath = Registry.ITEM.getId(item.getValue()).getPath();
            Registry.register(Registry.ITEM, new Identifier(MOD_ID,
                    RUINED_PREFIX + vanillaItemIdPath), item.getKey());
        }
    }
}
