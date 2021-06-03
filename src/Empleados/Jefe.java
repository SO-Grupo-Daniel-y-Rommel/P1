package Empleados;

import Consumidores.Ensamblador;
import P1.Mattel;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel & Rommel
 */
public class Jefe extends Thread{
    
    
    public long tiempo_dormir = (long) Math.ceil(
            Mattel.segundos_por_dia * 1000 * Mattel.horas_por_dia_dormir_jefe
    );
    

    public long tiempo_pasar_dia = (long) Math.ceil(
            Mattel.segundos_por_dia * 1000 * Mattel.horas_por_dia_decrementar_contador
    );
    
  
    public Jefe(){
        
        
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                // Seccion Entrada
                Mattel.semaforos_exclusion[Mattel.BUFFER.CONTADOR.INDICE].acquire();
                
                
                //Seccion critica
                // ===> pasar el dia
                pasar_dias();
                
                
                //Seccion Salida
                Mattel.semaforos_exclusion[Mattel.BUFFER.CONTADOR.INDICE].release();
                
                // Seccion Restante
                dormir();
                
                
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void dormir() throws InterruptedException{
        Mattel.jefe_estado = "Durmiendo";
        Thread.sleep(tiempo_dormir);
    }
    
    
    public void pasar_dias() throws InterruptedException{
        Mattel.jefe_estado = "Cambiando counter";
        Thread.sleep(tiempo_pasar_dia);
        Mattel.dias_transcurridos += 1;
        Mattel.contador_dias -= 1;
    }
    
    
    
    
}
