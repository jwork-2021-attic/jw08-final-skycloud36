package com.example.screen;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
// import org.w3c.dom.events.MouseEvent;

import com.example.asciiPanel.AsciiPanel;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);

    public default Screen respondToUserMouse(MouseEvent mouse){return null;};
}
