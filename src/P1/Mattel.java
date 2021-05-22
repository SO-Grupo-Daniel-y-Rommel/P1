package P1;

import Consumidores.Ensamblador;
import Productores.Productor;
import java.util.ArrayList;
import java.util.List;

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
     */ // NOTA: esto lo podemos cambiar para que sea un int por porductor si quieres
    public static int[] capacidades_almacen = new int[4];
    public static int[] cantidad_productores = new int[4];
    public static int[] capacidades_productores = new int[4];
    public static float[] productos_por_dia = new float[4];
    
    // Almacenes (sudo-buffers):
    public static volatile int almacen_botones = 0;
    public static volatile int almacen_brazos = 0;
    public static volatile int almacen_piernas = 0;
    public static volatile int almacen_cuerpos = 0;
  
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
        
        // Para Boton i = 0
        Mattel.capacidades_almacen[0] = 60;
        Mattel.cantidad_productores[0] = 1;
        Mattel.capacidades_productores[0] = 4;
        Mattel.productos_por_dia[0] = 4f;
        
        // Para Brazos i = 1
        Mattel.capacidades_almacen[1] = 40;
        Mattel.cantidad_productores[1] = 1;
        Mattel.capacidades_productores[1] = 5;
        Mattel.productos_por_dia[1] = 4f;
        
        // Para Piernas i = 2
        Mattel.capacidades_almacen[2] = 36;
        Mattel.cantidad_productores[2] = 1;
        Mattel.capacidades_productores[2] = 4;
        Mattel.productos_por_dia[2] = 4f;
        
        // Para Cuerpos i = 3
        Mattel.capacidades_almacen[3] = 15;
        Mattel.cantidad_productores[3] = 1;
        Mattel.capacidades_productores[3] = 4;
        Mattel.productos_por_dia[3] = 4f;
        
        // TODO: creacion de semaforos
        
        
        // TODO: Fill los arreglos con los productores iniciales
        // NOTA: no le pongo size porque prefiero hacer las validaciones al momento que queremos agregar elemento
        Mattel.productores = new ArrayList<>(4);
        Mattel.ensambladores = new ArrayList<>();

        // Rutinas para comenzar cada thread
        for (int i = 0; i < Mattel.productores.size(); i++) {
            List<Productor> productorArr = Mattel.productores.get(i);
            for (int j = 0; j < productorArr.size(); j++) {
                productorArr.get(j).start();
            }
        }
        
    }
}
