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
public class Gerente extends Thread{
    
    public Jefe jefe;
    
    public float tiempo_en_reposo = 8f/24;  // 8 horas
    
    
    
    
    //Semaforo
    private Semaphore semaforo_exclusion_jefeGerente = Mattel.semaforo_exclusion_dias;    // Para asegurar exclusion mutua al modiciar el contador de los dias
    
    
    public Gerente(){
        
       
    }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                
                
                //Seccion Entrada
                despertar(); 
                semaforo_exclusion_jefeGerente.acquire();
                Mattel.semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].acquire();
                   
                
                if(Mattel.dias_restantes==0){
                    
                    
                    
                    
                    //Seccion Critica
                    realizar_entrega();
                    
                    
                    //Volver a esperar 8 dias para la realizar una entrega
                    reiniciar_dias_para_entrega();
                
                    
                }
                
                
                //Seccion Salida
                Mattel.semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].release();
                semaforo_exclusion_jefeGerente.release();

                
                dormir();
                
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void realizar_entrega() throws InterruptedException{
        Mattel.ger_status="Activo";
        int n_panas=Mattel.almacen_panas;
        Mattel.almacen_panas=0;
        Thread.sleep(Mattel.tiempo_entrega);
        Mattel.panas_entregados+=n_panas;
    }
    
    public void reiniciar_dias_para_entrega() {
        Mattel.dias_restantes = 8;
    }
    
    public void dormir() throws InterruptedException{
        Mattel.ger_status="Dormido";
        Thread.sleep(Mattel.tiempo_dormir);
    }
    
    public void despertar() throws InterruptedException{
        Mattel.ger_status="Activo";
//        Thread.sleep(3);
        
    }
    
    
   
    
}
