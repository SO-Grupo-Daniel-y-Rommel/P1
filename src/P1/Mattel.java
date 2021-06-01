package P1;

import Consumidores.Ensamblador;
import Empleados.Gerente;
import Empleados.Jefe;
import Productores.Botones;
import Productores.Brazos;
import Productores.Cuerpos;
import Productores.Piernas;
import Productores.Productor;
import java.io.File;
import java.io.FileWriter;
import static java.lang.System.out;
import java.util.Scanner;
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
    


//  Variables de jefe y gerente   
    public static Semaphore semaforo_exclusion_dias=new Semaphore(1);
    public static volatile int contador_dias = 0;
    public static volatile int dias_restantes=8;
    public static volatile int panas_entregados=0;
    public static volatile String chief_status="Dormido";
    public static volatile String ger_status="Dormido";
    public static volatile int tiempo_entrega = 8000;
    public static volatile int tiempo_dormir = 8000;
    public static volatile int tiempo_chief_pasar_dias = 8000;
    
    
    
    // NOTA: intente hacer esto con arreglos pero arreglo de java no son dinamicos :(
    public static List<List<Productor>> productores;
    public static List<Ensamblador> ensambladores;
    
    public static void main(String[] args) {

        
        
        var frame1=new Inicio();
        frame1.setVisible(true);
        
        
//        =================================================================
 
   
    }

    
    public static void simulacion(){
        
        String data=Lectura_Txt();
        System.out.println(data);
        String [] info=data.split(",");
        
        
        // Valores Iniciales:
        Mattel.segundos_por_dia = Integer.parseInt(info[5]);
        Mattel.dias_entre_despachos = Integer.parseInt(info[6]);

        Mattel.cantidad_ensambladores = Integer.parseInt(info[4]);
        Mattel.capacidad_ensambladores = Integer.parseInt(info[11]);
        Mattel.dias_para_asemblar = 1f;
        
        
        int indice;
        
        // Para Boton i = 0
        indice = BUFFER.BOTON.INDICE;
        Mattel.cantidad_productores[indice] = Integer.parseInt(info[3]);
        Mattel.capacidad_almacenes[indice] = 60;
        Mattel.capacidad_productores[indice] = Integer.parseInt(info[10]);
        Mattel.productos_por_dia[indice] = 4f;
        Mattel.unidades_requeridas[indice] = 8;
        
        // Para Brazos i = 1
        indice = BUFFER.BRAZO.INDICE;
        Mattel.cantidad_productores[indice] = Integer.parseInt(info[1]);
        Mattel.capacidad_almacenes[indice] = 40;
        Mattel.capacidad_productores[indice] = Integer.parseInt(info[8]);
        Mattel.productos_por_dia[indice] = 1f;
        Mattel.unidades_requeridas[indice] = 2;
        
        // Para Piernas i = 2
        indice = BUFFER.PIERNA.INDICE;
        Mattel.cantidad_productores[indice] = Integer.parseInt(info[0]);
        Mattel.capacidad_almacenes[indice] = 36;
        Mattel.capacidad_productores[indice] = Integer.parseInt(info[7]);
        Mattel.productos_por_dia[indice] = 1f/2;
        Mattel.unidades_requeridas[indice] = 2;
        
        // Para Cuerpos i = 3
        indice = BUFFER.CUERPO.INDICE;
        Mattel.cantidad_productores[indice] = Integer.parseInt(info[2]);
        Mattel.capacidad_almacenes[indice] = 15;
        Mattel.capacidad_productores[indice] = Integer.parseInt(info[9]);
        Mattel.productos_por_dia[indice] = 1f/3;
        Mattel.unidades_requeridas[indice] = 1;
        
        // Inizializacion de Semaforos
        semaforo_exclusion_dias=new Semaphore(1);
        
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
                if (i == Mattel.BUFFER.BOTON.INDICE) producers.add(new Botones());
                if (i == Mattel.BUFFER.BRAZO.INDICE) producers.add(new Brazos());
                if (i == Mattel.BUFFER.PIERNA.INDICE) producers.add(new Piernas());
                if (i == Mattel.BUFFER.CUERPO.INDICE) producers.add(new Cuerpos());
            }
            productores.add(producers);
        }
        
        // Creando asembladores inicial
        for (int i = 0; i < cantidad_ensambladores; i++) {
            ensambladores.add(new Ensamblador());
        }
        
        // Comenzamos cada thread
        Jefe chief= new Jefe();
        chief.start();
        
        Gerente ger= new Gerente();
        ger.start();
        
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
    
    public static void contratarProductor(int productorIndice) {
        Productor productor = null;
        
        if (productorIndice == Mattel.BUFFER.BOTON.INDICE) productor = new Botones();
        if (productorIndice == Mattel.BUFFER.BRAZO.INDICE) productor = new Brazos();
        if (productorIndice == Mattel.BUFFER.PIERNA.INDICE) productor = new Piernas();
        if (productorIndice == Mattel.BUFFER.CUERPO.INDICE) productor = new Cuerpos();
        
        
        if (productor == null) {
            System.out.println("Error: no existe productor con indice " + productorIndice);
            return;
        }
        
        // Lo agregamos al arreglo global
        productores.get(productorIndice).add(productor);
        
        // Comenzamos el hilo
        productor.start();
    }
    
    public static void despedirProductor(int productorIndice) {
        List<Productor> productores;
        
        try {   // Para asegurarnos que existe un productor con este indice
            productores = Mattel.productores.get(productorIndice);
        } catch(Exception e) {
            System.out.println("Error: no existe productor con indice " + productorIndice);
            return;
        }
//        
        if (productores.isEmpty()) {
            System.out.println("Error: no hay productores de este tipo para despedir");
            return;
        }
        
        productores.get(0).despedir();
    }
    
    public static void contratarEnsamblador() {
        Ensamblador ensamblador = new Ensamblador();
        
        // Lo agregamos al arreglo global
        ensambladores.add(ensamblador);
        
        // Comenzamos el hilo
        ensamblador.start();
    }
    
    public static void despedirEnsamblador() {
        if (ensambladores.isEmpty()) {
            System.out.println("Error: no hay ensambladores para despedir");
            return;
        }
        
        ensambladores.get(0).despedir();
    }
    
    public static String Lectura_Txt(){
                //ARCHIVO DE TEXTO DE LA FORMA:
                //Productores de piernas=5
                //Productores de brazos=4
                //Productores de cuerpo central=3
                //Productores de botones=2
                //Ensambladores=1
                //Tiempo=10
                //Maximo productores piernas=6
                //Maximo productores brazos=8
                //Maximo productores cuerpo central=5
                //Maximo productores de botones=5
                //Maximo ensambladores=5
                

        
        	// Fichero del que queremos leer
		var fichero = new File("src\\P1\\Data.txt");
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
    
    
    
    public static void Escritura_Txt(String p_piernas,
                                     String p_brazos, 
                                     String p_cuerpoC,
                                     String p_botones,
                                     String ensambladores,
                                     String tiempo,
                                     String cant_d_entre_desp,
                                     String max_piernas,
                                     String max_brazos,
                                     String max_cuerpoC,
                                     String max_botones,
                                     String max_ensambladores){
        
        String[] info = { "Productores de piernas", 
                          "Productores de brazos",
                          "Productores de cuerpo central", 
                          "Productores de botones",
                          "Ensambladores",
                          "Tiempo",
                          "Cantidad de dias entre despachos",
                           "Maximo productores piernas",
                           "Maximo productores brazos",
                           "Maximo productores cuerpo central",
                           "Maximo productores de botones",
                           "Maximo ensambladores"
                        };
        String[] value = { 
                            p_piernas,
                            p_brazos, 
                            p_cuerpoC, 
                            p_botones, 
                            ensambladores, 
                            tiempo,
                            cant_d_entre_desp,
                            max_piernas,
                            max_brazos,
                            max_cuerpoC,
                            max_botones,
                            max_ensambladores};
        
        for (int i = 0; i < value.length; i++) {
            System.out.println(value[i]);
        }
   
		/** FORMA 1 DE ESCRITURA **/
		
		try {

            // Escribimos linea a linea en el fichero
            try (var fichero = new FileWriter("src\\P1\\Data.txt")) {
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
        
        Escritura_Txt("4","5", "4", "4", "5", "2","1","4","5","4","4","5");
        
    }
}

