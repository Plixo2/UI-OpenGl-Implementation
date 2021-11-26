package com.plixo.ui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Test {
    public static void main(String[] args) {
        GameInstance gameInstance = new GameInstance();
        AppGameContainer container;
        try {
            container = new AppGameContainer(gameInstance, 1400, 800, false);
            container.setShowFPS(false);
            container.setMultiSample(16);
            container.setVSync(true);

            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }
}
