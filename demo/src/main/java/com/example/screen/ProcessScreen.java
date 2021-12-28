package com.example.screen;

import java.io.*;
import java.util.List;
import java.awt.event.*;
// import java.util.concurrent.TimeUnit;

import javax.swing.*;
// import com.example.maze.World;

import com.example.maze.World;

// import org.w3c.dom.events.MouseEvent;



public class ProcessScreen extends JFrame{
    private World world;
    JTextArea textArea;
    JScrollPane jsp;
    public ProcessScreen(World world){
    // public ProcessScreen(){
        this.world = world;
        this.setTitle("游戏进程");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(1300, 80);

        textArea = new JTextArea(30, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        jsp = new JScrollPane(textArea);

        JMenuBar menu = new JMenuBar();
        JMenu b1 = new JMenu("操作");
        JMenuItem b2 = new JMenuItem("保存");
        b2.addActionListener(new myAction(this));
        b1.add(b2);
        menu.add(b1);

        this.setJMenuBar(menu);
        this.add(jsp);
        this.pack();
        this.addWindowListener(new myClose(this));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    public void addProcess(String process){
        this.textArea.append(process +"\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    public void saveByThread(){
        World temp_world = this.world;
        class saveThread implements Runnable{
            String process;
            saveThread(JTextArea text){
                synchronized(text){
                    this.process = text.getText();
                }
            }

            @Override
            public void run(){
                try {
                    WriteProcess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void WriteProcess() throws IOException{
                BufferedWriter outputStream = null;
                try{
                    outputStream = new BufferedWriter(new FileWriter("GameProcess.txt"));
                    if(outputStream != null){
                        outputStream.write(process + "\n");
                    }
                }
                finally{
                    if(outputStream != null)
                        outputStream.close();
                }                
                try{
                    outputStream = new BufferedWriter(new FileWriter("GameMap.txt"));
                    List<String> Info = world.saveMap();
                    if(outputStream != null){            
                        for(String info : Info){
                            // System.out.println(info);
                            outputStream.write(info + "\n");
                        }
                    }
                }
                finally{
                    if(outputStream != null)
                        outputStream.close();
                }
            }
        }
        
        saveThread s1 = new saveThread(this.textArea);
        Thread t1 = new Thread(s1);
        t1.start();
    }

    class myClose extends WindowAdapter{
        ProcessScreen ps;
        myClose(ProcessScreen ps){
            super();
            this.ps = ps;
        }

        @Override 
        public void windowClosing(WindowEvent e){
            // ps.saveByThread();
            System.exit(0);
        }
    }

    class myAction implements ActionListener{
        ProcessScreen ps;
        myAction(ProcessScreen ps){
            super();
            this.ps = ps;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ps.saveByThread();
        }
    }
}
