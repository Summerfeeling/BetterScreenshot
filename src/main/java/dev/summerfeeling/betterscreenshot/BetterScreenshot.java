package dev.summerfeeling.betterscreenshot;

import dev.summerfeeling.betterscreenshot.listener.MessageSendListener;
import dev.summerfeeling.betterscreenshot.utils.BetterDropDownElement;
import dev.summerfeeling.betterscreenshot.utils.DescribableBooleanElement;
import dev.summerfeeling.betterscreenshot.utils.DescribableHeaderElement;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.List;

public class BetterScreenshot extends LabyModAddon {

    private static BetterScreenshot instance;

    private Resolution resolution = Resolution.ORIGINAL;
    private boolean enabled = true;

    private boolean copyToClipboard = false;
    private boolean directUpload = false;
    private boolean uploadEnabled = true;

    private boolean deleteEnabled = true;

    @Override
    public void onEnable() {
        BetterScreenshot.instance = this;

        this.getApi().getEventService().registerListener(new MessageSendListener());
    }

    @Override
    public void loadConfig() {
        this.enabled = !getConfig().has("enabled") || getConfig().get("enabled").getAsBoolean();
        this.resolution = (!getConfig().has("resolution") || getConfig().get("resolution").isJsonNull() ? Resolution.ORIGINAL : Resolution.valueOf(getConfig().get("resolution").getAsString()));

        this.copyToClipboard = getConfig().has("copyToClipboard") && getConfig().get("copyToClipboard").getAsBoolean();
        this.uploadEnabled = !getConfig().has("uploadEnabled") || getConfig().get("uploadEnabled").getAsBoolean();
        this.directUpload = getConfig().has("directUpload") && getConfig().get("directUpload").getAsBoolean();

        this.deleteEnabled = !getConfig().has("deleteEnabled") || getConfig().get("deleteEnabled").getAsBoolean();
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.clear();

        list.add(new HeaderElement("§6BetterScreenshot"));
        list.add(new DescribableBooleanElement("Enabled", this, new IconData(Material.LEVER), "enabled", true).editDescriptionText("Whether this addon should be enabled or not"));

        list.add(new DescribableHeaderElement("Image Settings"));
        list.add(new BetterDropDownElement<Resolution>("Scale", new DropDownMenu<Resolution>("", 0, 0, 0, 0).fill(Resolution.values())).select(resolution).changeListener(resolution -> {
            getConfig().addProperty("resolution", resolution.name());
            loadConfig();
        }).entryDrawer((object, matrixStack, x, y, trimmed) -> {
            Resolution resolution = (Resolution) object;

            if (resolution == Resolution.ORIGINAL) {
                LabyMod.getInstance().getDrawUtils().drawString(matrixStack, "Keep resolution", x, y);
            } else {
                LabyMod.getInstance().getDrawUtils().drawString(matrixStack, resolution.getWidth() + " x " + resolution.getHeight(), x, y);
            }
        }).editDescriptionText("Should the uploaded picture be scaled? (Useful to lower file size and therefore upload time, especially for displays over Full HD)").setIconData(new IconData("labymod/textures/settings/default/gui_scaling.png")));

        list.add(new DescribableHeaderElement("§eimgur.com Upload").editDescriptionText("You can upload your screenshot with one click - or automatically - to imgur.com"));
        list.add(new DescribableBooleanElement("[UPLOAD] Enabled", this, new IconData(Material.LEVER), "uploadEnabled", true).editDescriptionText("Shows / hides the [UPLOAD] button"));
        list.add(new DescribableBooleanElement("Automatic copy to clipboard", this, new IconData(Material.BOOK_AND_QUILL), "copyToClipboard", false).editDescriptionText("Should the imgur.com link be automatically copied to your clipboard?"));
        list.add(new DescribableBooleanElement("Automatic upload", this, new IconData(Material.FIREWORK_ROCKET), "directUpload", false).editDescriptionText("Should every screenshot be uploaded automatically?"));

        list.add(new DescribableHeaderElement("§cDelete Function").editDescriptionText("You can delete accidentally created screenshots with one click directly in Minecraft"));
        list.add(new DescribableBooleanElement("[DELETE] Enabled", this, new IconData(Material.LEVER), "deleteEnabled", true).editDescriptionText("Shows / hides the [DELETE] button"));

        list.add(new HeaderElement(" "));
        list.add(new HeaderElement("§oUpload limited to 500 pictures a day due to imgur's rate limit."));
        list.add(new DescribableHeaderElement("§oDeveloper information").editDescriptionText("If you want to contact me: \nTwitter: @official_s_f\nDiscord: Summerfeeling#3310"));
    }

    public static BetterScreenshot getInstance() {
        return instance;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCopyToClipboard() {
        return copyToClipboard;
    }

    public boolean isDirectUpload() {
        return directUpload;
    }

    public boolean isUploadEnabled() {
        return uploadEnabled;
    }

    public boolean isDeleteEnabled() {
        return deleteEnabled;
    }

}
