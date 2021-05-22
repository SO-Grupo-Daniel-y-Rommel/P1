package Consumidores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel
 */
public class Ensamblador {
    
    public int dias_para_producir = 1;
    public int producidos = 0;
    
    private Semaphore[] semaforos_exclusion;   // Para asegurar exclusion mutua al modiciar cantidadades almacen
    private Semaphore[] semaforos_productor;   // Para saber cuantos espacios en cada almacen estan disponibles
    private Semaphore[] semaforos_consumidor;  // Para saber cuantos espacios en cada almacen estan ocupados
    
}
