package P1;

import Consumidores.Ensamblador;
import Productores.Botones;
import Productores.Brazos;
import Productores.Cuerpos;
import Productores.Piernas;
import Productores.Productor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Daniel & Rommel 
 */
public class Mattel {
    
    // Datos Globales de Simulacion
    public static int segundos_por_dia;
    public static int dias_entre_despachos;

    public static int cantidad_ensambladores;
    public static int capacidad_ensambladores;
    
    /* Formato de arreglos:
     * i = 0 -> Botones
     * i = 1 -> Brazos
     * i = 2 -> Piernas
     * i = 3 -> Cuerpos
     * i = 4 -> Contador de dias
     * i = 5 -> Panas
     */
    
    public static enum BUFFER {
        BOTON(0),
        BRAZO(1),
        PIERNA(2),
        CUERPO(3),
        CONTADOR(4),
        PANAS(5);
    
        public int INDICE;
        
        BUFFER(int indice) {
            this.INDICE = indice;
        }
    }
    
    // Capacidades & Cantidades
    public static int[] capacidades_almacen = new int[4];
    public static int[] cantidad_productores = new int[4];
    public static int[] capacidades_productores = new int[4];
    public static float[] productos_por_dia = new float[4];
    
    // Semaforos
    public static Semaphore[] semaforos_exclusion = new Semaphore[BUFFER.values().length];          // Para enforcar la exclusion mutua
    public static Semaphore[] semaforos_productor = new Semaphore[BUFFER.values().length - 2];      // Para saber cuantos espacios en cada almacen estan disponibles
    public static Semaphore[] semaforos_consumidor = new Semaphore[BUFFER.values().length - 2];     // Para saber cuantos espacios en cada almacen estan ocupados
        
    
    // Almacenes (sudo-buffers):
    public static volatile int almacen_botones = 0;
    public static volatile int almacen_brazos = 0;
    public static volatile int almacen_piernas = 0;
    public static volatile int almacen_cuerpos = 0;
    public static volatile int almacen_panas = 0;
    public static volatile int contador_dias = 0;
  
    // TODO: como vamos a structurar estos arreglos?
    public static List<List<Productor>> productores;
    public static List<Ensamblador> ensambladores;
    
    public static void main(String[] args) {
        
        // Valores Iniciales:
        // TODO: inizializar estos datos via archivo de texto
        Mattel.segundos_por_dia = 20;
        Mattel.dias_entre_despachos = 10;

        Mattel.cantidad_ensambladores = 1;
        Mattel.capacidad_ensambladores = 5;
        
        int indice;
        
        // Para Boton i = 0
        indice = BUFFER.BOTON.INDICE;
        Mattel.cantidad_productores[indice] = 1;
        Mattel.capacidades_almacen[indice] = 60;
        Mattel.capacidades_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 4f;
        
        // Para Brazos i = 1
        indice = BUFFER.BRAZO.INDICE;
        Mattel.cantidad_productores[indice] = 1;
        Mattel.capacidades_almacen[indice] = 40;
        Mattel.capacidades_productores[indice] = 5;
        Mattel.productos_por_dia[indice] = 4f;
        
        // Para Piernas i = 2
        indice = BUFFER.PIERNA.INDICE;
        Mattel.cantidad_productores[indice] = 1;
        Mattel.capacidades_almacen[indice] = 36;
        Mattel.capacidades_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 4f;
        
        // Para Cuerpos i = 3
        indice = BUFFER.CUERPO.INDICE;
        Mattel.cantidad_productores[indice] = 1;
        Mattel.capacidades_almacen[indice] = 15;
        Mattel.capacidades_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 4f;
        
        // Inizializacion de Semaforos
        for (int i = 0; i < semaforos_exclusion.length; i++) {
            semaforos_exclusion[i] = new Semaphore(1);
        }
        
        for (int i = 0; i < semaforos_productor.length; i++) {
            semaforos_productor[i] = new Semaphore(Mattel.capacidades_almacen[i]);
        }
        
        for (int i = 0; i < semaforos_consumidor.length; i++) {
            semaforos_consumidor[i] = new Semaphore(0);
        }
        
        // Inizializando arreglos con cada productor/asemblador
        // NOTA: no le pongo size porque prefiero hacer las validaciones al momento que queremos agregar elemento
        productores = new ArrayList<List<Productor>>();
        ensambladores = new ArrayList<>();
        
        // Creando productores iniciales
        for (int i = 0; i < cantidad_productores.length; i++) {
            List producers = new ArrayList<Productor>();
            for (int j = 0; j < cantidad_productores[i]; j++) {
                switch(i) {
                    // Por alguna razon java no me deja utilizar indices del ENUM
                    case 0 -> producers.add(new Botones());
                    case 1 -> producers.add(new Brazos());
                    case 2 -> producers.add(new Piernas());
                    case 3 -> producers.add(new Cuerpos());
                }   
            }
            productores.add(producers);
        }
        
        // Loop para comenzar cada thread
        for (int i = 0; i < Mattel.productores.size(); i++) {
            List<Productor> productorArr = Mattel.productores.get(i);
            for (int j = 0; j < productorArr.size(); j++) {
                productorArr.get(j).start();
            }
        }
        
    }
}
