package dev.summerfeeling.betterscreenshot.utils;

import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.Style;

/**
 * Created: 02.05.2021
 *
 * @author Summerfeeling
 */
public class ToStringProcessor {

    private final StringBuilder builder = new StringBuilder();

    public ToStringProcessor(IReorderingProcessor processor) {
        processor.accept(this::accept);
    }

    private boolean accept(int index, Style style, int asciiChar) {
        this.builder.append((char) asciiChar);
        return true;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

}
