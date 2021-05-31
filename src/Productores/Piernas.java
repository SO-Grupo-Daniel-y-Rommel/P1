package Productores;

import imagines.Productor;
import P1.Mattel;

/**
 *
 * @author Daniel & Rommel
 */
public class Piernas extends Productor {
    public Piernas() {
        super(Mattel.BUFFER.PIERNA.INDICE);
    }
    
    protected void almacenar() {
        Mattel.almacen_piernas += 1;
    }
}
