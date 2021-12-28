package com.example.screen;

// import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;
public class LoseScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Blue Team Win! Press enter to try again.", 25, 20);
    }

    
}
