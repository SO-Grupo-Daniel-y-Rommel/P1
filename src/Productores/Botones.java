package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Botones extends Productor {
    public Botones(int indice, Semaphore Se, Semaphore Sp, Semaphore Sc) {
        super(
            indice,
            Mattel.almacen_botones,         // Buffer
            Mattel.capacidades_almacen[0],  // Capacidad
            Mattel.productos_por_dia[0],    // Producto/dia
            Se, Sp, Sc
        );
    }
}
