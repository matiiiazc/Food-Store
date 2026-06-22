package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.StockInvalidoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;
    private static Long detalleIdCounter = 1L;

    public Pedido(Long id, LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super(id);
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto)
            throws StockInvalidoException {
        if (cantidad <= 0) {
            throw new StockInvalidoException("La cantidad debe ser mayor a 0.");
        }
        if (producto.getStock() < cantidad) {
            throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        DetallePedido existente = findDetallePedidoByProducto(producto);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(existente.getCantidad() * precioUnitario);
        } else {
            double subtotal = cantidad * precioUnitario;
            DetallePedido detalle = new DetallePedido(detalleIdCounter++, cantidad, subtotal, producto);
            detalles.add(detalle);
        }
        this.total = calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido d : detalles) {
            if (d.getProducto().equals(producto)) return d;
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        detalles.removeIf(d -> d.getProducto().equals(producto));
        this.total = calcularTotal();
    }

    @Override
    public double calcularTotal() {
        double suma = 0;
        for (DetallePedido d : detalles) {
            suma += d.getSubtotal();
        }
        return suma;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetallePedido> getDetalles() { return detalles; }

    @Override
    public String toString() {
        return String.format("[ID: %d] Usuario: %s %s | Fecha: %s | Estado: %s | Pago: %s | Total: $%.2f",
                getId(),
                usuario.getNombre(), usuario.getApellido(),
                fecha, estado, formaPago, total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido p = (Pedido) o;
        return getId().equals(p.getId());
    }
}
