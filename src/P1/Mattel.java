package P1;

import Ensambladores.Ensamblador;
import Productores.Productor;
import java.io.File;
import java.io.FileWriter;
import static java.lang.System.out;
import java.util.Scanner;

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

        
        var frame1=new Inicio();
        frame1.setVisible(true);
        String data=Lectura_Txt();
        System.out.println(data);
        
        
        
        // Valores Iniciales:
        // TODO: inizializar estos datos via archivo de texto
//        Mattel.segundos_por_dia = 20;
//        Mattel.dias_entre_despachos = 10;
//
//        Mattel.cantidad_ensambladores = 1;
//        Mattel.cantidad_maxima_ensambladores = 5;
//        
//        // Para Boton i = 0
//        Mattel.cantidades_almacen[0] = 60;
//        Mattel.cantidad_productores[0] = 1;
//        Mattel.cantidad_maxima_productores[0] = 4;
//        
//        // Para Brazos i = 1
//        Mattel.cantidades_almacen[1] = 40;
//        Mattel.cantidad_productores[1] = 1;
//        Mattel.cantidad_maxima_productores[1] = 5;
//        
//        // Para Piernas i = 2
//        Mattel.cantidades_almacen[2] = 36;
//        Mattel.cantidad_productores[2] = 1;
//         Mattel.cantidad_maxima_productores[2] = 4;
//        
//        // Para Cuerpos i = 3
//        Mattel.cantidades_almacen[3] = 15;
//        Mattel.cantidad_productores[3] = 1;
//        Mattel.cantidad_maxima_productores[3] = 4;
//
//        
//        // TODO: Fill los arreglos con los datos dados
////        Mattel.productores;
////        Mattel.ensambladores;
//
//        // Rutinas para comenzar cada thread
//        for (int i = 0; i < Mattel.productores.length; i++) {
//            Mattel.productores[i].start();
//        }
        
    }
    
    
    public static String Lectura_Txt(){
                //ARCHIVO DE TEXTO DE LA FORMA:
                //Productores de piernas=5
                //Productores de brazos=4
                //Productores de cuerpo central=3
                //Productores de botones=2
                //Ensambladores=1
                //Tiempo=10

        
        	// Fichero del que queremos leer
		var fichero = new File("C:\\Users\\DELL\\Desktop\\TRIMESTRES METROPOLITANA\\10MO TRIMESTRE\\SO\\Proyecto1\\src\\P1\\Data.txt");
		Scanner s = null;
                   
		try {
			// Leemos el contenido del fichero
			s = new Scanner(fichero);
                        
                        var linea="";
			// Leemos linea a linea el fichero
			while (s.hasNextLine()) {
                                linea=linea+"\n"+s.nextLine(); //se concatena cada linea en el string
                             	// Guardamos la linea en un String
			}
                        
                        //modificacion a la data del txt para utilizar la informacion para el sistema
                        var data=linea.split("\n"); //QUITAR TODOS LOS SALTOS DE LINEA
                        
                        char [] cadena;
                        var projectInfo="";
                        for (var i = 0; i < data.length; i++) {
                            data[i]=data[i].replace(" ", "");
                            cadena=data[i].toCharArray();
                            
                            
                            if (cadena.length>0){
                                var equal_index=data[i].indexOf("=");
                                var j=equal_index+2;
//                                System.out.println(cadena[j]);
                                while(j!=cadena.length+1){
                                    projectInfo=projectInfo+cadena[j-1];
                                    j++;
                                }
                                if (i+1!=data.length) {
                                    projectInfo=projectInfo+",";
                                }
                            }
                            
                           
                            
                        }
                        
                        return projectInfo;

		} catch (Exception ex) {
			out.println("Mensaje: " + ex.getMessage());
		} finally {
			// Cerramos el fichero tanto si la lectura ha sido correcta o no
			try {
				if (s != null)
					s.close();
			} catch (Exception ex2) {
				out.println("Mensaje 2: " + ex2.getMessage());
			}
		}
        return null;
	}
    
    
    
    public static void Escritura_Txt(String p_piernas,String p_brazos, String p_cuerpoC,String p_botones,String ensambladores,String tiempo){
        
        String[] info = { "Productores de piernas", "Productores de brazos","Productores de cuerpo central", "Productores de botones","Ensambladores","Tiempo"};
        String[] value = { p_piernas,p_brazos, p_cuerpoC, p_botones, ensambladores, tiempo};
        for (int i = 0; i < value.length; i++) {
            System.out.println(value[i]);
        }
		/** FORMA 1 DE ESCRITURA **/
		
		try {

            // Escribimos linea a linea en el fichero
            try (var fichero = new FileWriter("C:\\Users\\DELL\\Desktop\\TRIMESTRES METROPOLITANA\\10MO TRIMESTRE\\SO\\Proyecto1\\src\\P1\\Data.txt")) {
                var flag = true;
                for (String value1 : value) {
                    if (value1.matches("[+-]?\\d*(\\.\\d+)?")) {
                        continue;
                    } else {
                        flag=false;
                        break;
                    }
                } 
                if (flag) {
                    if (info.length==value.length) {
                        for (var i = 0; i < info.length; i++) {
                            fichero.write(info[i]+"="+value[i]+"\n");
                        }
                    } else {
                        out.println("DATA INVALIDA");
                    }
                }
            }

		} catch (Exception ex) {
			out.println("Mensaje de la excepción: " + ex.getMessage());
		}
        
	}
    
    public static void cargar_data_default_txt(){
        
        Escritura_Txt("5","4", "2", "4", "3", "2");
        
    }
}

