package com.plixo.ui.interfaces;

import com.plixo.ui.IKeyboard;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class LwjglKeyboard implements IKeyboard {

    @Override
    public boolean isKeyDown(int i) {
        return Keyboard.isKeyDown(i);
    }

    @Override
    public boolean isKeyComboCtrlX(int i) {
        return i == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }

    @Override
    public boolean isKeyComboCtrlV(int i) {
        return i == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }

    @Override
    public boolean isKeyComboCtrlC(int i) {
        return i == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }

    @Override
    public boolean isKeyComboCtrlA(int i) {
        return i == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }

    @Override
    public boolean isCtrlKeyDown() {
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    @Override
    public boolean isAltKeyDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }

    @Override
    public boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    @Override
    public String getClipboardString() {
        try
        {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception var1)
        {
            ;
        }

        return "";
    }

    @Override
    public void setClipboardString(String copyText) {
        if (copyText != null && copyText.length() > 0)
        {
            try
            {
                StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner)null);
            }
            catch (Exception var2)
            {
                ;
            }
        }
    }


    @Override
    public boolean isAllowedTextCharacter(char character)
    {
        final boolean b = character != 167 && character >= 32 && character != 127;
        return b;
    }

    @Override
    public String filterAllowedCharacters(String input) {

        StringBuilder stringbuilder = new StringBuilder();

        for (char c0 : input.toCharArray())
        {
            if (isAllowedTextCharacter(c0))
            {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }

    @Override
    public String getKeyName(int i) {
        return Keyboard.getKeyName(i);
    }

    @Override
    public int ESCAPE() {
        return Keyboard.KEY_ESCAPE;
    }

    @Override
    public int RETURN() {
        return Keyboard.KEY_RETURN;
    }

    @Override
    public int BACK() {
        return Keyboard.KEY_BACK;
    }
}
