package com.example;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.example.maze.MazeGenerator;
import com.example.maze.World;
import com.example.screen.ClientWorldScreen;
import com.example.screen.Screen;
import com.example.screen.StartScreen;
import com.example.screen.WorldScreen;


import com.example.asciiPanel.AsciiFont;
import com.example.asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener, MouseListener {

    public AsciiPanel terminal;
    public Screen screen;

    class myClose extends WindowAdapter{
        myClose(){
            super();
        }

        @Override 
        public void windowClosing(WindowEvent e){
            if(screen instanceof ClientWorldScreen){
                ((ClientWorldScreen)screen).closeClient();
            }
        }
    }


    public Main() {
        super();

        

        // terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT + 5, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        // screen = new StartScreen();
        // screen = new WorldScreen(false);
        screen = new ClientWorldScreen();
        // ((ClientWorldScreen)screen).listen();
        addKeyListener(this);
        addMouseListener(this);
        // repaint();
        paintByTimer();
        this.addWindowListener(new myClose());

    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        // if(screen instanceof WorldScreen){
        //     Screen temp = ((WorldScreen)screen).Finish();
        //     if(temp != null){    
        //         super.repaint();
        //         screen = temp;
        //         try {
        //             TimeUnit.MILLISECONDS.sleep(1000);
        //         } catch (InterruptedException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        // repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("x:" + e.getX() + " y:" + e.getY());
        screen.respondToUserMouse(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    private void paintByTimer(){
        Runnable runnable = new Runnable(){
            @Override
            public void run(){
                repaint();
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        int frameRate = 1000 / 30;
        service.scheduleAtFixedRate(runnable, 0, frameRate, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        // System.out.println("hello world");
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public Screen getScreen(){
        return this.screen;
    }

    public AsciiPanel getTerminal(){
        return this.terminal;
    }
}
