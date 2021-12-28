package com.example.screen;

import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;

public abstract class RestartScreen implements Screen {

    @Override
    public abstract void displayOutput(AsciiPanel terminal);

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new StartScreen();
            default:
                return this;
        }
    }

}
