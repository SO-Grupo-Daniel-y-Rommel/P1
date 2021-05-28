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
        PANAS(4),
        CONTADOR(5);
    
        public int INDICE;
        
        BUFFER(int indice) {
            this.INDICE = indice;
        }
    }
    
    // Capacidades, Cantidades & Requisitos
    public static int cantidad_ensambladores;
    public static int capacidad_ensambladores;
    public static float dias_para_asemblar;
    public static int[] capacidad_almacenes = new int[4];
    public static int[] cantidad_productores = new int[4];
    public static int[] capacidad_productores = new int[4];
    public static float[] productos_por_dia = new float[4];
    public static int[] unidades_requeridas = new int[4];
    
    // Semaforos
    public static Semaphore[] semaforos_exclusion = new Semaphore[BUFFER.values().length];          // Para enforcar la exclusion mutua
    public static Semaphore[] semaforos_productor = new Semaphore[BUFFER.values().length - 2];      // Para saber cuantos espacios en cada almacen estan disponibles
    public static Semaphore[] semaforos_consumidor = new Semaphore[BUFFER.values().length];         // Para saber cuantos espacios en cada almacen estan ocupados
        
    
    // Almacenes (sudo-buffers): 
    // NOTA: es posible cambiar esto para que use AtomicArrays para hacer un set de valores volatiles
    public static volatile int almacen_botones = 0;
    public static volatile int almacen_brazos = 0;
    public static volatile int almacen_piernas = 0;
    public static volatile int almacen_cuerpos = 0;
    public static volatile int almacen_panas = 0;
    public static volatile int contador_dias = 0;
  
    // NOTA: intente hacer esto con arreglos pero arreglo de java no son dinamicos :(
    public static List<List<Productor>> productores;
    public static List<Ensamblador> ensambladores;
    
    public static void main(String[] args) {
        
        
        // Valores Iniciales:
        // TODO: inizializar estos datos via archivo de texto
        Mattel.segundos_por_dia = 5;
        Mattel.dias_entre_despachos = 10;

        Mattel.cantidad_ensambladores = 2;
        Mattel.capacidad_ensambladores = 5;
        Mattel.dias_para_asemblar = 1f;
        
        
        int indice;
        
        // Para Boton i = 0
        indice = BUFFER.BOTON.INDICE;
        Mattel.cantidad_productores[indice] = 2;
        Mattel.capacidad_almacenes[indice] = 60;
        Mattel.capacidad_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 4f;
        Mattel.unidades_requeridas[indice] = 8;
        
        // Para Brazos i = 1
        indice = BUFFER.BRAZO.INDICE;
        Mattel.cantidad_productores[indice] = 4;
        Mattel.capacidad_almacenes[indice] = 40;
        Mattel.capacidad_productores[indice] = 5;
        Mattel.productos_por_dia[indice] = 1f;
        Mattel.unidades_requeridas[indice] = 2;
        
        // Para Piernas i = 2
        indice = BUFFER.PIERNA.INDICE;
        Mattel.cantidad_productores[indice] = 2;
        Mattel.capacidad_almacenes[indice] = 36;
        Mattel.capacidad_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 1f/2;
        Mattel.unidades_requeridas[indice] = 2;
        
        // Para Cuerpos i = 3
        indice = BUFFER.CUERPO.INDICE;
        Mattel.cantidad_productores[indice] = 5;
        Mattel.capacidad_almacenes[indice] = 15;
        Mattel.capacidad_productores[indice] = 4;
        Mattel.productos_por_dia[indice] = 1f/3;
        Mattel.unidades_requeridas[indice] = 1;
        
        // Inizializacion de Semaforos
        for (int i = 0; i < semaforos_exclusion.length; i++) {
            semaforos_exclusion[i] = new Semaphore(1);
        }
        
        for (int i = 0; i < semaforos_productor.length; i++) {
            semaforos_productor[i] = new Semaphore(Mattel.capacidad_almacenes[i]);
        }
        
        for (int i = 0; i < semaforos_consumidor.length; i++) {
            semaforos_consumidor[i] = new Semaphore(0);
        }
        
        // Inizializando arreglos con cada productor/asemblador
        productores = new ArrayList<List<Productor>>();
        ensambladores = new ArrayList<>();
        
        // Creando productores iniciales
        for (int i = 0; i < cantidad_productores.length; i++) {
            List producers = new ArrayList<Productor>();
            for (int j = 0; j < cantidad_productores[i]; j++) {
                switch(i) {
                    // Por alguna razon java no me deja utilizar indices del ENUM
                    case 0 : producers.add(new Botones()); 
                    case 1 : producers.add(new Brazos());
                    case 2 : producers.add(new Piernas());
                    case 3 : producers.add(new Cuerpos());
                }   
            }
            productores.add(producers);
        }
        
        // Creando asembladores inicial
        for (int i = 0; i < cantidad_ensambladores; i++) {
            ensambladores.add(new Ensamblador());
        }
        
        // Comenzamos cada thread
        for (int i = 0; i < Mattel.productores.size(); i++) {   // Productores
            List<Productor> productorArr = Mattel.productores.get(i);
            for (int j = 0; j < productorArr.size(); j++) {
                productorArr.get(j).start();
            }
        }
       
        for (int i = 0; i < Mattel.ensambladores.size(); i++) {  // Ensambladores
            ensambladores.get(i).start();
        }
        
    }
}
