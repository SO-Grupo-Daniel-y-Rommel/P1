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
    
    
    public String estado;
    
    // Constantes ??????????????????????????
//    public float tiempo_para_decrementar = 8f/24;   // 8 horas cambiando decrementando contador
//    public float tiempo_en_reposo = 24f - this.tiempo_para_decrementar; // resto del dia en reposo
 
    //Semaforo
    private Semaphore semaforo_exclusion_jefeGerente = Mattel.semaforo_exclusion_dias;    // Para asegurar exclusion mutua al modiciar el contador de los dias
    
    
    public Jefe(){
        
        
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                // Seccion Entrada
                semaforo_exclusion_jefeGerente.acquire();
                
                
                //Seccion critica
                // ===> pasar el dia
                pasar_dias();
                
                
                //Seccion Salida
                semaforo_exclusion_jefeGerente.release();
                dormir();
                
                
                
                
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void dormir() throws InterruptedException{
        Mattel.chief_status="Dormido";
        Thread.sleep(Mattel.tiempo_dormir);
    }
    
    
    public void pasar_dias() throws InterruptedException{
        Mattel.chief_status="Activo";
        Mattel.contador_dias += 1;
        Mattel.dias_restantes -= 1;
        Thread.sleep(Mattel.tiempo_chief_pasar_dias);
        
    }
    
    
    
    
}
