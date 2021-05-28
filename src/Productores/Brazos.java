package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Brazos extends Productor {
    public Brazos() {
        super(Mattel.BUFFER.BRAZO.INDICE);
    }
    
    protected void almacenar() {
        Mattel.almacen_brazos += 1;
    }
}
