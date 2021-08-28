package dev.summerfeeling.betterscreenshot.utils;

import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.Module;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.utils.Consumer;

public class DescribableBooleanElement extends BooleanElement {

    public DescribableBooleanElement(String displayName, String configEntryName, IconData iconData) {
        super(displayName, configEntryName, iconData);
    }

    public DescribableBooleanElement(String displayName, Server server, IconData iconData, String attributeName) {
        super(displayName, server, iconData, attributeName);
    }

    public DescribableBooleanElement(String displayName, LabyModAddon addon, IconData iconData, String attributeName, boolean defaultValue) {
        super(displayName, addon, iconData, attributeName, defaultValue);
    }

    public DescribableBooleanElement(String displayName, IconData iconData, Consumer<Boolean> toggleListener, boolean currentValue) {
        super(displayName, iconData, toggleListener, currentValue);
    }

    public DescribableBooleanElement(Module module, IconData iconData, String displayName, String attribute) {
        super(module, iconData, displayName, attribute);
    }

    public BooleanElement editDescriptionText(String descriptionText) {
        super.setDescriptionText(descriptionText);
        return this;
    }
}
