package com.pavl;

import java.awt.*;
import java.util.*;

public class LabelTimerTask extends TimerTask {
    private Label timerLabel;
    private Date startTime;

    public LabelTimerTask(Label timerLabel, Date startTime) {
        this.timerLabel = timerLabel;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        Date currentTime = new Date();
        long seconds = (currentTime.getTime() - startTime.getTime()) / 1000;
        long hour = seconds / 3600;
        seconds = seconds % 3600;
        long minute = seconds / 60;
        seconds = seconds % 60;
        timerLabel.setText(new Formatter().format("%02d:%02d:%02d", hour, minute, seconds).toString());
    }
}
