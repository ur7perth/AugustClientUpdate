package com.augustclient.gui;

import com.augustclient.AugustClient;
import com.augustclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class AugustClickGuiScreen extends Screen {

    public AugustClickGuiScreen() {
        super(Text.literal("AugustClient GUI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Frame
        context.fill(50, 30, this.width - 50, this.height - 30, 0xCC111827);
        context.drawTextWithShadow(this.textRenderer, "AugustClient v1.0 | Competitive Edition", 60, 40, 0x00F3FF);

        int y = 70;
        List<Module> modules = AugustClient.moduleManager.getModules();
        for (Module m : modules) {
            int color = m.isEnabled() ? 0x00FF88 : 0xAAAAAA;
            String text = m.getName() + " [" + (m.isEnabled() ? "ON" : "OFF") + "] (" + m.getCategory() + ")";
            context.drawTextWithShadow(this.textRenderer, text, 65, y, color);
            y += 18;
            if (y > this.height - 50) break;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int y = 70;
        List<Module> modules = AugustClient.moduleManager.getModules();
        for (Module m : modules) {
            if (mouseX >= 65 && mouseX <= 300 && mouseY >= y && mouseY <= y + 15) {
                m.toggle();
                return true;
            }
            y += 18;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
