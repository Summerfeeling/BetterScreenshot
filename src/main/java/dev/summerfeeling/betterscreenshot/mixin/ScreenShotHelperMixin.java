package dev.summerfeeling.betterscreenshot.mixin;

import dev.summerfeeling.betterscreenshot.BetterScreenshot;
import dev.summerfeeling.betterscreenshot.publisher.Publisher;
import dev.summerfeeling.betterscreenshot.publisher.Result;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenShotHelper.class)
public abstract class ScreenShotHelperMixin {

    @Shadow private static File getTimestampedPNGFileForDirectory(File gameDirectory) { return null; }

    @Shadow @Final private static Logger LOGGER;

    /**
     * @author Summerfeeling
     */
    @Overwrite
    private static void saveScreenshotRaw(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer, Consumer<ITextComponent> messageConsumer) {
        NativeImage nativeImage = ScreenShotHelper.createScreenshot(width, height, buffer);

        File screenshotsDirectory = new File(gameDirectory, "screenshots");
        screenshotsDirectory.mkdir();

        File screenshotFile = getScreenshotFile(screenshotName, screenshotsDirectory);

        Util.getRenderingService().execute(() -> {
            try {
                nativeImage.write(screenshotFile);

                if (!BetterScreenshot.getInstance().isEnabled()) {
                    ITextComponent textComponent = new StringTextComponent(screenshotFile.getName()).mergeStyle(TextFormatting.UNDERLINE).modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.OPEN_FILE, screenshotFile.getAbsolutePath())));
                    messageConsumer.accept(textComponent);
                    return;
                }

                // Build message

                IFormattableTextComponent firstComponent = new StringTextComponent("Saved screenshot as ")
                    .mergeStyle(TextFormatting.GOLD)
                    .append(new StringTextComponent(screenshotFile.getName())
                        .mergeStyle(TextFormatting.YELLOW)
                        .modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Open screenshot"))))
                        .modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.OPEN_FILE, screenshotFile.getAbsolutePath())))
                    );

                IFormattableTextComponent secondComponent = new StringTextComponent("\n» ").mergeStyle(TextFormatting.GRAY);

                if (BetterScreenshot.getInstance().isUploadEnabled() && !BetterScreenshot.getInstance().isDirectUpload()) {
                    secondComponent.append(new StringTextComponent("[UPLOAD] ")
                        .mergeStyle(TextFormatting.YELLOW)
                        .modifyStyle((style) -> style.setBold(true))
                        .modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Upload to imgur"))))
                        .modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "-uploadscreenshot \"" + screenshotFile.getAbsolutePath() + "\"")))
                    );
                }

                if (BetterScreenshot.getInstance().isDeleteEnabled()) {
                    secondComponent.append(new StringTextComponent("[DELETE]")
                        .mergeStyle(TextFormatting.RED)
                        .modifyStyle((style) -> style.setBold(true))
                        .modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Delete screenshot"))))
                        .modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "-deletescreenshot \"" + screenshotFile.getAbsolutePath() + "\"")))
                    );
                }

                if (BetterScreenshot.getInstance().isUploadEnabled() || BetterScreenshot.getInstance().isDeleteEnabled()) {
                    firstComponent.append(secondComponent);
                }

                // Direct upload
                if (BetterScreenshot.getInstance().isDirectUpload()) {
                    Publisher.publishScreenshot(screenshotFile, (result, detail) -> {
                        if (result == Result.UPLOAD_DONE) {
                            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§aUpload finished: " + detail).modifyStyle((style) -> style.setClickEvent(new ClickEvent(Action.OPEN_URL, detail))));

                            if (BetterScreenshot.getInstance().isCopyToClipboard()) {
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(detail), new StringSelection(detail));
                            }
                        } else {
                            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§cUpload failed: §f" + detail));
                        }
                    });
                }

                messageConsumer.accept(firstComponent);
            } catch (Exception e) {
                LOGGER.warn("Couldn't save screenshot", e);
                messageConsumer.accept(new TranslationTextComponent("screenshot.failure", e.getMessage()));
            } finally {
                nativeImage.close();
            }
        });
    }

    private static File getScreenshotFile(String screenshotName, File screenshotsDirectory) {
        if (screenshotName == null) {
            return getTimestampedPNGFileForDirectory(screenshotsDirectory);
        } else {
            return new File(screenshotsDirectory, screenshotName);
        }
    }

}
