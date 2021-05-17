package Productores;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
