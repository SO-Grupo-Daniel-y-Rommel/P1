package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Brazos extends Productor {
    public Brazos(int indice, Semaphore Se, Semaphore Sp, Semaphore Sc) {
        super(
            indice,
            Mattel.almacen_brazos,          // Buffer
            Mattel.capacidades_almacen[1],  // Capacidad
            Mattel.productos_por_dia[1],    // Producto/dia
            Se, Sp, Sc
        );
    }
}
