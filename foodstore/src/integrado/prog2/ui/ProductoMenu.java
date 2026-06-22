package integrado.prog2.ui;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.CategoriaService;
import integrado.prog2.service.ProductoService;

import java.util.List;
import java.util.Scanner;

public class ProductoMenu {
    private ProductoService productoService;
    private CategoriaService categoriaService;
    private Scanner scanner;

    public ProductoMenu(ProductoService productoService, CategoriaService categoriaService, Scanner scanner) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.scanner = scanner;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE PRODUCTOS ===");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar por categoría");
            System.out.println("3. Crear");
            System.out.println("4. Editar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> listar();
                case "2" -> listarPorCategoria();
                case "3" -> crear();
                case "4" -> editar();
                case "5" -> eliminar();
                case "0" -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void listar() {
        List<Producto> lista = productoService.listar();
        if (lista.isEmpty()) System.out.println("No hay productos cargados.");
        else { System.out.println("\n--- Productos ---"); lista.forEach(System.out::println); }
    }

    private void listarPorCategoria() {
        categoriaService.listar().forEach(System.out::println);
        System.out.print("ID de categoría: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Categoria cat = categoriaService.buscarPorId(id);
            List<Producto> lista = productoService.listarPorCategoria(cat);
            if (lista.isEmpty()) System.out.println("No hay productos en esa categoría.");
            else lista.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void crear() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripción: ");
            String desc = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Imagen (URL o nombre): ");
            String imagen = scanner.nextLine();
            System.out.print("¿Disponible? (S/N): ");
            boolean disponible = scanner.nextLine().trim().equalsIgnoreCase("S");

            categoriaService.listar().forEach(System.out::println);
            System.out.print("ID de categoría: ");
            Long catId = Long.parseLong(scanner.nextLine().trim());
            Categoria cat = categoriaService.buscarPorId(catId);

            Producto nuevo = productoService.crear(nombre, precio, desc, stock, imagen, disponible, cat);
            System.out.println("Producto creado con ID: " + nuevo.getId());
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID de producto a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Producto p = productoService.buscarPorId(id);
            System.out.println("Editando: " + p);

            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripción (Enter para mantener): ");
            String desc = scanner.nextLine();
            System.out.print("Nuevo precio (Enter para mantener): ");
            String precioStr = scanner.nextLine().trim();
            System.out.print("Nuevo stock (Enter para mantener): ");
            String stockStr = scanner.nextLine().trim();
            System.out.print("Nueva imagen (Enter para mantener): ");
            String imagen = scanner.nextLine();
            System.out.print("¿Disponible? S/N (Enter para mantener): ");
            String dispStr = scanner.nextLine().trim();

            Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);
            Integer stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);
            Boolean disponible = dispStr.isEmpty() ? null : dispStr.equalsIgnoreCase("S");

            productoService.editar(id,
                    nombre.isEmpty() ? null : nombre,
                    precio,
                    desc.isEmpty() ? null : desc,
                    stock,
                    imagen.isEmpty() ? null : imagen,
                    disponible, null);
            System.out.println("Producto actualizado.");
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID de producto a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Producto p = productoService.buscarPorId(id);
            System.out.print("¿Confirmar eliminación de \"" + p.getNombre() + "\"? (S/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                productoService.eliminar(id);
                System.out.println("Producto eliminado.");
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
