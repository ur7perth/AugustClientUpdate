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
        boolean hovered = isMouseOver(mouseX, mouseY);

        int trackOn = 0xFF8B7BFF;
        int trackOnEdge = 0xFF6C5CE7;
        int trackOff = 0xFF34343E;
        int trackOffEdge = 0xFF232329;

        context.fill(getX(), getY(), getX() + width, getY() + height, on ? trackOnEdge : trackOffEdge);
        context.fill(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, on ? trackOn : trackOff);
        if (hovered) {
            context.fill(getX(), getY(), getX() + width, getY() + height, 0x1AFFFFFF);
        }

        int knobSize = height - 4;
        int knobX = on ? getX() + width - height + 2 : getX() + 2;
        int knobY = getY() + 2;
        context.fill(knobX + 1, knobY + 1, knobX + knobSize + 1, knobY + knobSize + 1, 0x33000000);
        context.fill(knobX, knobY, knobX + knobSize, knobY + knobSize, 0xFFFFFFFF);
        context.fill(knobX + 1, knobY + 1, knobX + knobSize - 1, knobY + knobSize - 1,
                on ? 0xFFEDEBFF : 0xFFD8D8DE);
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
