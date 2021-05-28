package Productores;

import P1.Mattel;

/**
 *
 * @author Daniel & Rommel
 */
public class Cuerpos extends Productor {
    public Cuerpos() {
        super(Mattel.BUFFER.CUERPO.INDICE);
    }
    
    protected void almacenar() {
        Mattel.almacen_cuerpos += 1;
    }
}
