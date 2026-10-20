package io.github.kimovoid.tweakimo.mixin;

import io.github.kimovoid.tweakimo.config.TweakimoConfig;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ChatMessageC2SPacket.class)
public class ChatMessageC2SPacketMixin {

    @ModifyConstant(method = "<init>(Ljava/lang/String;)V", constant = @Constant(intValue = 100))
    private static int extendWriteMessage(int constant) {
        return TweakimoConfig.longerChatMessages ? 256 : constant;
    }

    @ModifyConstant(method = "read", constant = @Constant(intValue = 100))
    private static int extendReadMessage(int constant) {
        return TweakimoConfig.longerChatMessages ? 256 : constant;
    }
}
