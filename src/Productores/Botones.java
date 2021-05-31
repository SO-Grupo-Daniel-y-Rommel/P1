package Productores;

import P1.Mattel;

/**
 *
 * @author Daniel & Rommel
 */
public class Botones extends Productor {
    public Botones() {
        super(Mattel.BUFFER.BOTON.INDICE);
    }
    
    protected void almacenar() {
        Mattel.almacen_botones += 1;
    }
}
