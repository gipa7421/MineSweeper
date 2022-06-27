package com.pavl;

/**
 * A {@code Spot} object represents spot on Minesweeper game board
 */
public class Spot {

    private final int xCord;
    private final int yCord;
    private final boolean hasBomb;
    private boolean isDiscovered;
    private Integer neighbouringBombsCount;

    /**
     * @param xCord X coordinate
     * @param yCord Y coordinate
     * @param hasBomb If spot contains bomb or not
     * @param isDiscovered If spot is already discovered
     * @param neighbouringBombsCount Number of neighbours that contain bombs
     */
    public Spot(int xCord, int yCord, boolean hasBomb, boolean isDiscovered, Integer neighbouringBombsCount) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.hasBomb = hasBomb;
        this.isDiscovered = isDiscovered;
        this.neighbouringBombsCount = neighbouringBombsCount;
    }

    /**
     * @return X coordinate
     */
    public int getXCord() {
        return xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public Integer getNeighbouringBombsCount() {
        return neighbouringBombsCount;
    }

    public void setDiscovered(boolean discovered) {
        isDiscovered = discovered;
    }

    public void setNeighbouringBombsCount(Integer neighbouringBombsCount) {
        this.neighbouringBombsCount = neighbouringBombsCount;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Spot)){
            return false;
        }

        return this.xCord == ((Spot) obj).getXCord() && this.yCord == ((Spot) obj).getYCord();
    }

    @Override
    public String toString() {
        return "Spot{" +
                "xCord=" + xCord +
                ", yCord=" + yCord +
                ", hasBomb=" + hasBomb +
                ", isDiscovered=" + isDiscovered +
                ", neighbouringBombsCount=" + neighbouringBombsCount +
                '}';
    }
}
