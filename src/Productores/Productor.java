package Productores;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Daniel & Rommel
 */
public abstract class Productor extends Thread {
    
    public String producto;    
    public int almacenamiento;
    public float producto_por_dia;
    
    private Semaphore semaforo;
    
    public Productor(String producto, int almacenamiento, float producto_por_dia, Semaphore semaforo) {
        this.producto = producto;
        this.almacenamiento = almacenamiento;
        this.producto_por_dia = producto_por_dia;
        this.semaforo = semaforo;
    }
    
    public void run() {
        while(true) {
            try{
                this.semaforo.acquire();
                
                // Seccion Critica
                
                this.semaforo.release();
                
            } catch(InterruptedException ex) {
                getLogger(Productor.class.getName()).log(SEVERE, null, ex);
            }
        }
    }
}
