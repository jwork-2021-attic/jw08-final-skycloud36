package com.example.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.example.asciiPanel.AsciiPanel;

public class StartScreen implements Screen {
    List<Button> buttons = new ArrayList<>();
    int index = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                String choice = buttons.get(index).getButtonName();
                if(choice.equals("NEW GAME")){
                    return new WorldScreen();
                }
                if(choice.equals("RECORD GAME")){
                    return new WorldScreen(true);
                }
                if(choice.equals("LOAD GAME")){
                    return new WorldScreen(choice);
                }
                if(choice.equals("REPLAY GAME")){
                    return new ReplayScreen();
                }
                if(choice.equals("ONLINE GAME")){
                    return new ClientWorldScreen();
                }
                return new WorldScreen(buttons.get(index).getButtonName());
            //     if(buttons.get(index).getButtonName() == "NEW GAME")
            //     // if(buttons.get(index).getButtonName().equals("NEW GAME"))
            //         return new WorldScreen();
            //     else
            //         return new WorldScreen(buttons.get(index).getButtonName());
            case KeyEvent.VK_DOWN:
                buttons.get(index).unselect();
                index = (index+1)%buttons.size();
                buttons.get(index).select();
                return this;
            case KeyEvent.VK_UP:
                buttons.get(index).unselect();
                index = (index-1+buttons.size())%buttons.size();
                buttons.get(index).select();
                return this;
            default:
                return this;
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("This is the start screen.", 25, 18);
        terminal.write("Press ENTER to continue...", 25, 19);

        // terminal.write(b1.getButtonName(), b1.getXloc(), b1.getYloc(), b1.getFrontColor(), b1.getBackColor());
        // terminal.write(b2.getButtonName(), b2.getXloc(), b2.getYloc(), b2.getFrontColor(), b2.getBackColor());
        if(buttons.size() == 0){
            Button b1 = new Button("NEW GAME", 25, 21, Color.WHITE, Color.GRAY, terminal);
            Button b2 = new Button("RECORD GAME", 25, 23, terminal);
            Button b3 = new Button("LOAD GAME", 25, 25, terminal);
            Button b4 = new Button("REPLAY GAME", 25, 27, terminal);
            Button b5 = new Button("ONLINE GAME", 25, 29, terminal);
            buttons.add(b1);
            buttons.add(b2);
            buttons.add(b3);
            buttons.add(b4);
            buttons.add(b5);
        }
        for(Button b : buttons){
            b.display();
        }
    }

}
