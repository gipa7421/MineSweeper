package com.pavl;

import java.util.*;

public class GameBoardModel {

    private final List<List<Spot>> spots;
    private int bombCount;

    public GameBoardModel(int size){
        int count = 0;
        Random random = new Random();
        List<List<Spot>> spots = new ArrayList<>();
        for(int i = 0; i < size; i++){
            List<Spot> row = new ArrayList<>();
            for(int j = 0; j < size; j++){
                boolean bomb = false;
                int num = random.nextInt(100);
                if(num >= 80){
                    bomb = true;
                    count++;
                }
                Spot spot = new Spot(j, i, bomb, false, null);
                row.add(spot);
            }
            spots.add(row);
        }
        this.spots = spots;
        this.bombCount = count;
    }

    public List<List<Spot>> getSpots() {
        return this.spots;
    }

    public int getDiscoveredCount() {
        int count = 0;
        for(List<Spot> row : spots) {
            for(Spot sp : row) {
                if(sp.isDiscovered()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getBombCount() {
        return bombCount;
    }

    public List<Spot> discoverBombs(){
        List<Spot> bombs = new ArrayList<>();
        for(int i = 0; i < spots.size(); i++){
            for(int j = 0; j < spots.size(); j++){
                Spot currentSpot = spots.get(i).get(j);
                if(currentSpot.hasBomb()){
                    currentSpot.setDiscovered(true);
                    bombs.add(currentSpot);
                }
            }
        }
        return bombs;
    }

    public List<Spot> getNeighbouringSpots(int xCord, int yCord){
        List<Spot> neighbours = new ArrayList<>();
        for(int i = yCord - 1; i <= yCord + 1; i++){
            for(int j = xCord - 1; j <= xCord + 1; j++){
                if(i != yCord || j != xCord) {
                    if (j >= 0 && j < this.spots.size() && i >= 0 && i < this.spots.size()) {
                        neighbours.add(this.spots.get(i).get(j));
                    }
                }
            }
        }
        return neighbours;
    }

    public List<Spot> openSpots(Spot startingSpot){
        List<Spot> openedSpots = new ArrayList<>();
        Queue<Spot> queue = new LinkedList<>();
        Set<Spot> visited = new HashSet<>();
        queue.add(startingSpot);

        while(!queue.isEmpty()){
            Spot s = queue.poll();
            openedSpots.add(s);
            visited.add(s);
            s.setDiscovered(true);
            int neighbouringBombCount = 0;

            List<Spot> neighbours = this.getNeighbouringSpots(s.getXCord(), s.getYCord());

            for(Spot sp : neighbours){
                if(sp.hasBomb()){
                    neighbouringBombCount++;
                }
            }
            s.setNeighbouringBombsCount(neighbouringBombCount);
            if(neighbouringBombCount == 0){
                for(Spot neighbour : neighbours){
                    if(!visited.contains(neighbour)){
                        queue.add(neighbour);
                    }
                }
            }
        }
        return openedSpots;
    }
}
