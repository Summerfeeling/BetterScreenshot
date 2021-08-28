package dev.summerfeeling.betterscreenshot.utils;

import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.Module;
import net.labymod.settings.elements.SliderElement;
import net.labymod.utils.Consumer;

public class DescribableSliderElement extends SliderElement {

    public DescribableSliderElement(String displayName, String configEntryName, IconData iconData) {
        super(displayName, configEntryName, iconData);
    }

    public DescribableSliderElement(String displayName, IconData iconData, int currentValue) {
        super(displayName, iconData, currentValue);
    }

    public DescribableSliderElement(String displayName, LabyModAddon addon, IconData iconData, String attribute, int currentValue) {
        super(displayName, addon, iconData, attribute, currentValue);
    }

    public DescribableSliderElement(Module module, IconData iconData, String displayName, String attribute) {
        super(module, iconData, displayName, attribute);
    }

    public DescribableSliderElement editDescription(String descriptionText) {
        super.setDescriptionText(descriptionText);
        return this;
    }

    public DescribableSliderElement setRange(int minValue, int maxValue) {
        super.setRange(minValue, maxValue);
        return this;
    }

    public DescribableSliderElement setSteps(int steps) {
        super.setSteps(steps);
        return this;
    }

    public DescribableSliderElement addCallback(Consumer<Integer> callback) {
        super.addCallback(callback);
        return this;
    }

}
