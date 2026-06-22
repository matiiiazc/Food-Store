package integrado.prog2.ui;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.CategoriaService;

import java.util.List;
import java.util.Scanner;

public class CategoriaMenu {
    private CategoriaService service;
    private Scanner scanner;

    public CategoriaMenu(CategoriaService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE CATEGORÍAS ===");
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
        List<Categoria> lista = service.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
        } else {
            System.out.println("\n--- Categorías ---");
            lista.forEach(System.out::println);
        }
    }

    private void crear() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        try {
            Categoria nueva = service.crear(nombre, desc);
            System.out.println("Categoría creada con ID: " + nueva.getId());
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID de categoría a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Categoria c = service.buscarPorId(id);
            System.out.println("Editando: " + c);
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripción (Enter para mantener): ");
            String desc = scanner.nextLine();
            service.editar(id, nombre.isEmpty() ? null : nombre, desc.isEmpty() ? null : desc);
            System.out.println("Categoría actualizada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID de categoría a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Categoria c = service.buscarPorId(id);
            System.out.println("¿Confirmar eliminación de \"" + c.getNombre() + "\"? (S/N): ");
            String conf = scanner.nextLine().trim();
            if (conf.equalsIgnoreCase("S")) {
                service.eliminar(id);
                System.out.println("Categoría eliminada.");
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
