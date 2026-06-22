package integrado.prog2.ui;

import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.PedidoService;
import integrado.prog2.service.ProductoService;
import integrado.prog2.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class PedidoMenu {
    private PedidoService pedidoService;
    private UsuarioService usuarioService;
    private ProductoService productoService;
    private Scanner scanner;

    public PedidoMenu(PedidoService pedidoService, UsuarioService usuarioService,
                      ProductoService productoService, Scanner scanner) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.scanner = scanner;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE PEDIDOS ===");
            System.out.println("1. Listar pedidos");
            System.out.println("2. Listar por usuario");
            System.out.println("3. Crear pedido");
            System.out.println("4. Actualizar estado/forma de pago");
            System.out.println("5. Ver detalles de pedido");
            System.out.println("6. Eliminar pedido");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> listar();
                case "2" -> listarPorUsuario();
                case "3" -> crear();
                case "4" -> actualizar();
                case "5" -> verDetalles();
                case "6" -> eliminar();
                case "0" -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void listar() {
        List<Pedido> lista = pedidoService.listar();
        if (lista.isEmpty()) System.out.println("No hay pedidos registrados.");
        else { System.out.println("\n--- Pedidos ---"); lista.forEach(System.out::println); }
    }

    private void listarPorUsuario() {
        usuarioService.listar().forEach(System.out::println);
        System.out.print("ID de usuario: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Usuario u = usuarioService.buscarPorId(id);
            List<Pedido> lista = pedidoService.listarPorUsuario(u);
            if (lista.isEmpty()) System.out.println("No hay pedidos para ese usuario.");
            else lista.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void crear() {
        try {
            usuarioService.listar().forEach(System.out::println);
            System.out.print("ID de usuario: ");
            Long uid = Long.parseLong(scanner.nextLine().trim());
            Usuario usuario = usuarioService.buscarPorId(uid);

            System.out.println("Forma de pago: 1. TARJETA  2. TRANSFERENCIA  3. EFECTIVO");
            System.out.print("Seleccione: ");
            String fpStr = scanner.nextLine().trim();
            FormaPago fp = switch (fpStr) {
                case "1" -> FormaPago.TARJETA;
                case "2" -> FormaPago.TRANSFERENCIA;
                default -> FormaPago.EFECTIVO;
            };

            Pedido pedido = pedidoService.crear(usuario, fp);
            System.out.println("Pedido creado con ID: " + pedido.getId());

            boolean agregarMas = true;
            while (agregarMas) {
                productoService.listar().forEach(System.out::println);
                System.out.print("ID de producto (0 para terminar): ");
                Long pid = Long.parseLong(scanner.nextLine().trim());
                if (pid == 0) break;

                Producto producto = productoService.buscarPorId(pid);
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine().trim());

                try {
                    pedidoService.agregarDetalle(pedido, cantidad, producto);
                    System.out.println("Detalle agregado. Subtotal parcial: $" + String.format("%.2f", pedido.getTotal()));
                } catch (StockInvalidoException e) {
                    System.out.println("Error al agregar detalle: " + e.getMessage());
                    System.out.println("Pedido cancelado por error en detalle.");
                    pedidoService.cancelarPedido(pedido);
                    return;
                }

                System.out.print("¿Agregar otro producto? (S/N): ");
                agregarMas = scanner.nextLine().trim().equalsIgnoreCase("S");
            }

            System.out.println("Pedido finalizado. Total: $" + String.format("%.2f", pedido.calcularTotal()));

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizar() {
        listar();
        System.out.print("ID de pedido a actualizar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Pedido p = pedidoService.buscarPorId(id);
            System.out.println("Pedido actual: " + p);

            System.out.println("¿Qué desea actualizar?");
            System.out.println("1. Estado");
            System.out.println("2. Forma de pago");
            System.out.println("3. Ambos");
            System.out.print("Seleccione: ");
            String op = scanner.nextLine().trim();

            if (op.equals("1") || op.equals("3")) {
                System.out.println("Estado: 1.PENDIENTE 2.CONFIRMADO 3.TERMINADO 4.CANCELADO");
                System.out.print("Seleccione: ");
                String est = scanner.nextLine().trim();
                Estado estado = switch (est) {
                    case "1" -> Estado.PENDIENTE;
                    case "2" -> Estado.CONFIRMADO;
                    case "3" -> Estado.TERMINADO;
                    default -> Estado.CANCELADO;
                };
                pedidoService.actualizarEstado(id, estado);
            }

            if (op.equals("2") || op.equals("3")) {
                System.out.println("Forma de pago: 1.TARJETA 2.TRANSFERENCIA 3.EFECTIVO");
                System.out.print("Seleccione: ");
                String fp = scanner.nextLine().trim();
                FormaPago formaPago = switch (fp) {
                    case "1" -> FormaPago.TARJETA;
                    case "2" -> FormaPago.TRANSFERENCIA;
                    default -> FormaPago.EFECTIVO;
                };
                pedidoService.actualizarFormaPago(id, formaPago);
            }

            System.out.println("Pedido actualizado.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verDetalles() {
        listar();
        System.out.print("ID de pedido: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Pedido p = pedidoService.buscarPorId(id);
            System.out.println("\nPedido: " + p);
            System.out.println("Detalles:");
            if (p.getDetalles().isEmpty()) {
                System.out.println("  Sin detalles.");
            } else {
                for (DetallePedido d : p.getDetalles()) {
                    System.out.println(d);
                }
            }
            System.out.printf("TOTAL: $%.2f%n", p.calcularTotal());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID de pedido a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Pedido p = pedidoService.buscarPorId(id);
            System.out.print("¿Confirmar eliminación del pedido ID " + id + "? (S/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                pedidoService.eliminar(id);
                System.out.println("Pedido eliminado.");
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
