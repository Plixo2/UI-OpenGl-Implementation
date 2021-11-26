package com.plixo.ui.interfaces;

import com.plixo.ui.GameInstance;
import com.plixo.ui.IMouse;

public class LwjglMouse implements IMouse {
    @Override
    public float getDWheel() {
        return GameInstance.INSTANCE.mouseDWheel;
    }
}
