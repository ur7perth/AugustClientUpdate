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
    private final int panelWidth = 300;
    private final int panelHeight = 240;
    private final int sidebarWidth = 140;
    private final int headerHeight = 32;
    private final int toggleWidth = 34;

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

    private int contentRight() {
        return panelX + sidebarWidth + panelWidth - 18;
    }

    private void rebuildContent() {
        clearChildren();
        if (!selectedCategory.equals("Player")) return;

        ModConfig cfg = NameTweaksClient.CONFIG;
        int rowX = panelX + sidebarWidth + 18;
        int rowY = panelY + 24;
        int rowGap = 32;
        int toggleX = contentRight() - toggleWidth;

        addDrawableChild(new ToggleSwitchWidget(toggleX, rowY, () -> cfg.nameTagPingEnabled, v -> {
            cfg.nameTagPingEnabled = v;
            cfg.save();
        }));
        rowY += rowGap;

        addDrawableChild(new ToggleSwitchWidget(toggleX, rowY, () -> cfg.pingColorEnabled, v -> {
            cfg.pingColorEnabled = v;
            cfg.save();
        }));
        rowY += rowGap;

        addDrawableChild(new ToggleSwitchWidget(toggleX, rowY, () -> cfg.pingAboveName, v -> {
            cfg.pingAboveName = v;
            cfg.save();
            rebuildContent();
        }));
        rowY += rowGap;

        if (cfg.pingAboveName) {
            addDrawableChild(new OffsetSliderWidget(rowX, rowY, contentRight() - rowX, 16, "Offset",
                    () -> cfg.pingOffset, v -> {
                        cfg.pingOffset = v;
                        cfg.save();
                    }));
            rowY += rowGap;
        }

        addDrawableChild(new ToggleSwitchWidget(toggleX, rowY, () -> cfg.nameInF5, v -> {
            cfg.nameInF5 = v;
            cfg.save();
        }));
    }

    private static final int SIDEBAR_ROW_HEIGHT = 26;
    private static final int SIDEBAR_ROW_START_OFFSET = 8;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int totalWidth = sidebarWidth + panelWidth;
        int outerX = panelX - 1;
        int outerY = panelY - headerHeight - 1;
        int outerRight = panelX + totalWidth + 1;
        int outerBottom = panelY + panelHeight + 1;

        context.fill(outerX, outerY, outerRight, outerBottom, 0x33000000);

        context.fillGradient(panelX, panelY - headerHeight, panelX + totalWidth, panelY,
                0xF01C1826, 0xF01A1622);
        context.fill(panelX, panelY - headerHeight, panelX + 4, panelY, 0xFF8B7BFF);
        context.drawText(textRenderer, Text.literal("NameTweaks"), panelX + 14, panelY - headerHeight / 2 - 4, 0xFFFFFFFF, false);
        context.drawText(textRenderer, Text.literal("v1.0.0"), panelX + 14, panelY - headerHeight / 2 + 5, 0xFF6F6F82, false);
        String closeHint = "esc";
        context.drawText(textRenderer, Text.literal(closeHint),
                panelX + totalWidth - textRenderer.getWidth(closeHint) - 12, panelY - headerHeight / 2 - 4, 0xFF6F6F82, false);

        context.fillGradient(panelX, panelY, panelX + sidebarWidth, panelY + panelHeight, 0xF0161320, 0xF0141018);
        int catY = panelY + SIDEBAR_ROW_START_OFFSET;
        for (String cat : CATEGORIES) {
            boolean selected = cat.equals(selectedCategory);
            boolean hovered = !selected && mouseX >= panelX && mouseX <= panelX + sidebarWidth
                    && mouseY >= catY && mouseY <= catY + SIDEBAR_ROW_HEIGHT;

            if (selected) {
                context.fill(panelX, catY, panelX + sidebarWidth, catY + SIDEBAR_ROW_HEIGHT, 0x228B7BFF);
                context.fill(panelX, catY, panelX + 3, catY + SIDEBAR_ROW_HEIGHT, 0xFF8B7BFF);
            } else if (hovered) {
                context.fill(panelX, catY, panelX + sidebarWidth, catY + SIDEBAR_ROW_HEIGHT, 0x14FFFFFF);
            }

            int textY = catY + (SIDEBAR_ROW_HEIGHT - textRenderer.fontHeight) / 2;
            context.drawText(textRenderer, Text.literal(cat), panelX + 16, textY,
                    selected ? 0xFFFFFFFF : (hovered ? 0xFFD4D4E0 : 0xFF9A9AAC), false);
            catY += SIDEBAR_ROW_HEIGHT;
        }

        int contentX = panelX + sidebarWidth;
        context.fillGradient(contentX, panelY, panelX + totalWidth, panelY + panelHeight, 0xF01A1722, 0xF0181520);

        if (selectedCategory.equals("Player")) {
            int rowX = contentX + 18;
            int labelRight = contentRight() - toggleWidth - 12;
            int rowY = panelY + 24;
            int rowGap = 32;

            drawSettingRow(context, rowX, labelRight, rowY, "Name Tag Ping", "Show player ping next to name tags");
            rowY += rowGap;
            drawSettingRow(context, rowX, labelRight, rowY, "Ping Color", "Color ping by connection quality");
            rowY += rowGap;
            drawSettingRow(context, rowX, labelRight, rowY, "Ping Above Name", "Place the ping above the name tag");
            rowY += rowGap;
            if (NameTweaksClient.CONFIG.pingAboveName) rowY += rowGap;
            drawSettingRow(context, rowX, labelRight, rowY, "Name In F5", "Show your own name tag in third person");
        } else {
            context.drawText(textRenderer, Text.literal("Coming soon"), contentX + 18, panelY + 24, 0xFF6F6F82, false);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawSettingRow(DrawContext context, int x, int labelRight, int y, String label, String subtitle) {
        context.drawText(textRenderer, Text.literal(label), x, y - 4, 0xFFECECF2, false);
        context.drawText(textRenderer, Text.literal(subtitle), x, y + 6, 0xFF77778A, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int catY = panelY + SIDEBAR_ROW_START_OFFSET;
            for (String cat : CATEGORIES) {
                if (mouseX >= panelX && mouseX <= panelX + sidebarWidth && mouseY >= catY && mouseY <= catY + SIDEBAR_ROW_HEIGHT) {
                    if (!cat.equals(selectedCategory)) {
                        selectedCategory = cat;
                        rebuildContent();
                    }
                    return true;
                }
                catY += SIDEBAR_ROW_HEIGHT;
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
