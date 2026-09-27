package com.nametweaks.gui;

import com.nametweaks.NameTweaksClient;
import com.nametweaks.config.ModConfig;
import com.nametweaks.gui.widget.OffsetSliderWidget;
import com.nametweaks.gui.widget.ToggleSwitchWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NameTweaksScreen extends Screen {

    private static final String[] CATEGORIES = {"Player", "Items", "Hud", "Misc", "Fonts", "ViewModel", "Config"};
    private String selectedCategory = "Player";

    private final Screen parent;
    private int panelX, panelY;
    private final int panelWidth = 260;
    private final int panelHeight = 220;
    private final int sidebarWidth = 130;

    public NameTweaksScreen(Screen parent) {
        super(Text.literal("NameTweaks"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        panelX = (width - (sidebarWidth + panelWidth)) / 2;
        panelY = (height - panelHeight) / 2;
        rebuildContent();
    }

    private void rebuildContent() {
        clearChildren();
        if (!selectedCategory.equals("Player")) return;

        ModConfig cfg = NameTweaksClient.CONFIG;
        int rowX = panelX + sidebarWidth + 20;
        int rowY = panelY + 20;
        int rowGap = 26;

        addDrawableChild(new ToggleSwitchWidget(rowX + 170, rowY, () -> cfg.nameTagPingEnabled, v -> {
            cfg.nameTagPingEnabled = v;
            cfg.save();
        }));
        rowY += rowGap;

        addDrawableChild(new ToggleSwitchWidget(rowX + 170, rowY, () -> cfg.pingColorEnabled, v -> {
            cfg.pingColorEnabled = v;
            cfg.save();
        }));
        rowY += rowGap;

        addDrawableChild(new ToggleSwitchWidget(rowX + 170, rowY, () -> cfg.pingAboveName, v -> {
            cfg.pingAboveName = v;
            cfg.save();
            rebuildContent();
        }));
        rowY += rowGap;

        if (cfg.pingAboveName) {
            addDrawableChild(new OffsetSliderWidget(rowX, rowY, 200, 18, "Offset",
                    () -> cfg.pingOffset, v -> {
                        cfg.pingOffset = v;
                        cfg.save();
                    }));
            rowY += rowGap;
        }

        addDrawableChild(new ToggleSwitchWidget(rowX + 170, rowY, () -> cfg.nameInF5, v -> {
            cfg.nameInF5 = v;
            cfg.save();
        }));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int totalWidth = sidebarWidth + panelWidth;
        context.fill(panelX, panelY - 30, panelX + totalWidth, panelY + panelHeight, 0xE6111018);
        context.drawText(textRenderer, Text.literal("\u2715 NameTweaks"), panelX + 12, panelY - 20, 0xFFFFFFFF, false);

        context.fill(panelX, panelY, panelX + sidebarWidth, panelY + panelHeight, 0xE61A1A24);
        int catY = panelY + 10;
        for (String cat : CATEGORIES) {
            boolean selected = cat.equals(selectedCategory);
            if (selected) {
                context.fill(panelX + 6, catY - 4, panelX + sidebarWidth - 6, catY + 14, 0xFF6C5CE7);
            }
            context.drawText(textRenderer, Text.literal(cat), panelX + 16, catY, selected ? 0xFFFFFFFF : 0xFFAAAAB4, false);
            catY += 26;
        }

        context.fill(panelX + sidebarWidth, panelY, panelX + totalWidth, panelY + panelHeight, 0xE617171F);

        if (selectedCategory.equals("Player")) {
            int rowX = panelX + sidebarWidth + 20;
            int rowY = panelY + 20;
            int rowGap = 26;
            context.drawText(textRenderer, Text.literal("Name Tag Ping"), rowX, rowY + 5, 0xFFFFFFFF, false);
            rowY += rowGap;
            context.drawText(textRenderer, Text.literal("Ping Color"), rowX, rowY + 5, 0xFFFFFFFF, false);
            rowY += rowGap;
            context.drawText(textRenderer, Text.literal("Ping Above Name"), rowX, rowY + 5, 0xFFFFFFFF, false);
            rowY += rowGap;
            if (NameTweaksClient.CONFIG.pingAboveName) rowY += rowGap;
            context.drawText(textRenderer, Text.literal("Name In F5"), rowX, rowY + 5, 0xFFFFFFFF, false);
        } else {
            context.drawText(textRenderer, Text.literal("player"), panelX + sidebarWidth + 20, panelY + 20, 0xFF77778A, false);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int catY = panelY + 10;
            for (String cat : CATEGORIES) {
                if (mouseX >= panelX && mouseX <= panelX + sidebarWidth && mouseY >= catY - 4 && mouseY <= catY + 14) {
                    if (!cat.equals(selectedCategory)) {
                        selectedCategory = cat;
                        rebuildContent();
                    }
                    return true;
                }
                catY += 26;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(parent);
    }
}
