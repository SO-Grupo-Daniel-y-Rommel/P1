package Productores;

import P1.Mattel;
import java.util.List;
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
    
    public long tiempo_producir;    // Tiempo necesario para producir (ms)
    private int indice;
    public boolean despedido = false;
    
    // vars para debug
    protected static int[] counterIDs =  { 1, 1, 1, 1 };
    private int id;
    
    // Referencias a semaforos
    private Semaphore semaforo_exclusion;   // Para asegurar exclusion mutua al modiciar cantidad almacen
    private Semaphore semaforo_productor;   // Para saber cuantos espacios en almacen estan disponibles
    private Semaphore semaforo_consumidor;  // Para saber cuantos espacios en almacen estan ocupados
    
    public Productor(int indice) {
        this.indice = indice;
        
        id = counterIDs[indice];
        counterIDs[indice] += 1;

        // NOTA: la funcion sleep toma un long, estoy rounding up los ms y casting a long
        tiempo_producir = (long) Math.ceil(
            Mattel.segundos_por_dia * (1000/Mattel.productos_por_dia[indice])
        );
        
//        System.out.println(getClass().getName().split("\\.")[1] + "#" + String.valueOf(id) + " Tiempo para producir: " + tiempo_producir + "ms");
        
        this.semaforo_exclusion = Mattel.semaforos_exclusion[indice];
        this.semaforo_productor = Mattel.semaforos_productor[indice];
        this.semaforo_consumidor = Mattel.semaforos_consumidor[indice];
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                // Produciendo...
                producir();
                
                // Seccion Entrada
                semaforo_productor.acquire();      // Queremos ocupar espacio en alamacen
                semaforo_exclusion.acquire();      // Queremos entrar a SC
                
                // Seccion Critica
                almacenar();
//                printChange();
                
                // Seccion Salida
                semaforo_exclusion.release();      // Se sale de de SC
                semaforo_consumidor.release();     // Se indica que un espacio se ocupo
                
                // Seccion Restante
                if (this.despedido) this.retirarse();
                
            } catch(InterruptedException ex) {
                getLogger(Productor.class.getName()).log(SEVERE, null, ex);
            }
        }
    }
    
    protected void producir() throws InterruptedException {
        Thread.sleep(tiempo_producir);
    }
    
    protected void printChange() {
        String productor = getClass().getName().split("\\.")[1];
        int value = 0;
        
        if (productor.equals("Botones")) value = Mattel.almacen_botones;
        if (productor.equals("Brazos")) value = Mattel.almacen_brazos;
        if (productor.equals("Piernas")) value = Mattel.almacen_piernas;
        if (productor.equals("Cuerpos")) value = Mattel.almacen_cuerpos;
        
        System.out.println(productor + " #" + String.valueOf(id) + " | " + "Almacen de " + productor + ": " + String.valueOf(value));
    }
    
    public void despedir() {
        this.despedido = true;
    }
    
    public void retirarse(){
        // Lo quitamos del arreglo
        Mattel.productores.get(this.indice).remove(0);
        
        // Mensaje
        String productor = getClass().getName().split("\\.")[1];
        System.out.println("Productor #" + String.valueOf(id) + " se retiro");
        
        // paramos Ejecucion del hilo
        this.stop();
    }
    
    // Metodos abstractos
    protected abstract void almacenar();
}
