package com.nametweaks.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class OffsetSliderWidget extends SliderWidget {
    private final DoubleConsumer setter;
    private final String label;

    public OffsetSliderWidget(int x, int y, int width, int height, String label,
                               DoubleSupplier getter, DoubleConsumer setter) {
        super(x, y, width, height, Text.empty(), (getter.getAsDouble() + 30.0) / 30.0);
        this.label = label;
        this.setter = setter;
        updateMessage();
    }

    private double toValue() {
        return -30.0 + this.value * 30.0;
    }

    @Override
    protected void updateMessage() {
        setMessage(Text.literal(label + ": " + Math.round(toValue())));
    }

    @Override
    protected void applyValue() {
        setter.accept(toValue());
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = isMouseOver(mouseX, mouseY);

        context.fill(getX(), getY(), getX() + width, getY() + height, 0xFF232329);
        context.fill(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, 0xFF303039);

        int fillWidth = Math.max(2, (int) (this.value * (width - 2)));
        context.fill(getX() + 1, getY() + 1, getX() + 1 + fillWidth, getY() + height - 1,
                hovered ? 0xFF8B7BFF : 0xFF6C5CE7);

        int handleWidth = 6;
        int handleX = getX() + (int) (this.value * (width - handleWidth));
        context.fill(handleX, getY() - 1, handleX + handleWidth, getY() + height + 1, 0xFF14141B);
        context.fill(handleX + 1, getY(), handleX + handleWidth - 1, getY() + height, 0xFFFFFFFF);

        var tr = MinecraftClient.getInstance().textRenderer;
        int textX = getX() + width / 2 - tr.getWidth(getMessage()) / 2;
        int textY = getY() + (height - 8) / 2;
        context.drawText(tr, getMessage(), textX + 1, textY + 1, 0x80000000, false);
        context.drawText(tr, getMessage(), textX, textY, 0xFFFFFFFF, false);
    }
}
