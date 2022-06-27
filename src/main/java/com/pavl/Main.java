package com.pavl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Frame f = new Frame();
        f.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 1));
        GameBoardCanvas gameBoardCanvas = new GameBoardCanvas(10, 481, 481);
        f.add(gameBoardCanvas);
        SidePanel sidePanel = new SidePanel(gameBoardCanvas, f);
        f.add(sidePanel);
        gameBoardCanvas.setSidePanel(sidePanel);
        f.setVisible(true);
        f.setTitle("Test");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.pack();
        //End
    }
}
