package integrado.prog2.ui;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class UsuarioMenu {
    private UsuarioService service;
    private Scanner scanner;

    public UsuarioMenu(UsuarioService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE USUARIOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> listar();
                case "2" -> crear();
                case "3" -> editar();
                case "4" -> eliminar();
                case "0" -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void listar() {
        List<Usuario> lista = service.listar();
        if (lista.isEmpty()) System.out.println("No hay usuarios cargados.");
        else { System.out.println("\n--- Usuarios ---"); lista.forEach(System.out::println); }
    }

    private void crear() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            System.out.print("Mail: ");
            String mail = scanner.nextLine();
            System.out.print("Celular: ");
            String celular = scanner.nextLine();
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine();
            System.out.println("Rol: 1. ADMIN  2. USUARIO");
            System.out.print("Seleccione: ");
            String rolStr = scanner.nextLine().trim();
            Rol rol = rolStr.equals("1") ? Rol.ADMIN : Rol.USUARIO;

            Usuario nuevo = service.crear(nombre, apellido, mail, celular, pass, rol);
            System.out.println("Usuario creado con ID: " + nuevo.getId());
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID de usuario a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Usuario u = service.buscarPorId(id);
            System.out.println("Editando: " + u);

            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo apellido (Enter para mantener): ");
            String apellido = scanner.nextLine();
            System.out.print("Nuevo mail (Enter para mantener): ");
            String mail = scanner.nextLine();
            System.out.print("Nuevo celular (Enter para mantener): ");
            String celular = scanner.nextLine();
            System.out.print("Nueva contraseña (Enter para mantener): ");
            String pass = scanner.nextLine();

            service.editar(id,
                    nombre.isEmpty() ? null : nombre,
                    apellido.isEmpty() ? null : apellido,
                    mail.isEmpty() ? null : mail,
                    celular.isEmpty() ? null : celular,
                    pass.isEmpty() ? null : pass);
            System.out.println("Usuario actualizado.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID de usuario a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Usuario u = service.buscarPorId(id);
            System.out.print("¿Confirmar eliminación de \"" + u.getNombre() + " " + u.getApellido() + "\"? (S/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                service.eliminar(id);
                System.out.println("Usuario eliminado.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
