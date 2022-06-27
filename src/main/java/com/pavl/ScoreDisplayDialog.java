package com.pavl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ScoreDisplayDialog extends Dialog {
    public ScoreDisplayDialog(Frame owner, String title, List<String> scores) {
        super(owner, title, false);
        this.setLayout(new GridLayout(scores.size(), 1));

        for(String score : scores) {
            this.add(new Label(score));
        }

        this.pack();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
