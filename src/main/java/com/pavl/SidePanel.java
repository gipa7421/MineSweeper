package com.pavl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class SidePanel extends Panel {

    private GameBoardCanvas gameBoardCanvas;

    private Button saveButton;
    private TextField nameTextField;
    private Label nameLabel;

    private Button reStartButton;
    private Button highScoresButton;
    private Label timer;
    private Label result;

    private Panel savePanel;
    private ScoreSaver scoreSaver;
    private Frame mainWindow;

    public SidePanel(GameBoardCanvas gameBoardCanvas, Frame f){
        super();

        this.mainWindow = f;
        this.gameBoardCanvas = gameBoardCanvas;
        this.scoreSaver = new ScoreSaver();

        this.saveButton = new Button("Save");
        this.saveButton.setEnabled(false);
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameTextField.getText() != ""){
                    try {
                        ScoreSaver.save(timer.getText(), nameTextField.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    nameTextField.setText("");
                    saveButton.setEnabled(false);
                    nameTextField.setEnabled(false);
                }
            }
        });

        this.nameTextField = new TextField(15);
        this.nameTextField.setEnabled(false);

        this.nameLabel = new Label("Name:");

        this.reStartButton = new Button("Restart");
        this.reStartButton.setEnabled(false);
        this.reStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reStartButton.setEnabled(false);
                gameBoardCanvas.resetBoard();
                gameBoardCanvas.repaint();
                result.setText("");
            }
        });

        this.highScoresButton = new Button("High Scores");
        this.highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> scores = ScoreSaver.get();
                    scores.sort(new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            String time1 = "";
                            String time2 = "";
                            StringTokenizer st1 = new StringTokenizer(o1);
                            while(st1.hasMoreTokens()) {
                                time1 = st1.nextToken();
                            }
                            StringTokenizer st2 = new StringTokenizer(o2);
                            while(st2.hasMoreTokens()) {
                                time2 = st2.nextToken();
                            }

                            int f1 = Integer.parseInt(time1.substring(0, 2));
                            int f2 = Integer.parseInt(time1.substring(3, 5));
                            int f3 = Integer.parseInt(time1.substring(6, 8));
                            int s1 = Integer.parseInt(time2.substring(0, 2));
                            int s2 = Integer.parseInt(time2.substring(3, 5));
                            int s3 = Integer.parseInt(time2.substring(6, 8));

                            if(f1 > s1) {
                                return -1;
                            }else if(f1 < s1) {
                                return 1;
                            }else {
                                if(f2 > s2) {
                                    return -1;
                                }else if(f2 < s2) {
                                    return 1;
                                }else {
                                    return -(Integer.compare(f3, s3));
                                }
                            }
                        }
                    });
                    ScoreDisplayDialog scoreDisplayDialog = new ScoreDisplayDialog(f, "High Scores", scores);
                    scoreDisplayDialog.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.timer = new Label("00:00:00");
        this.timer.setAlignment(Label.CENTER);

        this.result = new Label("");
        this.result.setAlignment(Label.CENTER);

        this.savePanel = new Panel();
        this.savePanel.add(this.nameLabel);
        this.savePanel.add(this.nameTextField);
        this.savePanel.add(this.saveButton);

        this.setLayout(new GridLayout(6, 3, 0, 10));

        this.add(this.savePanel);
        this.add(reStartButton);
        this.add(highScoresButton);
        this.add(timer);
        this.add(result);
    }

    public void enableRestart(boolean state){
        this.reStartButton.setEnabled(state);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 50, 0, 50);
    }

    public Label getTimer() {
        return timer;
    }

    public Label getResult() {
        return result;
    }

    public void enableSaving() {
        nameTextField.setEnabled(true);
        saveButton.setEnabled(true);
    }
}
