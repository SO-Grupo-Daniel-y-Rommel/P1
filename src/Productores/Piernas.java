package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Piernas extends Productor {
    public Piernas(int indice, Semaphore Se, Semaphore Sp, Semaphore Sc) {
        super(
            indice,
            Mattel.almacen_piernas,         // Buffer
            Mattel.capacidades_almacen[2],  // Capacidad
            Mattel.productos_por_dia[2],    // Producto/dia
            Se, Sp, Sc
        );
    }
}
