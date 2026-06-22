package integrado.prog2.service;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Usuario> listar() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.isEliminado()) activos.add(u);
        }
        return activos;
    }

    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id) && !u.isEliminado()) return u;
        }
        throw new EntidadNoEncontradaException("Usuario con ID " + id + " no encontrado.");
    }

    public Usuario crear(String nombre, String apellido, String mail, String celular,
                         String contrasenia, Rol rol) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty())
            throw new ValidacionException("El nombre no puede estar vacío.");
        if (mail == null || mail.trim().isEmpty())
            throw new ValidacionException("El mail no puede estar vacío.");
        validarMailUnico(mail, null);
        Usuario nuevo = new Usuario(idCounter++, nombre.trim(), apellido.trim(),
                mail.trim(), celular.trim(), contrasenia.trim(), rol);
        usuarios.add(nuevo);
        return nuevo;
    }

    public void editar(Long id, String nombre, String apellido, String mail,
                       String celular, String contrasenia)
            throws EntidadNoEncontradaException, ValidacionException {
        Usuario u = buscarPorId(id);
        if (nombre != null && !nombre.trim().isEmpty()) u.setNombre(nombre.trim());
        if (apellido != null && !apellido.trim().isEmpty()) u.setApellido(apellido.trim());
        if (mail != null && !mail.trim().isEmpty()) {
            validarMailUnico(mail, id);
            u.setMail(mail.trim());
        }
        if (celular != null && !celular.trim().isEmpty()) u.setCelular(celular.trim());
        if (contrasenia != null && !contrasenia.trim().isEmpty()) u.setContrasenia(contrasenia.trim());
    }

    private void validarMailUnico(String mail, Long idExcluir) throws ValidacionException {
        for (Usuario u : usuarios) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail.trim())
                    && (idExcluir == null || !u.getId().equals(idExcluir))) {
                throw new ValidacionException("Ya existe un usuario con ese mail.");
            }
        }
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Usuario u = buscarPorId(id);
        u.setEliminado(true);
    }
}
