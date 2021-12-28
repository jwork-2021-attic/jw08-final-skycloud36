package com.example.screen;

// import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;
public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Red Team won! Press enter to go again.", 25, 20);
    }

}
