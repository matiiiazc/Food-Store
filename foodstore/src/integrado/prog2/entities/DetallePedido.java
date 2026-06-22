package integrado.prog2.entities;

public class DetallePedido extends Base {
    private Integer cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(Long id, Integer cantidad, Double subtotal, Producto producto) {
        super(id);
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public String toString() {
        return String.format("  Producto: %s | Cantidad: %d | Subtotal: $%.2f",
                producto.getNombre(), cantidad, subtotal);
    }
}
