package ec.edu.puce.modelo;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private String nombre;

    private List<Categoria> subcategorias;

    private List<Producto> productos;

    public Categoria(String nombre) {

        this.nombre = nombre;
        this.subcategorias = new ArrayList<>();
        this.productos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Categoria> getSubcategorias() {
        return subcategorias;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    // Agregar producto
    public void agregarProducto(Producto producto) {

        if (producto == null) {

            System.out.println(
                    "No se puede agregar un producto vacío."
            );

            return;
        }

        productos.add(producto);
    }

    // Verificar si existe una categoría directa
    public boolean existeCategoria(
            String nombreCategoria
    ) {

        if (nombreCategoria == null) {
            return false;
        }

        for (Categoria categoria : subcategorias) {

            if (normalizar(categoria.getNombre())
                    .equals(normalizar(nombreCategoria))) {

                return true;
            }
        }

        return false;
    }

    // Agregar categoría sin repetir
    public void agregarCategoria(
            Categoria categoria
    ) {

        if (categoria == null) {

            System.out.println(
                    "No se puede agregar una categoría vacía."
            );

            return;
        }

        if (existeCategoria(categoria.getNombre())) {

            System.out.println(
                    "La categoría ya existe: "
                            + categoria.getNombre()
            );

        } else {

            subcategorias.add(categoria);

            System.out.println(
                    "Categoría agregada: "
                            + categoria.getNombre()
            );
        }
    }

    // Buscar producto por nombre exacto
    public Producto buscarProducto(
            String nombreProducto
    ) {

        if (nombreProducto == null) {
            return null;
        }

        for (Producto producto : productos) {

            if (normalizar(producto.getNombre())
                    .equals(normalizar(nombreProducto))) {

                return producto;
            }
        }

        /*
         * Búsqueda recursiva dentro
         * de todas las subcategorías.
         */
        for (Categoria categoria : subcategorias) {

            Producto encontrado =
                    categoria.buscarProducto(
                            nombreProducto
                    );

            if (encontrado != null) {
                return encontrado;
            }
        }

        return null;
    }

    // Buscar uno o varios productos por coincidencia parcial
    public List<ResultadoBusqueda> buscarProductos(
            String textoBusqueda
    ) {

        List<ResultadoBusqueda> resultados =
                new ArrayList<>();

        if (textoBusqueda == null
                || textoBusqueda.trim().isEmpty()) {

            return resultados;
        }

        buscarProductosRecursivo(
                normalizar(textoBusqueda),
                nombre,
                resultados
        );

        return resultados;
    }

    private void buscarProductosRecursivo(
            String textoBusqueda,
            String rutaActual,
            List<ResultadoBusqueda> resultados
    ) {

        /*
         * Buscar dentro de los productos
         * de la categoría actual.
         */
        for (Producto producto : productos) {

            String nombreProducto =
                    normalizar(producto.getNombre());

            if (nombreProducto.contains(textoBusqueda)) {

                ResultadoBusqueda resultado =
                        new ResultadoBusqueda(
                                producto,
                                rutaActual
                        );

                resultados.add(resultado);
            }
        }

        /*
         * Recorrer recursivamente todas
         * las subcategorías.
         */
        for (Categoria categoria : subcategorias) {

            String nuevaRuta =
                    rutaActual
                            + " > "
                            + categoria.getNombre();

            categoria.buscarProductosRecursivo(
                    textoBusqueda,
                    nuevaRuta,
                    resultados
            );
        }
    }

    // Buscar categoría recursivamente
    public Categoria buscarCategoria(
            String nombreCategoria
    ) {

        if (nombreCategoria == null) {
            return null;
        }

        if (normalizar(nombre)
                .equals(normalizar(nombreCategoria))) {

            return this;
        }

        for (Categoria categoria : subcategorias) {

            Categoria encontrada =
                    categoria.buscarCategoria(
                            nombreCategoria
                    );

            if (encontrada != null) {
                return encontrada;
            }
        }

        return null;
    }

    // Mostrar catálogo de forma sencilla
    public void mostrarCatalogo() {

        System.out.println(
                "\nCategoría: " + nombre
        );

        if (!productos.isEmpty()) {

            System.out.println("Productos:");

            for (Producto producto : productos) {
                producto.mostrarProducto();
            }
        }

        for (Categoria categoria : subcategorias) {
            categoria.mostrarCatalogo();
        }
    }

    // Mostrar estructura no lineal como árbol
    public void mostrarArbol() {
        mostrarArbolRecursivo(0);
    }

    private void mostrarArbolRecursivo(
            int nivel
    ) {

        StringBuilder espacio =
                new StringBuilder();

        for (int i = 0; i < nivel; i++) {
            espacio.append("    ");
        }

        System.out.println(
                espacio
                        + "[Categoría] "
                        + nombre
        );

        for (Producto producto : productos) {

            System.out.println(
                    espacio
                            + "    └── "
                            + producto
            );
        }

        for (Categoria categoria : subcategorias) {

            categoria.mostrarArbolRecursivo(
                    nivel + 1
            );
        }
    }

    // Eliminar tildes y convertir a minúsculas
    private String normalizar(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        String textoNormalizado =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );

        textoNormalizado =
                textoNormalizado.replaceAll(
                        "\\p{M}",
                        ""
                );

        return textoNormalizado
                .trim()
                .toLowerCase();
    }
}