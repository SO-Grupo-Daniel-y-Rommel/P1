package Productores;

import P1.Mattel;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel & Rommel
 */
public abstract class Productor extends Thread {
    
    public int almacen;      // Almacen de este producto (buffer)
    public int capacidad;           // Capacidad maxima de almacen
    public float tiempo_producir;   // Tiempo necesario para producir (ms)
    
    private Semaphore semaforo;
    
    public Productor(int almacen, int capacidad, float producto_por_dia, Semaphore semaforo) {
        this.almacen = almacen;
        this.capacidad = capacidad;
        this.tiempo_producir = (Mattel.segundos_por_dia * (1/producto_por_dia)) * 1000;
        
        this.semaforo = semaforo;
    }
    
    public void run() {
        while(true) {
            try{
                this.semaforo.acquire();
                
                // Seccion Critica
                
                this.semaforo.release();
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
