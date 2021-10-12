import model.Port;
import model.Ship;
import service.ShipService;

public class Main {
    public static void main(String[] args) {
        Port port = new Port(3,1000,850); // creating port
        for (int i = 0; i < 10; i++){
            int shipMaxCapacity = (int) (Math.random() * 500) + 100;
            int containersOnBoard = (int) (Math.random() * shipMaxCapacity);
            final int shipIndex = i;
            new Thread(() -> {
                ShipService.doAction(new Ship("Ship " + (shipIndex + 1), shipMaxCapacity, containersOnBoard), port);
            }).start();
        }
    }
}
