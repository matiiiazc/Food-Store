package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.exception.ValidacionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private List<Pedido> pedidos = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Pedido> listar() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) activos.add(p);
        }
        return activos;
    }

    public List<Pedido> listarPorUsuario(Usuario usuario) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado() && p.getUsuario().equals(usuario)) resultado.add(p);
        }
        return resultado;
    }

    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) return p;
        }
        throw new EntidadNoEncontradaException("Pedido con ID " + id + " no encontrado.");
    }

    public Pedido crear(Usuario usuario, FormaPago formaPago) throws ValidacionException {
        if (usuario == null) throw new ValidacionException("El pedido debe tener un usuario.");
        Pedido nuevo = new Pedido(idCounter++, LocalDate.now(), Estado.PENDIENTE, formaPago, usuario);
        pedidos.add(nuevo);
        return nuevo;
    }

    public void agregarDetalle(Pedido pedido, int cantidad, Producto producto)
            throws StockInvalidoException {
        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
    }

    public void actualizarEstado(Long id, Estado nuevoEstado)
            throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEstado(nuevoEstado);
    }

    public void actualizarFormaPago(Long id, FormaPago nuevaFormaPago)
            throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setFormaPago(nuevaFormaPago);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
        p.getDetalles().forEach(d -> d.setEliminado(true));
    }

    public void cancelarPedido(Pedido pedido) {
        pedidos.remove(pedido);
    }
}
