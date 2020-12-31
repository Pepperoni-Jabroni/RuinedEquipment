package pepjebs.ruined_equipment.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;

@Mixin(ClientPlayNetworkHandler.class)
public class RuinedEquipmentMixin {

    @Shadow private MinecraftClient client;

    @Inject(method = "onEntityStatus", at = @At("RETURN"))
    private void onOnEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getEntity(client.world).getEntityId() == client.player.getEntityId()) {
            // LivingEntity.getEquipmentBreakStatus.MAINHAND
            if (packet.getStatus() == 47) {
                if (client.player.getMainHandStack().getDamage() == 1) {
                    RuinedEquipmentMod.LOGGER.info("Broke a tool!");
                }
            }
        }
    }
}
