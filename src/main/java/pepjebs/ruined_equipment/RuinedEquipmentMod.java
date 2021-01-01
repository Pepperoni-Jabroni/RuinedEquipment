package pepjebs.ruined_equipment;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;

public class RuinedEquipmentMod implements ModInitializer {

    public static final String MOD_ID = "ruined_equipment";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final Item RUINED_DIAMOND_PICK = new RuinedEquipmentItem(new Item.Settings().maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ruined_diamond_pickaxe"), RUINED_DIAMOND_PICK);
    }
}
