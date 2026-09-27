package com.nametweaks.gui.widget;

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
}
