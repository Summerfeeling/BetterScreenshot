package dev.summerfeeling.betterscreenshot;

import net.labymod.addon.AddonTransformer;
import net.labymod.api.TransformerType;

public class BetterScreenshotTransformer extends AddonTransformer {

    @Override
    public void registerTransformers() {
        this.registerTransformer(TransformerType.VANILLA, "betterscreenshot.mixin.json");
    }

}
