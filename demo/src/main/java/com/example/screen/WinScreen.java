package com.example.screen;

import java.awt.Color;
// import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;

public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You won! Press enter to go again.", 25, 20, Color.GREEN);
    }

}
