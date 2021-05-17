package Productores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Botones extends Productor {
    public Botones(Semaphore semaforo) {
        super("Botones", 60, 4f, semaforo);
    }
}
