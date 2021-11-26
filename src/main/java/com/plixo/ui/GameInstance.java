package com.plixo.ui;

import com.plixo.animation.Animation;
import com.plixo.test.Main;
import com.plixo.ui.elements.canvas.UICanvas;
import com.plixo.ui.interfaces.LwjglKeyboard;
import com.plixo.ui.interfaces.LwjglMouse;
import com.plixo.ui.interfaces.OpenGlRenderer;
import com.plixo.util.FileUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameInstance extends BasicGame {

    UICanvas canvas;
    float mouseX, mouseY;
    long lastMs = System.currentTimeMillis();

    public float mouseDWheel = 0;

    public static GameInstance INSTANCE;


    public GameInstance() {
        super("Slick");
        INSTANCE = this;
    }



    @Override
    public void init(GameContainer container) throws SlickException {
        canvas = new UICanvas();

        canvas.setDimensions(0, 0, container.getWidth(), container.getHeight());
        canvas.setColor(ColorLib.getBackground(0.2f));

        FileUtil.STANDARD_PATH = "files/";

        OpenGlRenderer.setContainer(container);
        new LodestoneUI(new OpenGlRenderer() , new LwjglKeyboard(), new LwjglMouse());

        Main main = new Main();
        main.init(canvas);
    }


    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        long deltaMs = System.currentTimeMillis() - lastMs;
        if (deltaMs > 50) {
            lastMs = System.currentTimeMillis() - Math.min(50, (deltaMs - 50));
            canvas.onTick();
        }
    }



    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        mouseDWheel = Mouse.getDWheel();

        Animation.step();

        canvas.drawScreen(mouseX, mouseY);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
      //  UIElement.gui.drawStringWithShadow("FPS: " + container.getFPS(), 10, canvas.height - 10, -1);

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        mouseX = x;
        mouseY = y;
        super.mouseClicked(button, x, y, clickCount);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mouseX = newx;
        mouseY = newy;
        super.mouseDragged(oldx, oldy, newx, newy);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouseX = newx;
        mouseY = newy;
        super.mouseMoved(oldx, oldy, newx, newy);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        mouseX = x;
        mouseY = y;
        canvas.mouseClicked(mouseX, mouseY, button);
        super.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        mouseX = x;
        mouseY = y;

        canvas.mouseReleased(mouseX, mouseY, button);

        IOObject.draggedObject = null;
        super.mouseReleased(button, x, y);
    }

    @Override
    public void keyPressed(int key, char typedChar) {
        canvas.keyTyped(typedChar, key);
        super.keyPressed(key, typedChar);
    }

    @Override
    public void mouseWheelMoved(int change) {
        super.mouseWheelMoved(change);
    }
}
