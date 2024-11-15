package ca.bungo.sneakyqol.features;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class QuickSelectMenu extends Screen {

    private static final int SEGMENT_COUNT = 5;
    private static final float SEGMENT_ANGLE = 360.0f / SEGMENT_COUNT;
    private int selectedSegment = -1;

    public QuickSelectMenu() {
        super(Text.of("Action Wheel"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderBackground(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int radius = 60;

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            renderSegment(context, centerX, centerY, radius, i, mouseX, mouseY);
        }

        if (selectedSegment != -1) {
            renderHighlight(context, centerX, centerY, radius, selectedSegment);
        }
    }

    private void renderSegment(DrawContext context, int centerX, int centerY, int radius, int segment, int mouseX, int mouseY) {
        float startAngle = segment * SEGMENT_ANGLE;
        float endAngle = startAngle + SEGMENT_ANGLE;

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(centerX, centerY, 0).color(0.7f, 0.7f, 0.7f, 0.7f);

        int segments = 100; // Number of segments for smooth arc
        for (int i = 0; i <= segments; i++) {
            float angle = MathHelper.lerp(i / (float) segments, startAngle, endAngle);
            float x = centerX + radius * MathHelper.cos((float) Math.toRadians(angle));
            float y = centerY + radius * MathHelper.sin((float) Math.toRadians(angle));
            bufferBuilder.vertex(x, y, 0).color(0.7f, 0.7f, 0.7f, 0.7f);
        }

        BufferRenderer.draw(bufferBuilder.end());

        if (isMouseInSegment(centerX, centerY, radius, mouseX, mouseY, startAngle, endAngle)) {
            selectedSegment = segment;
        }
    }

    private void renderHighlight(DrawContext context, int centerX, int centerY, int radius, int segment) {
        float startAngle = segment * SEGMENT_ANGLE;
        float endAngle = startAngle + SEGMENT_ANGLE;

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(centerX, centerY, 0).color(1.0f, 1.0f, 0.0f, 1.0f);

        int segments = 100; // Number of segments for smooth arc
        for (int i = 0; i <= segments; i++) {
            float angle = MathHelper.lerp(i / (float) segments, startAngle, endAngle);
            float x = centerX + radius * MathHelper.cos((float) Math.toRadians(angle));
            float y = centerY + radius * MathHelper.sin((float) Math.toRadians(angle));
            bufferBuilder.vertex(x, y, 0).color(1.0f, 1.0f, 0.0f, 1.0f);
        }

        BufferRenderer.draw(bufferBuilder.end());
    }

    private boolean isMouseInSegment(int centerX, int centerY, int radius, int mouseX, int mouseY, float startAngle, float endAngle) {
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double distanceSquared = dx * dx + dy * dy;

        if (distanceSquared > radius * radius) {
            return false;
        }

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        if (angle < 0) {
            angle += 360;
        }

        return angle >= startAngle && angle < endAngle;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            selectedSegment = getSegmentAt(mouseX, mouseY);
            if (selectedSegment != -1) {
                // Trigger action for the selected segment
                performAction(selectedSegment);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private int getSegmentAt(double mouseX, double mouseY) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int radius = 60;

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            float startAngle = i * SEGMENT_ANGLE;
            float endAngle = startAngle + SEGMENT_ANGLE;
            if (isMouseInSegment(centerX, centerY, radius, (int) mouseX, (int) mouseY, startAngle, endAngle)) {
                return i;
            }
        }
        return -1; // No segment found
    }

    private void performAction(int segment) {
        // Implementation for performing an action based on the selected segment
        switch (segment) {
            case 0:
                // Action for segment 0
                break;
            case 1:
                // Action for segment 1
                break;
            case 2:
                // Action for segment 2
                break;
            case 3:
                // Action for segment 3
                break;
            case 4:
                // Action for segment 4
                break;
            default:
                break;
        }
    }

    @Override
    public boolean shouldPause() {
        return false; // Allow player movement while the action wheel is open
    }

}
