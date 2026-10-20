package io.github.kimovoid.tweakimo.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.kimovoid.tweakimo.config.TweakimoConfig;
import net.minecraft.client.render.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow private float fogRed;
    @Shadow private float fogGreen;
    @Shadow private float fogBlue;

    @ModifyExpressionValue(
            method = "render(IFJ)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/options/GameOptions;viewDistance:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int alwaysRenderSky(int original) {
        return TweakimoConfig.alwaysRenderSky ? Math.max(4, original) : original;
    }

    @Inject(method = "setupFog", at = @At("HEAD"))
    private void setFogColor(int mode, float tickDelta, CallbackInfo ci) {
        if (!TweakimoConfig.fogColorToggle) return;
        this.fogRed = TweakimoConfig.fogColor.getRedF();
        this.fogGreen = TweakimoConfig.fogColor.getGreenF();
        this.fogBlue = TweakimoConfig.fogColor.getBlueF();
    }

    @ModifyConstant(
            method = "setupFog",
            constant = @Constant(floatValue = 0.75F),
            remap = false
    )
    private float setFogStart(float constant) {
        return TweakimoConfig.fogStart - 0.05F;
    }
}
