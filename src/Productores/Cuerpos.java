package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Cuerpos extends Productor {
    public Cuerpos(int indice, Semaphore Se, Semaphore Sp, Semaphore Sc) {
        super(
            indice,
            Mattel.almacen_cuerpos,         // Buffer
            Mattel.capacidades_almacen[3],  // Capacidad
            Mattel.productos_por_dia[3],    // Producto/dia
            Se, Sp, Sc
        );
    }
}
