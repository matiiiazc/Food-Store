package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Producto> listar() {
        List<Producto> activos = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado()) activos.add(p);
        }
        return activos;
    }

    public List<Producto> listarPorCategoria(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado() && p.getCategoria().equals(categoria)) resultado.add(p);
        }
        return resultado;
    }

    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto p : productos) {
            if (p.getId().equals(id) && !p.isEliminado()) return p;
        }
        throw new EntidadNoEncontradaException("Producto con ID " + id + " no encontrado.");
    }

    public Producto crear(String nombre, Double precio, String descripcion, Integer stock,
                          String imagen, boolean disponible, Categoria categoria)
            throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty())
            throw new ValidacionException("El nombre no puede estar vacío.");
        if (precio < 0)
            throw new ValidacionException("El precio no puede ser negativo.");
        if (stock < 0)
            throw new ValidacionException("El stock no puede ser negativo.");

        Producto nuevo = new Producto(idCounter++, nombre.trim(), precio, descripcion.trim(),
                stock, imagen.trim(), disponible, categoria);
        productos.add(nuevo);
        return nuevo;
    }

    public void editar(Long id, String nombre, Double precio, String descripcion,
                       Integer stock, String imagen, Boolean disponible, Categoria categoria)
            throws EntidadNoEncontradaException, ValidacionException {
        Producto p = buscarPorId(id);
        if (nombre != null && !nombre.trim().isEmpty()) p.setNombre(nombre.trim());
        if (precio != null) {
            if (precio < 0) throw new ValidacionException("El precio no puede ser negativo.");
            p.setPrecio(precio);
        }
        if (descripcion != null && !descripcion.trim().isEmpty()) p.setDescripcion(descripcion.trim());
        if (stock != null) {
            if (stock < 0) throw new ValidacionException("El stock no puede ser negativo.");
            p.setStock(stock);
        }
        if (imagen != null && !imagen.trim().isEmpty()) p.setImagen(imagen.trim());
        if (disponible != null) p.setDisponible(disponible);
        if (categoria != null) p.setCategoria(categoria);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Producto p = buscarPorId(id);
        p.setEliminado(true);
    }
}
