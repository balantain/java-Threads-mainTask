package service;

import model.Port;
import model.Ship;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShipService {
    private static final Lock LOCK = new ReentrantLock();
    private static final int ONE_CONTAINER_LOADING_TIME = 10;
    private static final Condition condition = LOCK.newCondition();

    public static void doAction(Ship ship, Port port){ // we give an opportunity for all ships to come in port
        System.out.println(ship.toString() + " arrived to the port, looking for a free dock...");
        try {
            TimeUnit.MILLISECONDS.sleep(500);  // programme logic:
            port.getSemaphore().acquire();            // using port semaphore we give an opportunity to do some actions only for 3 ships
            if (ship.getFreeSpaceOnBoard() == 0){     // at the same time (because there are only 3 docks in port)
                boolean loadAction;
                Random random = new Random();         // if ship is full:
                loadAction = random.nextBoolean();    // according to random boolean loadAction:
                    if (loadAction) {                 // we load or reload the ship.
                        reloadTheShip(ship, port);
                    }
                    else {
                    unloadTheShip(ship, port);
                }
            }
            if (ship.getFreeSpaceOnBoard() == ship.getShipMaxCapacity()){   // if ship is empty: we load the ship
                loadTheShip(ship, port);
            }
            else {                                                          // in other case we use random actionCase to choose the kind of action
                int actionCase = (int) (Math.random() * 3) + 1;
                switch (actionCase){
                    case 1: loadTheShip(ship, port); break;
                    case 2: unloadTheShip(ship, port); break;
                    case 3: reloadTheShip(ship, port); break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            port.getSemaphore().release();
            System.out.println(ship.toString() + " leaves the port.");
        }
    }

    public static void loadTheShip(Ship ship, Port port){
        boolean isSuccessfully = false;
        int containersToLoad = (int)(Math.random()* ship.getFreeSpaceOnBoard()+1);  // containers to load quantity is random, but not more, than ship's free space
        try {
            LOCK.lock();
            System.out.println(ship.toString() + ": needs to load " + containersToLoad + " containers, there are " + port.getContainersInPort() + " containers in port.");
            if (containersToLoad > port.getContainersInPort()){
                System.out.println(ship.getName() + ": NOT ENOUGH CONTAINERS IN PORT TO LOAD THE SHIP!");
                condition.await(5, TimeUnit.SECONDS);
                if (containersToLoad > port.getContainersInPort()){
                    System.out.println(ship.getName() + " LOAD FAILED! LEAVING WITHOUT CARGO");
                } else {
                    moveContainersFromPortToShip(ship, port,containersToLoad);
                    isSuccessfully = true;
                }
            } else {
                moveContainersFromPortToShip(ship, port,containersToLoad);
                isSuccessfully = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            condition.signalAll();
            LOCK.unlock();
            if (isSuccessfully){
                try {
                    TimeUnit.MILLISECONDS.sleep((long) containersToLoad * ONE_CONTAINER_LOADING_TIME);
                    System.out.println(ship.toString() + " finished loading!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void unloadTheShip(Ship ship, Port port){
        boolean isSuccessfully = false;
        int containersToUnload = (int)(Math.random()* ship.getContainersOnBoard()+1);
        try{
            LOCK.lock();
            System.out.println(ship.toString() + " needs to unload " + containersToUnload + " containers, there are " + port.getPortFreeSpace() + " free spaces in port.");
            if (containersToUnload > port.getPortFreeSpace()) {
                System.out.println(ship.getName() + ": NOT ENOUGH FREE SPACE IN PORT");
                condition.await(5, TimeUnit.SECONDS);
                if (containersToUnload > port.getPortFreeSpace()){
                    System.out.println(ship.getName() + ": UNLOAD FAILED! LEAVING WITH CARGO");
                } else {
                    moveContainersFromShipToPort(ship, port, containersToUnload);
                    isSuccessfully = true;
                }
            } else {
                moveContainersFromShipToPort(ship, port, containersToUnload);
                isSuccessfully = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            condition.signalAll();
            LOCK.unlock();
            if (isSuccessfully){
                try {
                    TimeUnit.MILLISECONDS.sleep((long) containersToUnload * ONE_CONTAINER_LOADING_TIME);
                    System.out.println(ship.toString() + " finished unloading");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void reloadTheShip(Ship ship, Port port){
        unloadTheShip(ship, port);
        loadTheShip(ship, port);
    }

    public static void moveContainersFromPortToShip(Ship ship, Port port, int containers){
        System.out.println(ship.getName() + " starts loading...");
        ship.setContainersOnBoard(ship.getContainersOnBoard() + containers);
        port.setContainersInPort(port.getContainersInPort() - containers);
    }

    public static void moveContainersFromShipToPort(Ship ship, Port port, int containers){
        System.out.println(ship.getName() + " starts unloading...");
        ship.setContainersOnBoard(ship.getContainersOnBoard() - containers);
        port.setContainersInPort(port.getContainersInPort() + containers);
    }
}
