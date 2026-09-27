package com.nametweaks.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ToggleSwitchWidget extends ClickableWidget {
    private final BooleanSupplier getter;
    private final Consumer<Boolean> setter;

    public ToggleSwitchWidget(int x, int y, BooleanSupplier getter, Consumer<Boolean> setter) {
        super(x, y, 34, 18, Text.empty());
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean on = getter.getAsBoolean();
        int bg = on ? 0xFF7C6CF0 : 0xFF3A3A46;
        context.fill(getX(), getY(), getX() + width, getY() + height, bg);
        int knobX = on ? getX() + width - height : getX();
        context.fill(knobX + 2, getY() + 2, knobX + height - 2, getY() + height - 2, 0xFFFFFFFF);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setter.accept(!getter.getAsBoolean());
        MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
