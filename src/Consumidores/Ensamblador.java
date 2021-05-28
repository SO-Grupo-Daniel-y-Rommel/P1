package Consumidores;

import P1.Mattel;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel & Rommel
 */
public class Ensamblador extends Thread {
    
    private static int counterID = 1;
    private int id;
    
    private long tiempo_asemblar;
    
    private Semaphore[] semaforos_exclusion = new Semaphore[5];    // Para asegurar exclusion mutua al modiciar cantidadades almacen
    private Semaphore[] semaforos_productor = new Semaphore[4];    // Para saber cuantos espacios en cada almacen estan disponibles
    private Semaphore[] semaforos_consumidor = new Semaphore[5];   // Para saber cuantos espacios en cada almacen estan ocupados
    
    public Ensamblador() {
        id = counterID;
        counterID += 1;
        
        tiempo_asemblar = (long) Math.ceil(
            Mattel.segundos_por_dia * (1000/Mattel.dias_para_asemblar)
        );
        
        System.out.println("hi");
        
        // Semaforos necesarios
        for (int i = 0; i < this.semaforos_exclusion.length; i++) {
            this.semaforos_exclusion[i] = Mattel.semaforos_exclusion[i];
        }
        for (int i = 0; i < this.semaforos_productor.length; i++) {
            this.semaforos_productor[i] = Mattel.semaforos_productor[i];
        }
        for (int i = 0; i < this.semaforos_consumidor.length; i++) {
            this.semaforos_consumidor[i] = Mattel.semaforos_consumidor[i];
        }
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                // Seccion Entrada
                int unidades;
                
                for (int i = 0; i < Mattel.productores.size(); i++) {   // Primero vemos si existe suficientes partes para ensamblar un Pana
                    unidades = Mattel.unidades_requeridas[i];
                    semaforos_consumidor[i].acquire(unidades);
                }
                for (int i = 0; i < Mattel.productores.size(); i++) {   // Ahora reservamos exclusividad en cada almacen
                    semaforos_exclusion[i].acquire();
                }
                
                // Seccion Critica
                recolectar();
                
                // Seccion Salida
                for (int i = 0; i < Mattel.productores.size(); i++) {   // Liberamos Exclusividad
                    semaforos_exclusion[i].release();
                }
                for (int i = 0; i < Mattel.productores.size(); i++) {   // Anunciamos que buffers tienen espacios desocupados
                    unidades = Mattel.unidades_requeridas[i];
                    semaforos_productor[i].release(unidades);
                }
                
                // Seccion Restante
                asemblar();
                
                // NOTA: como el asemblador es un productor y consumidor, tenemos varias secciones criticas
                
                // Seccion Entrada
                semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].acquire();
                
                // Seccion Critica
                almacenar();
                
                // Seccion Salidad
                semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].release();
                semaforos_consumidor[Mattel.BUFFER.PANAS.INDICE].release();
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void recolectar() {
        Mattel.almacen_botones -= Mattel.unidades_requeridas[Mattel.BUFFER.BOTON.INDICE];
        Mattel.almacen_brazos -= Mattel.unidades_requeridas[Mattel.BUFFER.BRAZO.INDICE];
        Mattel.almacen_piernas -= Mattel.unidades_requeridas[Mattel.BUFFER.PIERNA.INDICE];
        Mattel.almacen_cuerpos -= Mattel.unidades_requeridas[Mattel.BUFFER.CUERPO.INDICE];
        
        
        System.out.println("\n" + getClass().getName().split("\\.")[1] + " #" + id + " recolecto partes para pana!");
        System.out.println("Almacen Botones: " + Mattel.almacen_botones);
        System.out.println("Almacen Brazos: " + Mattel.almacen_brazos);
        System.out.println("Almacen Botones: " + Mattel.almacen_piernas);
        System.out.println("Almacen Botones: " + Mattel.almacen_cuerpos + "\n");
        
    }
    
    private void asemblar() throws InterruptedException {
        Thread.sleep(tiempo_asemblar);
    }
    
    private void almacenar() {
        Mattel.almacen_panas += 1;
        System.out.println("\n" + getClass().getName().split("\\.")[1] + " #" + id + " almaceno pana!");
        System.out.println("Almacen Panas: " + Mattel.almacen_panas + "\n");
    } 
    
}
