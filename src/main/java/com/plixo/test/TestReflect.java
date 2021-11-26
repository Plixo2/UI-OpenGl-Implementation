package com.plixo.test;

import com.plixo.ui.resource.util.SimpleSlider;
import com.plixo.util.Color;
import com.plixo.util.FileUtil;
import com.plixo.util.Vector2f;
import com.plixo.util.Vector3f;
import org.plixo.gsonplus.HideField;

import java.io.File;

public class TestReflect {
    public boolean aBoolean = false;

    public float amount= 0;

    public String helloWorld = "HelloWorld";

    public int spinner = 0;

    public SimpleSlider slider = new SimpleSlider(10,0,100);

    public Vector2f vector2f = new Vector2f(1,2);

    public Vector3f vector3f = new Vector3f(1,2,3);

    public Runnable action = () -> {
        System.out.println("do something");
    };

    public AEnum aEnum = AEnum.Hidden;

    public Color color = new Color(0xFF00FF00);

    public File aFile = FileUtil.getFileFromName("text.txt");



    static enum AEnum {
        Yes,No,Hidden,Print
    }

    @HideField
    public boolean hidden = false;
}
