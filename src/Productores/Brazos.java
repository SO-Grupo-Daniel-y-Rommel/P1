package Productores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Brazos extends Productor {
    public Brazos(Semaphore semaforo) {
        super("Brazos", 40, 1f, semaforo);
    }
}
