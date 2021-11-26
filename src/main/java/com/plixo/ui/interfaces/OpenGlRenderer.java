package com.plixo.ui.interfaces;

import com.plixo.ui.IRenderer;
import com.plixo.util.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.opengl.GLUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import static org.lwjgl.opengl.GL11.*;


public class OpenGlRenderer implements IRenderer {

    static float yOffset = 16;
    public static org.newdawn.slick.TrueTypeFont font = new org.newdawn.slick.TrueTypeFont(new Font("Verdana", Font.BOLD, 25), true);

    public static GameContainer container;

    public static void setContainer(GameContainer container) {
        OpenGlRenderer.container = container;
    }

    @Override
    public void drawLinedRect(float left, float top, float right, float bottom, int color, float width) {


        float temp;

        if (left < right) {
            temp = left;
            left = right;
            right = temp;
        }

        if (top < bottom) {
            temp = top;
            top = bottom;
            bottom = temp;
        }

        float wHalf = 0;

        drawLine(left + wHalf, top, right - wHalf, top, color, width);
        drawLine(left + wHalf, bottom, right - wHalf, bottom, color, width);

        drawLine(left, top + wHalf, left, bottom - wHalf, color, width);
        drawLine(right, top + wHalf, right, bottom - wHalf, color, width);


    }

    public void drawLinedRoundedRect(float left, float top, float right, float bottom, float radius, int color, float width) {

        if (radius <= 1f) {
            drawLinedRect(left, top, right, bottom, color, width);
            return;
        }

        float var5;

        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        radius = Math.min((left - right) / 2, Math.min((top - bottom) / 2, radius));

        circleLinedSection(left - radius, top - radius, radius, color, 0, 45, width);
        circleLinedSection(left - radius, bottom + radius, radius, color, 45, 90, width);
        circleLinedSection(right + radius, top - radius, radius, color, 135, 180, width);
        circleLinedSection(right + radius, bottom + radius, radius, color, 90, 135, width);

        drawLine(left - radius, top, right + radius, top, color, width);
        drawLine(left - radius, bottom, right + radius, bottom, color, width);
        drawLine(left, top - radius, left, bottom + radius, color, width);
        drawLine(right, top - radius, right, bottom + radius, color, width);

    }

    @Override
    public void drawLine(float left, float top, float right, float bottom, int color, float width) {

        set(color);

        glEnable(GL_LINE_SMOOTH);
        glLineWidth(width);

        glBegin(GL_LINES);
        glVertex2d(left, top);
        glVertex2d(right, bottom);
        glEnd();

        reset();
    }

    @Override
    public void drawRect(float left, float top, float right, float bottom, int color) {

        glDisable(GL_POLYGON_SMOOTH);
        float temp;

        if (left < right) {
            temp = left;
            left = right;
            right = temp;
        }

        if (top < bottom) {
            temp = top;
            top = bottom;
            bottom = temp;
        }

        set(color);
        glBegin(GL_QUADS);

        glVertex2d(left, bottom);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glVertex2d(left, top);

        glEnd();
        reset();


    }

    @Override
    public void drawCircle(float x, float y, float radius, int color) {


        set(color);
        glEnable(GL_POLYGON_SMOOTH);
        glBegin(9);
        float s = (float) (Math.PI / 90.0);
        int i = 0;
        glVertex2d(x, y);
        float theta;
        while (i <= 360) {
            theta = i * s;

            glVertex2d((x + Math.sin(theta) * radius), (y + Math.cos(theta) * radius));
            i += 9;
        }
        glEnd();
        reset();

        glDisable(GL_POLYGON_SMOOTH);
    }

    @Override
    public void drawOval(float x, float y, float width, float height, int color) {
        set(color);
        glBegin(9);
        int i = 0;
        double theta = 0;
        glVertex2d(x, y);
        while (i <= 360) {
            theta = i * Math.PI / 90.0;
            GL11.glVertex2d(x + (width / 2 * Math.cos(theta)), (height / 2 * Math.sin(theta)));
            i += 3;
        }
        glEnd();
        reset();
    }

    @Override
    public void drawRoundedRect(float left, float top, float right, float bottom, float radius, int color) {

        if (radius <= 1f) {
            drawRect(left, top, right, bottom, color);
            return;
        }

        float temp;

        if (left < right) {
            temp = left;
            left = right;
            right = temp;
        }

        if (top < bottom) {
            temp = top;
            top = bottom;
            bottom = temp;
        }

        radius = Math.min((left - right) / 2, Math.min((top - bottom) / 2, radius));

        set(color);

        drawCircleFast(left - radius, top - radius, radius, 0, 45);
        drawCircleFast(left - radius, bottom + radius, radius, 45, 90);
        drawCircleFast(right + radius, top - radius, radius, 135, 180);
        drawCircleFast(right + radius, bottom + radius, radius, 90, 135);

        drawRectFast(left - radius, top, right + radius, bottom);
        drawRectFast(left, top - radius, left - radius, bottom + radius);
        drawRectFast(right + radius, top - radius, right, bottom + radius);

        reset();

    }

    public void drawRectFast(float left, float top, float right, float bottom) {
        glDisable(GL_POLYGON_SMOOTH);
        glBegin(GL_QUADS);
        glVertex2d(left, bottom);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glVertex2d(left, top);
        glEnd();
    }

    public void drawCircleFast(float x, float y, float radius, int from, int to) {

        float s = (float) (Math.PI / 90.0f);
        float yaw;
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x, y);
        float offset = to - from;
        float rad = 0;
        while (rad <= offset) {
            yaw = (from + rad) * s;
            glVertex2d((x + Math.sin(yaw) * radius), (y + Math.cos(yaw) * radius));
            rad += 9;
        }
        glEnd();

    }

    @Override
    public void drawCircle(float x, float y, float radius, int color, int from, int to) {

        set(color);
        glBegin(9);
        int i = from;
        glVertex2d(x, y);
        while (i <= to) {
            glVertex2d((x + Math.sin((float) i * Math.PI / 90.0) * radius), (y + Math.cos(i * Math.PI / 90.0) * radius));
            i += 9;
        }
        glEnd();
        reset();
    }

    @Override
    public void circleLinedSection(float x, float y, float radius, int color, int from, int to, float width) {

        set(color);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(width);

        glBegin(GL_LINES);
        int i = from;
        float toRadiant = (float) (Math.PI / 90);
        while (i < to) {
            glVertex2d(x + Math.sin(i * toRadiant) * radius, y + Math.cos(i * toRadiant) * radius);


            i += 9;
            glVertex2d(x + Math.sin(i * toRadiant) * radius, y + Math.cos(i * toRadiant) * radius);

        }
        glEnd();
        reset();
    }

    public static void set(int color) {
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        setColor(color);
    }

    public static void reset() {
        // glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glColor4f(1, 1, 1, 1);
    }

    public static void setColor(int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0f;
        float red = (float) (color >> 16 & 255) / 255.0f;
        float green = (float) (color >> 8 & 255) / 255.0f;
        float blue = (float) (color & 255) / 255.0f;
        glColor4f(red, green, blue, alpha);
    }

    @Override
    public void pushMatrix() {
        glPushMatrix();
    }

    @Override
    public void popMatrix() {
        glPopMatrix();
    }

    @Override
    public void translate(float x, float y) {
        glTranslated(x, y, 0);
    }

    @Override
    public void scale(float x, float y) {
        glScaled(x, y, 1);
    }

    @Override
    public void activateScissor() {
        glEnable(GL_SCISSOR_TEST);
    }

    @Override
    public void deactivateScissor() {
        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public void createScissorBox(float x, float y, float x2, float y2) {

        float wDiff = x2 - x;
        float hDiff = y2 - y;
        if (wDiff < 0 || hDiff < 0) {
            return;
        }
        float factor = (float) 1;
        float bottomY = container.getHeight() - y2;
        glScissor(Math.round(x * factor), Math.round(bottomY * factor), Math.round(wDiff * factor), Math.round(hDiff * factor));
    }


    @Override
    public float[] getModelViewMatrix() {
        float[] mat = new float[16];


        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer);
        for (int i = 0; i < 16; i++) {
            mat[i] = floatBuffer.get(i);
        }
        floatBuffer.rewind();
        return mat;
    }

    @Override
    public Vector2f toScreenSpace(float[] mat, float x, float y) {
        float nX = mat[0] * x + mat[4] * y + mat[8] * 0 + mat[12];
        float nY = mat[1] * x + mat[5] * y + mat[9] * 0 + mat[13];
        return new Vector2f(nX, nY);
    }


    @Override
    public void drawCenteredString(String text, float x, float y, int color) {

        push();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(0.5f, 0.5f, 1);
        font.drawString(-getStringWidth(text), -yOffset, text, new org.newdawn.slick.Color(color));
        pop();
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(text, x + 1, y + 1, 0xFF000000);
        drawCenteredString(text, x, y, color);
    }


    @Override
    public void drawString(String text, float x, float y, int color) {
        //  System.out.println(text+" text");
        glDisable(GL_DEPTH_TEST);
        push();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_POINT_SMOOTH, GL_NICEST);
        glHint(GL_LINE_SMOOTH, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH, GL_NICEST);

        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);

        GL11.glTranslated(x, y, 0);
        GL11.glScaled(0.5f, 0.5f, 1);
        font.drawString(0, -yOffset, text, new org.newdawn.slick.Color(color));

        pop();
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, int color) {
        drawString(text, x + 1, y + 1, 0xFF000000);
        drawString(text, x, y, color);
    }

    @Override
    public float getStringWidth(String text) {
        try {
            return font.getWidth(text) / 2f;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String trimStringToWidth(String text, float width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < (float) width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getStringWidth(c0 + "");

            if (flag) {
                flag = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0F) {
                flag = true;
            } else {
                f += f1;

                if (flag1) {
                    ++f;
                }
            }

            if (f > (float) width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, (char) c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    @Override
    public float getScale() {
        float[] modelViewMatrix = getModelViewMatrix();
        return (float) Math.sqrt(modelViewMatrix[0] * modelViewMatrix[0] + modelViewMatrix[1] * modelViewMatrix[1] + modelViewMatrix[2] * modelViewMatrix[2]);
    }


    static Stack<FloatBuffer> matrices = new Stack<>();

    public static void push() {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer);
        matrices.add(floatBuffer);
    }

    public static void pop() {
        glLoadMatrix(Objects.requireNonNull(matrices.pop()));
    }

}
