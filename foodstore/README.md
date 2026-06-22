# Food Store – Sistema de Gestión de Pedidos

**Tecnicatura Universitaria en Programación – Programación 2**

**Integrantes:**
- Gonzales Patricio
- Zuvialde Ignacio
- Azcurra Matias

---

## ¿Cómo ejecutar el proyecto?

### Requisitos
- Java 21 instalado
- IDE recomendado: IntelliJ IDEA o Eclipse

### Pasos

1. Clonar o descomprimir el proyecto
2. Abrir en el IDE como proyecto Java
3. Marcar `src` como carpeta fuente (Sources Root)
4. Ejecutar la clase `integrado.prog2.Main`

### Desde consola (opcional)
```bash
cd foodstore
javac -d out -sourcepath src src/integrado/prog2/Main.java
java -cp out integrado.prog2.Main
```

---

## Estructura del proyecto

```
src/
└── integrado/prog2/
    ├── Main.java
    ├── entities/     → Clases del modelo (Base, Categoria, Producto, Usuario, Pedido, DetallePedido)
    ├── enums/        → Rol, Estado, FormaPago
    ├── exception/    → Excepciones propias
    ├── service/      → Lógica de negocio y manejo de colecciones
    └── ui/           → Menús de consola
```

---

## Links

- 📹 Video demostrativo: [AGREGAR LINK]
- 📄 Documentación PDF: [AGREGAR LINK]
