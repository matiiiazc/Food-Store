package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    private List<Categoria> categorias = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Categoria> listar() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria c : categorias) {
            if (!c.isEliminado()) activas.add(c);
        }
        return activas;
    }

    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria c : categorias) {
            if (c.getId().equals(id) && !c.isEliminado()) return c;
        }
        throw new EntidadNoEncontradaException("Categoría con ID " + id + " no encontrada.");
    }

    public Categoria crear(String nombre, String descripcion) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty())
            throw new ValidacionException("El nombre no puede estar vacío.");
        if (descripcion == null || descripcion.trim().isEmpty())
            throw new ValidacionException("La descripción no puede estar vacía.");
        for (Categoria c : categorias) {
            if (!c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre.trim()))
                throw new ValidacionException("Ya existe una categoría con ese nombre.");
        }
        Categoria nueva = new Categoria(idCounter++, nombre.trim(), descripcion.trim());
        categorias.add(nueva);
        return nueva;
    }

    public void editar(Long id, String nuevoNombre, String nuevaDescripcion)
            throws EntidadNoEncontradaException, ValidacionException {
        Categoria c = buscarPorId(id);
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            for (Categoria otro : categorias) {
                if (!otro.isEliminado() && !otro.getId().equals(id)
                        && otro.getNombre().equalsIgnoreCase(nuevoNombre.trim()))
                    throw new ValidacionException("Ya existe una categoría con ese nombre.");
            }
            c.setNombre(nuevoNombre.trim());
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            c.setDescripcion(nuevaDescripcion.trim());
        }
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Categoria c = buscarPorId(id);
        c.setEliminado(true);
    }

    public List<Categoria> getTodas() {
        return categorias;
    }
}
