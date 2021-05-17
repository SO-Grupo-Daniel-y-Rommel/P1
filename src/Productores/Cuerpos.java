package Productores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Cuerpos extends Productor {
    public Cuerpos(Semaphore semaforo) {
        super("Cuerpos", 15, 1f/3, semaforo);
    }
}
