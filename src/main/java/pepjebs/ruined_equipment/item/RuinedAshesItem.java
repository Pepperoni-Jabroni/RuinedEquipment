package pepjebs.ruined_equipment.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.List;

public class RuinedAshesItem extends Item {
    public RuinedAshesItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Identifier id = RuinedEquipmentUtils.getItemKeyIdFromItemStack(stack);
        if (id == null) return;
        Item item = Registries.ITEM .get(id);
        if (stack.hasCustomName()) {
            tooltip.add(Text.translatable(this.getTranslationKey()).formatted(Formatting.GRAY));
        }
        tooltip.add(Text.translatable(item.getTranslationKey(stack)).formatted(Formatting.GRAY));
        RuinedEquipmentItem.appendRuinedTooltip(stack, tooltip);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return RuinedEquipmentItem.hasRuinedGlint(stack);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return RuinedEquipmentItem.getRuinedRarity(stack);
    }
}
