package com.plixo.test;

import com.plixo.animation.Animation;
import com.plixo.animation.Ease;
import com.plixo.ui.ColorLib;
import com.plixo.ui.elements.canvas.*;

import com.plixo.ui.elements.other.UIButton;
import com.plixo.ui.elements.other.UIScrollBar;
import com.plixo.ui.resource.UIReference;
import com.plixo.util.reference.FieldReference;
import com.plixo.util.reflections.ReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Main {

    public void init(UICanvas canvas) {
        Keyboard.enableRepeatEvents(true);


        UIScalar scalar = new UIScalar();
        scalar.copyDimensions(canvas);
        scalar.setScale(2f);
        scalar.scaleDimensions();
        canvas.add(scalar);
        canvas = scalar;

        canvas.setRoundness(0);
        canvas.setColor(ColorLib.getBackground(0));

     //   UIGrid draggable = new UIGrid();
      //  draggable.setDimensions(0,0,canvas.width,canvas.height);

        UIColumn column = new UIColumn();
        column.setDimensions(10,10,0,0);


        column.setSpace(5);
       // column.add(button);

        TestReflect reflect = new TestReflect();
        final List<FieldReference<?>> fieldReferences = new ReflectionHelper().reflectFields(reflect);

        fieldReferences.forEach(ref -> {
            UIReference reference = new UIReference();
            //reference.setOutlineWidth(2);
            reference.initialize(0,0,700,90,ref.getName(),ref,ref.getField().getType());
            reference.getReferenceObject().getUIElement().setOutlineWidth(2);
            column.add(reference);
        });

        column.pack();
        column.height = canvas.height;


        UIScrollBar scrollBar = new UIScrollBar();
        scrollBar.setDragerHeight(20);
        scrollBar.setColor(0x55FFFFFF);
        scrollBar.setDimensions(2,10,5,canvas.height-20);
        scrollBar.setReturnPercent(column::setPercent);
        scrollBar.setSupplier(column::getPercent);
        canvas.add(scrollBar);


        UIButton button = new UIButton();
        button.setDimensions(800,0,100,20);
        button.setDisplayName("Hello");
        button.setAction(() -> {
            //Animation.wait(seconds, () -> {});
            Animation.animate(scrollBar::setDragerHeight,20,100,5, Ease.OutBounce);
        });
        canvas.add(button);

        Animation.animate(column::setPercent,0,1,5, Ease.InOutCubic);

       // draggable.add(column);
      //  draggable.setShouldDrawLines(true);

/*


        UITextBox textBox = new UITextBox();
        textBox.setReference(new ObjectReference<>("Hello Textbox"));
        textBox.setDimensions(10,0,400,30);
        textBox.setRoundness(10);
        column.add(textBox);



 */

        canvas.add(column);
    }

}
