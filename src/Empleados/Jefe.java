package Empleados;

/**
 *
 * @author Daniel & Rommel
 */
public class Jefe {
    
    public int dias_para_entrega;
    public int dias_restantes;
    
    // Constantes
    public float tiempo_para_decrementar = 8f/24;   // 8 horas cambiando decrementando contador
    public float tiempo_en_reposo = 24f - this.tiempo_para_decrementar; // resto del dia en reposo
    
    public Jefe(int dias_para_entrega){
        this.dias_para_entrega = dias_para_entrega;
        this.dias_restantes = dias_para_entrega;
    }
    
    public void decrementar_dias() {
        this.dias_restantes -= 1;
    }
    
    public void reiniciar_dias() {
        this.dias_restantes = this.dias_para_entrega;
    }
}
