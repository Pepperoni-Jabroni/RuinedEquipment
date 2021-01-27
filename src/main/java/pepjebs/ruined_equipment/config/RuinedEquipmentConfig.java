package pepjebs.ruined_equipment.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
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
}
