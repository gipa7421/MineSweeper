package com.pavl;


import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameBoardCanvas extends Canvas {

    private static final int DISCOVERED_SPOT_COLOR_R = 230;
    private static final int DISCOVERED_SPOT_COLOR_G = 224;
    private static final int DISCOVERED_SPOT_COLOR_B = 174;

    private static final Color UNDISCOVERED_SPOT_COLOR = Color.GREEN;
    private static final Color HIGHLIGHTED_SPOT_COLOR = Color.RED;

    private GameBoardModel gameBoardModel;
    private final int blockSize;
    private final List<List<Rectangle>> blocks;
    private Rectangle highlightedSpot;
    private Rectangle previousHighlightedSpot;
    private final Image bombImage;
    private GameState gameState;
    private Timer timer;

    private SidePanel sidePanel;

    private MouseMotionAdapter mouseMotionAdapter;
    private MouseAdapter mouseAdapter;

    public GameBoardCanvas(int size, int width, int height) throws IOException {
        super();
        this.gameState = GameState.NOT_STARTED;
        this.bombImage = ImageIO.read(new File("./src/main/resources/bomb.png"));
        this.blocks = new ArrayList<>();
        this.setSize(width, height);
        this.setBackground(Color.BLUE);
        this.setVisible(true);
        this.gameBoardModel = new GameBoardModel(size);
        this.blockSize = (this.getWidth() - this.gameBoardModel.getSpots().size() - 1) / this.gameBoardModel.getSpots().size();
        this.setFont(new Font("SansSerif", Font.PLAIN, blockSize * 3 / 4));

        for(int i = 0; i < this.gameBoardModel.getSpots().size(); i++){
            List<Rectangle> row = new ArrayList<>();
            for(int j = 0; j < this.gameBoardModel.getSpots().size(); j++){
                row.add(new Rectangle(j + 1 + j * blockSize, i + 1 + i * blockSize, blockSize, blockSize));
            }
            this.blocks.add(row);
        }

        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                var res = findSpot(e.getX(), e.getY());
                if(res != null) {
                    Spot spot = res.left;
                    if (!spot.isDiscovered()) {
                        sidePanel.enableRestart(true);
                        highlightedSpot = null;
                        if (spot.hasBomb()) {
                            gameState = GameState.LOST;
                            List<Spot> bombs = gameBoardModel.discoverBombs();
                            for (Spot s : bombs) {
                                repaint(s.getXCord() + 1 + s.getXCord() * blockSize, s.getYCord() + 1 + s.getYCord() * blockSize, blockSize, blockSize);
                            }
                            for (MouseListener mouseListener : getListeners(MouseListener.class)) {
                                removeMouseListener(mouseListener);
                            }
                            for (MouseMotionListener mouseMotionListener : getListeners(MouseMotionListener.class)) {
                                removeMouseMotionListener(mouseMotionListener);
                            }
                            if(timer != null) {
                                timer.cancel();
                            }
                            sidePanel.getResult().setText("You Lose");
                        } else {
                            gameState = GameState.NOT_FINISHED;
                            if(timer == null) {
                                timer = new Timer(true);
                                timer.schedule(new LabelTimerTask(sidePanel.getTimer(), new Date()), 0, 1000);
                            }
                            List<Spot> openedSpots = gameBoardModel.openSpots(spot);
                            for (Spot s : openedSpots) {
                                repaint(s.getXCord() + 1 + s.getXCord() * blockSize, s.getYCord() + 1 + s.getYCord() * blockSize, blockSize, blockSize);
                            }
                            if(gameBoardModel.getBombCount() + gameBoardModel.getDiscoveredCount() == size * size) {
                                if(timer != null) {
                                    timer.cancel();
                                }
                                List<Spot> bombs = gameBoardModel.discoverBombs();
                                for (Spot s : bombs) {
                                    repaint(s.getXCord() + 1 + s.getXCord() * blockSize, s.getYCord() + 1 + s.getYCord() * blockSize, blockSize, blockSize);
                                }
                                sidePanel.getResult().setText("You Win");
                                sidePanel.enableSaving();
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                previousHighlightedSpot = highlightedSpot;
                highlightedSpot = null;
                if(previousHighlightedSpot != null){
                    repaint(previousHighlightedSpot.x, previousHighlightedSpot.y, previousHighlightedSpot.width, previousHighlightedSpot.height);
                }
            }
        };

        this.mouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var spot = findSpot(e.getX(), e.getY());

                if(spot == null){
                    if(highlightedSpot != null){
                        previousHighlightedSpot = highlightedSpot;
                        highlightedSpot = null;
                        repaint(previousHighlightedSpot.x, previousHighlightedSpot.y, previousHighlightedSpot.width, previousHighlightedSpot.height);
                    }
                }else {
                    if (!spot.left.isDiscovered()) {
                        if (highlightedSpot == null) {
                            previousHighlightedSpot = null;
                            highlightedSpot = spot.right;
                            repaint(highlightedSpot.x, highlightedSpot.y, highlightedSpot.width, highlightedSpot.height);
                        } else if (!spot.right.equals(highlightedSpot)) {
                            previousHighlightedSpot = highlightedSpot;
                            highlightedSpot = spot.right;
                            repaint(highlightedSpot.x, highlightedSpot.y, highlightedSpot.width, highlightedSpot.height);
                            repaint(previousHighlightedSpot.x, previousHighlightedSpot.y, previousHighlightedSpot.width, previousHighlightedSpot.height);
                        }
                    }else if(highlightedSpot != null){
                        if(!findSpot(highlightedSpot.x + 1, highlightedSpot.y + 1).left.isDiscovered()){
                            previousHighlightedSpot = highlightedSpot;
                            highlightedSpot = null;
                            repaint(previousHighlightedSpot.x, previousHighlightedSpot.y, previousHighlightedSpot.width, previousHighlightedSpot.height);
                        }
                    }
                }
            }
        };

        this.addMouseListener(this.mouseAdapter);
        this.addMouseMotionListener(this.mouseMotionAdapter);
    }

    public void setSidePanel(SidePanel sidePanel) {
        this.sidePanel = sidePanel;
    }

    public void resetBoard(){
        this.gameState = GameState.NOT_FINISHED;
        int size = this.gameBoardModel.getSpots().size();
        this.gameBoardModel = new GameBoardModel(size);
        if(timer != null) {
            this.timer.cancel();
        }
        this.timer = null;
        this.sidePanel.getTimer().setText("00:00:00");
        this.addMouseListener(this.mouseAdapter);
        this.addMouseMotionListener(this.mouseMotionAdapter);
    }

    private ImmutablePair<Spot, Rectangle> findSpot(int screenCordX, int screenCordY){
        for(int i = 0; i < this.gameBoardModel.getSpots().size(); i++){
            for(int j = 0; j < this.gameBoardModel.getSpots().size(); j++){
                if(this.blocks.get(i).get(j).contains(new Point(screenCordX, screenCordY))){
                    return new ImmutablePair<>(this.gameBoardModel.getSpots().get(i).get(j), this.blocks.get(i).get(j));
                }
            }
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        for(int i = 0; i < this.gameBoardModel.getSpots().size(); i++){
            for(int j = 0; j < this.gameBoardModel.getSpots().size(); j++){
                if(this.highlightedSpot != null) {
                    if (i + 1 + i * blockSize == this.highlightedSpot.y && j + 1 + j * blockSize == this.highlightedSpot.x) {
                        g.setColor(HIGHLIGHTED_SPOT_COLOR);
                        Rectangle currentRectangle = this.blocks.get(i).get(j);
                        g.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);
                    } else {
                        drawSpot(j, i, g);
                    }
                }else{
                    drawSpot(j, i, g);
                }
            }
        }
    }

    private void drawSpot(int x, int y, Graphics g) {
        Spot currentSpot = this.gameBoardModel.getSpots().get(y).get(x);
        if(currentSpot.isDiscovered()){
            if(currentSpot.hasBomb()){
                g.setColor(new Color(DISCOVERED_SPOT_COLOR_R, DISCOVERED_SPOT_COLOR_G, DISCOVERED_SPOT_COLOR_B));
                Rectangle currentRectangle = this.blocks.get(y).get(x);
                g.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);
                g.drawImage(this.bombImage,x + 1 + x * blockSize, y + 1 + y * blockSize, x + 1 + x * blockSize + blockSize, y + 1 + y * blockSize + blockSize, 0, 0, 512, 512, null);
            }else {
                g.setColor(new Color(DISCOVERED_SPOT_COLOR_R, DISCOVERED_SPOT_COLOR_G, DISCOVERED_SPOT_COLOR_B));
                Rectangle currentRectangle = this.blocks.get(y).get(x);
                g.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);
                if (currentSpot.getNeighbouringBombsCount() != 0) {
                    g.setColor(Color.BLACK);
                    String txt = currentSpot.getNeighbouringBombsCount().toString();
                    char character = txt.charAt(0);
                    FontMetrics fm = g.getFontMetrics();
                    int charWidth = fm.charWidth(character);
                    g.drawString(txt, x + 1 + x * blockSize + (blockSize - charWidth) / 2, y + 1 + y * blockSize + (blockSize - fm.getAscent() - fm.getDescent()) / 2 + (fm.getAscent()));
                }
            }
        }else{
            g.setColor(UNDISCOVERED_SPOT_COLOR);
            Rectangle currentRectangle = this.blocks.get(y).get(x);
            g.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);
        }
    }
}
