package pepjebs.ruined_equipment;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
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

        ItemGroup test = null;
        if (config.enableCreativeInventoryTab) {
            test = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "ruined_items"))
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
        Item.Settings set = new Item.Settings().maxCount(1).group(test != null ? test : ItemGroup.MISC);
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
    }
}
