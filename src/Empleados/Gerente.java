package Empleados;

import Consumidores.Ensamblador;
import P1.Mattel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel & Rommel
 */
public class Gerente extends Thread{
    
    public long tiempo_dormir = (long) Math.ceil(
            Mattel.segundos_por_dia * 1000 * Mattel.horas_por_dia_dormir_gerente
    );
    
    public Gerente(){ }
    
    @Override
    public void run() {
        while(true) {
            try{
                
                despertar();
                
                //Seccion Entrada
                Mattel.semaforos_exclusion[Mattel.BUFFER.CONTADOR.INDICE].acquire();
                Mattel.semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].acquire();
                
                //Seccion Critica
                if (Mattel.contador_dias == 0){
                    realizar_entrega();
                    //Volver a esperar 8 dias para la realizar una entrega
                    reiniciar_dias_para_entrega();
                }
                
                //Seccion Salida
                Mattel.semaforos_exclusion[Mattel.BUFFER.PANAS.INDICE].release();
                Mattel.semaforos_exclusion[Mattel.BUFFER.CONTADOR.INDICE].release();

                // Seccion Restante
                dormir();
                
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void realizar_entrega() throws InterruptedException{
        Mattel.gerente_estado = "Entregando Panas";
        int n_panas = Mattel.almacen_panas;
        Mattel.almacen_panas = 0;
        Thread.sleep(Mattel.tiempo_entrega);
        Mattel.panas_entregados += n_panas;
    }
    
    public void reiniciar_dias_para_entrega() {
        Mattel.contador_dias = Mattel.dias_entre_despachos;
    }
    
    public void dormir() throws InterruptedException{
        Mattel.gerente_estado = "Durmiendo";
        Thread.sleep(tiempo_dormir);
    }
    
    public void despertar() throws InterruptedException{
        Mattel.gerente_estado = "Intentando Leer Contador";
        
    }
    
    
   
    
}
