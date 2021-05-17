package P1;

import Ensambladores.Ensamblador;
import Productores.Productor;

/**
 *
 * @author Daniel & Rommel 
 */
public class Mattel {
    
    // Datos Globales de Simulacion
    public static int segundos_por_dia;
    public static int dias_entre_despachos;

    public static int cantidad_ensambladores;
    public static int cantidad_maxima_ensambladores;
    
    /* Formato de arreglos:
     * i = 0 -> Botones
     * i = 1 -> Brazos
     * i = 2 -> Piernas
     * i = 3 -> Cuerpos
     */
    public static int[] cantidades_almacen = new int[4];
    public static int[] cantidad_productores = new int[4];
    public static int[] cantidad_maxima_productores = new int[4];
    
    public static volatile Productor[] productores;
    public static volatile Ensamblador[] ensambladores;
    
    public static void main(String[] args) {
        
        // Valores Iniciales:
        // TODO: inizializar estos datos via archivo de texto
        Mattel.segundos_por_dia = 20;
        Mattel.dias_entre_despachos = 10;

        Mattel.cantidad_ensambladores = 1;
        Mattel.cantidad_maxima_ensambladores = 5;
        
        // Para Boton i = 0
        Mattel.cantidades_almacen[0] = 60;
        Mattel.cantidad_productores[0] = 1;
        Mattel.cantidad_maxima_productores[0] = 4;
        
        // Para Brazos i = 1
        Mattel.cantidades_almacen[1] = 40;
        Mattel.cantidad_productores[1] = 1;
        Mattel.cantidad_maxima_productores[1] = 5;
        
        // Para Piernas i = 2
        Mattel.cantidades_almacen[2] = 36;
        Mattel.cantidad_productores[2] = 1;
         Mattel.cantidad_maxima_productores[2] = 4;
        
        // Para Cuerpos i = 3
        Mattel.cantidades_almacen[3] = 15;
        Mattel.cantidad_productores[3] = 1;
        Mattel.cantidad_maxima_productores[3] = 4;

        
        // TODO: Fill los arreglos con los datos dados
//        Mattel.productores;
//        Mattel.ensambladores;

        // Rutinas para comenzar cada thread
        for (int i = 0; i < Mattel.productores.length; i++) {
            Mattel.productores[i].start();
        }
        
    }
}
