package com.example.screen;

import java.awt.Color;

// import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;
public class LoseScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You lose! Press enter to try again.", 25, 20, Color.GREEN);
    }

    
}
