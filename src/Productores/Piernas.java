package Productores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Piernas extends Productor {
    public Piernas(Semaphore semaforo) {
        super("Piernas", 36, 1f/2, semaforo);
    }
}
