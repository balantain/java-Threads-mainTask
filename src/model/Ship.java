package model;

import service.ShipService;

public class Ship implements Runnable {
    private String name;
    private int shipMaxCapacity;
    private int containersOnBoard;
    private Port port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShipMaxCapacity() {
        return shipMaxCapacity;
    }

    public void setShipMaxCapacity(int shipMaxCapacity) {
        this.shipMaxCapacity = shipMaxCapacity;
    }

    public int getContainersOnBoard() {
        return containersOnBoard;
    }

    public void setContainersOnBoard(int containersOnBoard) {
        this.containersOnBoard = containersOnBoard;
    }

    public Ship(String name, int shipMaxCapacity, int containersOnBoard, Port port) {
        this.name = name;
        this.shipMaxCapacity = shipMaxCapacity;
        this.containersOnBoard = containersOnBoard;
        this.port = port;
    }
    public int getFreeSpaceOnBoard(){
        return shipMaxCapacity - containersOnBoard;
    }

    @Override
    public void run() {
        ShipService.doAction(this, port);
    }

    @Override
    public String toString() {
        return "|" + name + ", max:" + shipMaxCapacity + ", on board: " + containersOnBoard + "|";
    }
}
