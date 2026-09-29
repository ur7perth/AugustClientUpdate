package com.nametweaks.mixin;

import com.nametweaks.NameTweaksClient;
import com.nametweaks.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void nametweaks$hideSelfInF5(AbstractClientPlayerEntity player, Text text, MatrixStack matrices,
                                          VertexConsumerProvider vertexConsumers, int light, float tickDelta,
                                          CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig cfg = NameTweaksClient.CONFIG;
        boolean isSelf = player == client.player;
        boolean thirdPerson = !client.options.getPerspective().isFirstPerson();
        if (isSelf && thirdPerson && !cfg.nameInF5) {
            ci.cancel();
        }
    }

    @Inject(method = "renderLabelIfPresent", at = @At("TAIL"))
    private void nametweaks$renderPing(AbstractClientPlayerEntity player, Text text, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, int light, float tickDelta,
                                        CallbackInfo ci) {
        ModConfig cfg = NameTweaksClient.CONFIG;
        if (!cfg.nameTagPingEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) return;

        boolean isSelf = player == client.player;
        boolean thirdPerson = !client.options.getPerspective().isFirstPerson();
        if (isSelf && thirdPerson && !cfg.nameInF5) return;

        var entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (entry == null) return;
        int ping = entry.getLatency();

        int color;
        if (!cfg.pingColorEnabled) {
            color = 0xAAAAAA;
        } else if (ping < 80) {
            color = 0x55FF55;
        } else if (ping <= 180) {
            color = 0xFFAA00;
        } else {
            color = 0xFF5555;
        }

        Text pingText = Text.literal(ping + "ms").styled(s -> s.withColor(color));
        TextRenderer tr = client.textRenderer;
        float nameHeight = player.getHeight() + 0.5F - (player.isInSneakingPose() ? 0.25F : 0.0F);

        matrices.push();
        matrices.translate(0.0, cfg.pingAboveName ? nameHeight + 0.30 : nameHeight, 0.0);
        matrices.multiply(client.getEntityRenderDispatcher().getRotation());
        matrices.scale(0.025F, -0.025F, 0.025F);

        Matrix4f mat = matrices.peek().getPositionMatrix();
        float x;
        if (cfg.pingAboveName) {
            x = -tr.getWidth(pingText) / 2F + (float) cfg.pingOffset;
        } else {
            x = tr.getWidth(text) / 2F + 4F;
        }

        var immediate = client.getBufferBuilders().getEntityVertexConsumers();
        tr.draw(pingText, x, 0, -1, false, mat, immediate, TextRenderer.TextLayerType.NORMAL, 0, light);
        immediate.draw();
        matrices.pop();
    }
}
