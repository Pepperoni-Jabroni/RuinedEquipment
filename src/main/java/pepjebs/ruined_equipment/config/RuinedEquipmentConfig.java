package pepjebs.ruined_equipment.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import pepjebs.ruined_equipment.RuinedEquipmentMod;

@Config(name = RuinedEquipmentMod.MOD_ID)
public class RuinedEquipmentConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', Ruined items will be set in-hand when they break instead of going into the inventory.")
    public boolean enableSetRuinedItemInHand = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', repairing Ruined items using the Crafting Grid will be enabled.")
    public boolean enableCraftingGridRuinedRepair = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', repairing Ruined items using the Anvil will be enabled.")
    public boolean enableAnvilRuinedRepair = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', 'Empowering' a Ruined item using the Smithing Table will be enabled.")
    public boolean enableSmithingRuinedEmpowered = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("The Item to be used for applying 'Empowered' to a Ruined item in a Smithing Table.")
    public String empowermentSmithingItem = "minecraft:netherite_scrap";

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', the 'Ruined Equipment' Creative inventory tab will be displayed.")
    public boolean enableCreativeInventoryTab = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', Ruined items with Mending held in your hands will be repaired when you collect XP.")
    public boolean enableRuinedMendingRepair = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("When 'true', non-vanilla breaking items will generate a 'Ruined Items Ashes'.")
    public boolean enableRuinedItemsAshesGeneration = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Codes for which repair items to use for which Ruined Items Ashes (e.g. 'copper_mod:copper_sword/minecraft:copper_ingot;')")
    public String ruinedItemsAshesRepairItems = null;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Codes for modded Items NOT to generate Ruined Items Ashes for when they break (e.g. 'copper_mod:copper_sword;')")
    public String blocklistForRuinedAshesItems = null;
}
