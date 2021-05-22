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
    
    public int almacen;             // Almacen de este producto (buffer)
    public int capacidad;           // Capacidad maxima de almacen
    public long tiempo_producir;    // Tiempo necesario para producir (ms)
    
    // Variables para debug
    public int indice;              // Para saber quien y cuendo un productor actua
    
    // Referencias a semaforos
    private Semaphore semaforo_exclusion;   // Para asegurar exclusion mutua al modiciar cantidad almacen
    private Semaphore semaforo_productor;   // Para saber cuantos espacios en almacen estan disponibles
    private Semaphore semaforo_consumidor;  // Para saber cuantos espacios en almacen estan ocupados
    
    public Productor(int indice, int almacen, int capacidad, float producto_por_dia, Semaphore Se, Semaphore Sp, Semaphore Sc) {
        this.indice = indice;
        
        this.almacen = almacen;
        this.capacidad = capacidad;
        // NOTA: la funcion sleep toma un long, estoy rounding up los ms y casting a long
        this.tiempo_producir = (long) Math.ceil((Mattel.segundos_por_dia * (1/producto_por_dia)) * 1000);
        
        this.semaforo_exclusion = Se;
        this.semaforo_productor = Sp;
        this.semaforo_consumidor = Sc;
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                // Produciendo...
                Thread.sleep(this.tiempo_producir);
                
                // Seccion Entrada
                this.semaforo_productor.acquire();      // Queremos ocupar espacio en alamacen
                this.semaforo_exclusion.acquire();      // Queremos entrar a SC
                
                // Seccion Critica
                this.producir();
                
                // Seccion Salida
                this.semaforo_exclusion.release();      // Se sale de de SC
                this.semaforo_consumidor.release();     // Se indica que un espacio se ocupo
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void producir() {
        this.almacen += 1;
    }
}
