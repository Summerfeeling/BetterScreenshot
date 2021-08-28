package dev.summerfeeling.betterscreenshot.listener;

import dev.summerfeeling.betterscreenshot.BetterScreenshot;
import dev.summerfeeling.betterscreenshot.publisher.Publisher;
import dev.summerfeeling.betterscreenshot.publisher.Result;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.chat.MessageSendEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

/**
 * Created: 01.05.2021
 *
 * @author Summerfeeling
 */
public class MessageSendListener {

    @Subscribe
    public void onMessageSend(MessageSendEvent event) {
        if (event.getMessage().startsWith("-uploadscreenshot") || event.getMessage().startsWith("-deletescreenshot")) {
            event.setCancelled(true);

            try {
                String message = event.getMessage();

                String filePath = message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\""));
                File file = new File(filePath);

                if (!file.exists()) {
                    Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("Error: ").mergeStyle(TextFormatting.RED).append(new StringTextComponent("The given file does not exists!").mergeStyle(TextFormatting.WHITE)));
                    return;
                }

                if (!file.canRead()) {
                    Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("Error: ").mergeStyle(TextFormatting.RED).append(new StringTextComponent("The given file is not accessible!").mergeStyle(TextFormatting.WHITE)));
                    return;
                }

                if (message.startsWith("-uploadscreenshot")) {
                    Publisher.publishScreenshot(file, (result, detail) -> {
                        if (result == Result.UPLOAD_DONE) {
                            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§aUpload finished: " + detail).modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.OPEN_URL, detail))));

                            if (BetterScreenshot.getInstance().isCopyToClipboard()) {
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(detail), new StringSelection(detail));
                            }
                        } else {
                            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§cUpload failed: §f" + detail));
                        }
                    });
                } else if (message.startsWith("-deletescreenshot")) {
                    if (file.delete()) {
                        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("Screenshot deleted!").mergeStyle(TextFormatting.GREEN));
                    } else {
                        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("Failed to delete screenshot").mergeStyle(TextFormatting.RED));
                    }
                }
            } catch (Exception e) {
                String keyName = KeyBinding.getDisplayString("key.screenshot").get().getString();
                Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§cSomething went wrong! Did you execute the upload command yourself? (You must not, just hit " + keyName + "!)"));
                e.printStackTrace();
            }
        }
    }

}
