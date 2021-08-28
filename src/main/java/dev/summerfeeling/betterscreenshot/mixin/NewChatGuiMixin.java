package dev.summerfeeling.betterscreenshot.mixin;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

/**
 * Created: 02.05.2021
 *
 * @author Summerfeeling
 */
@Mixin(NewChatGui.class)
public interface NewChatGuiMixin {

    @Accessor List<ChatLine<ITextComponent>> getChatLines();
    @Accessor List<String> getSentMessages();
    @Invoker void callDeleteChatLine(int id);

}
