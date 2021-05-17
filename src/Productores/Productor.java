package Productores;

/**
 *
 * @author Daniel & Rommel
 */
public abstract class Productor {
    
    public String producto;    
    public int almacenamiento;
    public float producto_por_dia;
    
    public Productor(String producto, int almacenamiento, float producto_por_dia) {
        this.producto = producto;
        this.almacenamiento = almacenamiento;
        this.producto_por_dia = producto_por_dia;
    }
}
