package integrado.prog2;

import integrado.prog2.service.*;
import integrado.prog2.ui.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService();
        UsuarioService usuarioService = new UsuarioService();
        PedidoService pedidoService = new PedidoService();

        CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService, scanner);
        ProductoMenu productoMenu = new ProductoMenu(productoService, categoriaService, scanner);
        UsuarioMenu usuarioMenu = new UsuarioMenu(usuarioService, scanner);
        PedidoMenu pedidoMenu = new PedidoMenu(pedidoService, usuarioService, productoService, scanner);

        boolean salir = false;
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("   SISTEMA DE PEDIDOS - FOOD STORE");
            System.out.println("========================================");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> categoriaMenu.mostrar();
                case "2" -> productoMenu.mostrar();
                case "3" -> usuarioMenu.mostrar();
                case "4" -> pedidoMenu.mostrar();
                case "0" -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }

        System.out.println("¡Hasta luego!");
        scanner.close();
    }
}
