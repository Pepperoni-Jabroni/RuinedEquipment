package pepjebs.ruined_equipment.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.DyeableItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;

public class RuinedEquipmentClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> {
            if (tintIndex == 0) {
                return ((DyeableItem)stack.getItem()).getColor(stack);
            } else {
                return -1;
            }
        }), RuinedEquipmentItems.RUINED_LEATHER_HELMET);
    }
}
