package io.github.kimovoid.tweakimo.mixin;

import io.github.kimovoid.tweakimo.config.TweakimoConfig;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At("TAIL"))
    private void extendChat(CallbackInfo ci) {
        if (!TweakimoConfig.longerChatMessages) return;
        this.chatField.setMaxLength(256);
    }
}
