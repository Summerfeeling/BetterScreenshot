package dev.summerfeeling.betterscreenshot.utils;

import net.labymod.gui.elements.DropDownMenu;
import net.labymod.gui.elements.DropDownMenu.DropDownEntryDrawer;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.utils.Consumer;

@SuppressWarnings("Duplicates")
public class BetterDropDownElement<E> extends DropDownElement<E> {

    private static final int TEXT_HEIGHT = LabyMod.getInstance().getDrawUtils().getFontRenderer().FONT_HEIGHT;

    private final long startDescription = -1L;

    public BetterDropDownElement(String diplayName, DropDownMenu dropDownMenu) {
        super(diplayName, dropDownMenu);
    }

    public BetterDropDownElement<E> editDescriptionText(String descriptionText) {
        super.setDescriptionText(descriptionText);
        return this;
    }

    public BetterDropDownElement<E> setIconData(IconData iconData) {
        this.iconData = iconData;
        return this;
    }

    public BetterDropDownElement<E> select(E select) {
        super.getDropDownMenu().setSelected(select);
        return this;
    }

    public BetterDropDownElement<E> changeListener(Consumer<E> consumer) {
        super.setChangeListener(consumer);
        return this;
    }

    public BetterDropDownElement<E> entryDrawer(DropDownEntryDrawer drawer) {
        super.getDropDownMenu().setEntryDrawer(drawer);
        return this;
    }

}
