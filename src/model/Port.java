package model;

import java.util.concurrent.Semaphore;

public class Port {
    private int portMaxCapacity;
    private int containersInPort;
    private int docksInPort;
    private Semaphore semaphore;

    public int getPortMaxCapacity() {
        return portMaxCapacity;
    }

    public void setPortMaxCapacity(int portMaxCapacity) {
        this.portMaxCapacity = portMaxCapacity;
    }

    public int getContainersInPort() {
        return containersInPort;
    }

    public void setContainersInPort(int containersInPort) {
        this.containersInPort = containersInPort;
    }

    public int getDocksInPort() {
        return docksInPort;
    }

    public void setDocksInPort(int docksInPort) {
        this.docksInPort = docksInPort;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Port(int docksInPort, int portMaxCapacity, int containersInPort) {
        this.docksInPort = docksInPort;
        this.portMaxCapacity = portMaxCapacity;
        this.containersInPort = containersInPort;
        semaphore = new Semaphore(docksInPort);
    }

    public int getPortFreeSpace() {
        return portMaxCapacity - containersInPort;
    }
}
