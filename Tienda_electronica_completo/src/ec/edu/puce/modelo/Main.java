package ec.edu.puce.modelo;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Categoria catalogo =
                crearCatalogo();

        Scanner teclado =
                new Scanner(System.in);

        int opcion;

        do {

            mostrarMenu();

            opcion = leerEntero(teclado);

            switch (opcion) {

                case 1:

                    mostrarCatalogo(catalogo);
                    break;

                case 2:

                    buscarProducto(
                            teclado,
                            catalogo
                    );

                    break;

                case 3:

                    buscarCategoria(
                            teclado,
                            catalogo
                    );

                    break;

                case 0:

                    System.out.println(
                            "\nPrograma finalizado."
                    );

                    break;

                default:

                    System.out.println(
                            "\nOpción incorrecta."
                    );

                    break;
            }

        } while (opcion != 0);

        teclado.close();
    }

    private static void mostrarMenu() {

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "          TIENDA ELECTRÓNICA"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "1. Mostrar productos y categorías"
        );

        System.out.println(
                "2. Buscar producto"
        );

        System.out.println(
                "3. Buscar categoría"
        );

        System.out.println(
                "0. Salir"
        );

        System.out.print(
                "Seleccione una opción: "
        );
    }

    private static int leerEntero(
            Scanner teclado
    ) {

        try {

            return Integer.parseInt(
                    teclado.nextLine()
            );

        } catch (NumberFormatException e) {

            return -1;
        }
    }

    private static void mostrarCatalogo(
            Categoria catalogo
    ) {

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "          CATÁLOGO DE LA TIENDA"
        );

        System.out.println(
                "======================================"
        );

        catalogo.mostrarArbol();
    }

    private static void buscarProducto(
            Scanner teclado,
            Categoria catalogo
    ) {

        System.out.print(
                "\nIngrese el nombre "
                        + "o una parte del producto: "
        );

        String nombre =
                teclado.nextLine();

        if (nombre.trim().isEmpty()) {

            System.out.println(
                    "Debe ingresar un texto de búsqueda."
            );

            return;
        }

        List<ResultadoBusqueda> resultados =
                catalogo.buscarProductos(nombre);

        if (resultados.isEmpty()) {

            System.out.println(
                    "No se encontraron productos."
            );

            return;
        }

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "          PRODUCTOS ENCONTRADOS"
        );

        System.out.println(
                "======================================"
        );

        for (int i = 0;
             i < resultados.size();
             i++) {

            ResultadoBusqueda resultado =
                    resultados.get(i);

            System.out.println(
                    "\n"
                            + (i + 1)
                            + ". "
                            + resultado.getProducto()
            );

            System.out.println(
                    "   Ruta: "
                            + resultado.getRuta()
            );
        }
    }

    private static void buscarCategoria(
            Scanner teclado,
            Categoria catalogo
    ) {

        System.out.print(
                "\nIngrese el nombre "
                        + "de la categoría: "
        );

        String nombre =
                teclado.nextLine();

        if (nombre.trim().isEmpty()) {

            System.out.println(
                    "Debe ingresar el nombre de una categoría."
            );

            return;
        }

        Categoria categoria =
                catalogo.buscarCategoria(nombre);

        if (categoria == null) {

            System.out.println(
                    "Categoría no encontrada."
            );

            return;
        }

        System.out.println(
                "\nCategoría encontrada: "
                        + categoria.getNombre()
        );

        if (categoria.getProductos().isEmpty()) {

            System.out.println(
                    "No tiene productos directos."
            );

        } else {

            System.out.println(
                    "\nProductos:"
            );

            for (Producto producto
                    : categoria.getProductos()) {

                System.out.println(
                        "- " + producto
                );
            }
        }

        if (categoria
                .getSubcategorias()
                .isEmpty()) {

            System.out.println(
                    "No tiene subcategorías."
            );

        } else {

            System.out.println(
                    "\nSubcategorías:"
            );

            for (Categoria subcategoria
                    : categoria.getSubcategorias()) {

                System.out.println(
                        "- "
                                + subcategoria.getNombre()
                );
            }
        }
    }

    public static Categoria crearCatalogo() {

        Categoria catalogo =
                new Categoria(
                        "Catálogo general"
                );

        /*
         * Categorías principales.
         */
        Categoria electronica =
                new Categoria(
                        "Electrónica"
                );

        Categoria ropa =
                new Categoria(
                        "Ropa"
                );

        Categoria hogar =
                new Categoria(
                        "Hogar"
                );

        catalogo.agregarCategoria(
                electronica
        );

        catalogo.agregarCategoria(
                ropa
        );

        catalogo.agregarCategoria(
                hogar
        );

        /*
         * Categorías de electrónica.
         */
        Categoria telefonos =
                new Categoria(
                        "Teléfonos"
                );

        Categoria computadoras =
                new Categoria(
                        "Computadoras"
                );

        Categoria accesorios =
                new Categoria(
                        "Accesorios"
                );

        electronica.agregarCategoria(
                telefonos
        );

        electronica.agregarCategoria(
                computadoras
        );

        electronica.agregarCategoria(
                accesorios
        );

        /*
         * Productos de teléfonos.
         */
        telefonos.agregarProducto(
                new Producto(
                        "iPhone 15",
                        999.00
                )
        );

        telefonos.agregarProducto(
                new Producto(
                        "Samsung Galaxy S24",
                        899.00
                )
        );

        telefonos.agregarProducto(
                new Producto(
                        "Xiaomi Redmi Note 13",
                        299.99
                )
        );

        /*
         * Productos de computadoras.
         */
        computadoras.agregarProducto(
                new Producto(
                        "Laptop Lenovo",
                        750.00
                )
        );

        computadoras.agregarProducto(
                new Producto(
                        "Laptop HP",
                        680.00
                )
        );

        computadoras.agregarProducto(
                new Producto(
                        "MacBook Air",
                        1199.00
                )
        );

        /*
         * Productos de accesorios.
         */
        accesorios.agregarProducto(
                new Producto(
                        "Mouse inalámbrico",
                        18.50
                )
        );

        accesorios.agregarProducto(
                new Producto(
                        "Teclado mecánico",
                        49.90
                )
        );

        accesorios.agregarProducto(
                new Producto(
                        "Audífonos Bluetooth",
                        35.00
                )
        );

        /*
         * Categorías de ropa.
         */
        Categoria hombre =
                new Categoria(
                        "Hombre"
                );

        Categoria mujer =
                new Categoria(
                        "Mujer"
                );

        ropa.agregarCategoria(
                hombre
        );

        ropa.agregarCategoria(
                mujer
        );

        /*
         * Productos de hombre.
         */
        hombre.agregarProducto(
                new Producto(
                        "Camiseta deportiva",
                        25.00
                )
        );

        hombre.agregarProducto(
                new Producto(
                        "Pantalón jean",
                        40.00
                )
        );

        /*
         * Productos de mujer.
         */
        mujer.agregarProducto(
                new Producto(
                        "Vestido casual",
                        39.99
                )
        );

        mujer.agregarProducto(
                new Producto(
                        "Blusa elegante",
                        28.75
                )
        );

        /*
         * Categorías de hogar.
         */
        Categoria cocina =
                new Categoria(
                        "Cocina"
                );

        Categoria iluminacion =
                new Categoria(
                        "Iluminación"
                );

        hogar.agregarCategoria(
                cocina
        );

        hogar.agregarCategoria(
                iluminacion
        );

        /*
         * Productos de cocina.
         */
        cocina.agregarProducto(
                new Producto(
                        "Licuadora",
                        55.00
                )
        );

        cocina.agregarProducto(
                new Producto(
                        "Juego de ollas",
                        89.90
                )
        );

        /*
         * Productos de iluminación.
         */
        iluminacion.agregarProducto(
                new Producto(
                        "Lámpara LED",
                        18.75
                )
        );

        iluminacion.agregarProducto(
                new Producto(
                        "Foco inteligente",
                        14.99
                )
        );

        return catalogo;
    }
}