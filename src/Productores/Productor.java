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
    
    public long tiempo_producir;    // Tiempo necesario para producir (ms)
    
    // Referencias a semaforos
    private Semaphore semaforo_exclusion;   // Para asegurar exclusion mutua al modiciar cantidad almacen
    private Semaphore semaforo_productor;   // Para saber cuantos espacios en almacen estan disponibles
    private Semaphore semaforo_consumidor;  // Para saber cuantos espacios en almacen estan ocupados
    
    public Productor(int indice) {

        // NOTA: la funcion sleep toma un long, estoy rounding up los ms y casting a long
        this.tiempo_producir = (long) Math.ceil(
            Mattel.segundos_por_dia * (1000/Mattel.productos_por_dia[indice])
        );
        
        this.semaforo_exclusion = Mattel.semaforos_exclusion[indice];
        this.semaforo_productor = Mattel.semaforos_productor[indice];
        this.semaforo_consumidor = Mattel.semaforos_consumidor[indice];
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                // Produciendo... / Durmiendo
                Thread.sleep(this.tiempo_producir);
                
                // Seccion Entrada
                this.semaforo_productor.acquire();      // Queremos ocupar espacio en alamacen
                this.semaforo_exclusion.acquire();      // Queremos entrar a SC
                
                // Seccion Critica
                this.almacenar();
                
                // Seccion Salida
                this.semaforo_exclusion.release();      // Se sale de de SC
                this.semaforo_consumidor.release();     // Se indica que un espacio se ocupo
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // Metodos abstractos
    protected abstract void almacenar();
}
