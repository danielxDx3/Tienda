package ec.edu.puce.modelo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Servidor web de la Tienda Electrónica.
 *
 * Características:
 *
 * - No utiliza rutas absolutas.
 * - No depende del nombre del proyecto.
 * - Funciona aunque se cambie de carpeta.
 * - Busca automáticamente la carpeta "web".
 * - Funciona desde IntelliJ.
 * - Expone los productos Java mediante:
 *
 *      http://localhost:8085/api/productos
 *
 * - Sirve HTML, CSS, JS e imágenes.
 */
public class ServidorWeb {

    private static final int PUERTO = 8085;

    /*
     * Busca automáticamente la carpeta web.
     */
    private static final Path CARPETA_WEB = localizarCarpetaWeb();

    /*
     * Catálogo Java.
     */
    private static final Categoria CATALOGO = Main.crearCatalogo();


    /**
     * Inicia el servidor.
     */
    public static void main(String[] args) throws IOException {

        System.out.println();
        System.out.println("============================================");
        System.out.println("       TIENDA ELECTRÓNICA - WEB + JAVA");
        System.out.println("============================================");
        System.out.println();

        System.out.println("Buscando carpeta web...");
        System.out.println("Carpeta encontrada:");
        System.out.println(CARPETA_WEB);
        System.out.println();


        /*
         * Comprobación importante.
         */
        Path index = CARPETA_WEB.resolve("index.html");

        if (!Files.isRegularFile(index)) {

            System.err.println("============================================");
            System.err.println("ERROR: NO SE ENCONTRÓ index.html");
            System.err.println("============================================");
            System.err.println();
            System.err.println(
                    "Se esperaba encontrar:"
            );
            System.err.println(index);
            System.err.println();

            System.err.println(
                    "La estructura recomendada es:"
            );

            System.err.println(
                    "proyecto/"
            );
            System.err.println(
                    "├── src/"
            );
            System.err.println(
                    "└── web/"
            );
            System.err.println(
                    "    └── index.html"
            );

            System.err.println();

            return;
        }


        /*
         * Crear servidor.
         */
        HttpServer servidor = HttpServer.create(
                new InetSocketAddress(PUERTO),
                0
        );


        /*
         * API.
         */
        servidor.createContext(
                "/api/productos",
                new ProductosHandler()
        );


        /*
         * Archivos web.
         */
        servidor.createContext(
                "/",
                new ArchivosWebHandler()
        );


        servidor.setExecutor(null);

        servidor.start();


        System.out.println("============================================");
        System.out.println("SERVIDOR INICIADO CORRECTAMENTE");
        System.out.println("============================================");
        System.out.println();

        System.out.println(
                "Web:"
        );

        System.out.println(
                "http://localhost:" + PUERTO
        );

        System.out.println();

        System.out.println(
                "API:"
        );

        System.out.println(
                "http://localhost:"
                        + PUERTO
                        + "/api/productos"
        );

        System.out.println();

        System.out.println(
                "Carpeta web:"
        );

        System.out.println(CARPETA_WEB);

        System.out.println();

        System.out.println(
                "Presione Ctrl + C para detener."
        );

        System.out.println();
    }


    /**
     * ============================================================
     * BUSCADOR AUTOMÁTICO DE LA CARPETA WEB
     * ============================================================
     *
     * No depende del nombre de la carpeta del proyecto.
     *
     * Busca desde:
     *
     * 1. Directorio actual.
     * 2. Padres del directorio actual.
     * 3. Ubicación de las clases compiladas.
     * 4. Padres de la ubicación de las clases.
     * 5. Subcarpetas cercanas.
     */
    private static Path localizarCarpetaWeb() {

        List<Path> lugares = new ArrayList<>();


        /*
         * --------------------------------------------------------
         * 1. Directorio desde donde se ejecutó Java.
         * --------------------------------------------------------
         */
        Path actual = Paths
                .get("")
                .toAbsolutePath()
                .normalize();

        agregarConPadres(lugares, actual);


        /*
         * --------------------------------------------------------
         * 2. Ubicación real de ServidorWeb.class
         *
         * Esto es muy útil cuando IntelliJ ejecuta:
         *
         * out/production/...
         *
         * --------------------------------------------------------
         */
        try {

            CodeSource codeSource =
                    ServidorWeb.class
                            .getProtectionDomain()
                            .getCodeSource();

            if (codeSource != null
                    && codeSource.getLocation() != null) {

                Path ubicacionClase =
                        Paths.get(
                                        codeSource
                                                .getLocation()
                                                .toURI()
                                )
                                .toAbsolutePath()
                                .normalize();

                /*
                 * Si apunta a un archivo, usamos su padre.
                 */
                if (Files.isRegularFile(
                        ubicacionClase)) {

                    ubicacionClase =
                            ubicacionClase.getParent();
                }

                agregarConPadres(
                        lugares,
                        ubicacionClase
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "No se pudo determinar la ubicación de las clases."
            );
        }


        /*
         * --------------------------------------------------------
         * 3. Buscar "web" en todos los lugares candidatos.
         * --------------------------------------------------------
         */
        for (Path lugar : lugares) {

            Path encontrada =
                    buscarWebEnLugar(lugar);

            if (encontrada != null) {

                return encontrada
                        .toAbsolutePath()
                        .normalize();
            }
        }


        /*
         * --------------------------------------------------------
         * 4. Último intento:
         *
         * buscar desde el directorio actual hasta 8 niveles.
         * --------------------------------------------------------
         */
        Path encontrada =
                buscarWebRecursivamente(actual, 8);

        if (encontrada != null) {

            return encontrada
                    .toAbsolutePath()
                    .normalize();
        }


        /*
         * No encontrada.
         */
        return actual
                .resolve("web")
                .normalize();
    }


    /**
     * Agrega una ruta y todos sus padres.
     */
    private static void agregarConPadres(
            List<Path> lugares,
            Path inicio) {

        if (inicio == null) {
            return;
        }

        Path actual = inicio;

        while (actual != null) {

            if (!lugares.contains(actual)) {
                lugares.add(actual);
            }

            actual = actual.getParent();
        }
    }


    /**
     * Busca:
     *
     * lugar/web/index.html
     */
    private static Path buscarWebEnLugar(
            Path lugar) {

        if (lugar == null) {
            return null;
        }


        Path web =
                lugar.resolve("web");


        if (Files.isRegularFile(
                web.resolve("index.html"))) {

            return web;
        }


        return null;
    }


    /**
     * Busca una carpeta web dentro de un directorio.
     */
    private static Path buscarWebRecursivamente(
            Path inicio,
            int profundidadMaxima) {

        if (inicio == null
                || !Files.isDirectory(inicio)) {

            return null;
        }


        try {

            try (Stream<Path> rutas =
                         Files.walk(
                                 inicio,
                                 profundidadMaxima
                         )) {

                return rutas

                        .filter(Files::isDirectory)

                        .filter(dir -> {

                            Path nombre =
                                    dir.getFileName();

                            return nombre != null
                                    && nombre
                                    .toString()
                                    .equalsIgnoreCase(
                                            "web"
                                    );
                        })

                        .filter(dir ->
                                Files.isRegularFile(
                                        dir.resolve(
                                                "index.html"
                                        )
                                )
                        )

                        .findFirst()

                        .orElse(null);
            }

        } catch (IOException e) {

            return null;
        }
    }


    /**
     * ============================================================
     * API DE PRODUCTOS
     * ============================================================
     */
    private static class ProductosHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {


            if (!"GET".equalsIgnoreCase(
                    exchange.getRequestMethod())) {

                enviarTexto(
                        exchange,
                        405,
                        "Método no permitido",
                        "text/plain; charset=UTF-8"
                );

                return;
            }


            List<ProductoWeb> productos =
                    new ArrayList<>();


            int[] secuencia = {1};


            recorrerCatalogo(
                    CATALOGO,
                    null,
                    productos,
                    secuencia
            );


            String json =
                    convertirAJson(productos);


            enviarTexto(
                    exchange,
                    200,
                    json,
                    "application/json; charset=UTF-8"
            );
        }
    }


    /**
     * ============================================================
     * SERVIDOR DE ARCHIVOS
     * ============================================================
     */
    private static class ArchivosWebHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {


            URI uri =
                    exchange.getRequestURI();


            String ruta =
                    uri.getPath();


            /*
             * "/" -> "/index.html"
             */
            if (ruta == null
                    || ruta.equals("/")) {

                ruta = "/index.html";
            }


            /*
             * Quitar primera barra.
             */
            String relativa =
                    ruta.substring(1);


            /*
             * Crear ruta absoluta.
             */
            Path archivo =
                    CARPETA_WEB
                            .resolve(relativa)
                            .normalize();


            /*
             * Seguridad.
             *
             * Impide:
             *
             * ../archivo
             */
            if (!archivo.startsWith(
                    CARPETA_WEB)) {

                enviarTexto(
                        exchange,
                        403,
                        "Acceso denegado",
                        "text/plain; charset=UTF-8"
                );

                return;
            }


            /*
             * Archivo inexistente.
             */
            if (!Files.exists(archivo)
                    || Files.isDirectory(archivo)) {

                System.err.println(
                        "Archivo no encontrado:"
                );

                System.err.println(
                        archivo
                                .toAbsolutePath()
                );


                enviarTexto(
                        exchange,
                        404,
                        "Archivo no encontrado",
                        "text/plain; charset=UTF-8"
                );

                return;
            }


            /*
             * Leer archivo.
             */
            byte[] contenido =
                    Files.readAllBytes(archivo);


            /*
             * Content-Type.
             */
            Headers headers =
                    exchange.getResponseHeaders();


            headers.set(
                    "Content-Type",
                    tipoContenido(archivo)
            );


            /*
             * Evita caché durante desarrollo.
             */
            headers.set(
                    "Cache-Control",
                    "no-cache"
            );


            /*
             * Enviar respuesta.
             */
            exchange.sendResponseHeaders(
                    200,
                    contenido.length
            );


            try (OutputStream salida =
                         exchange.getResponseBody()) {

                salida.write(contenido);
            }
        }
    }


    /**
     * ============================================================
     * RECORRER CATÁLOGO
     * ============================================================
     */
    private static void recorrerCatalogo(
            Categoria categoria,
            String categoriaPadre,
            List<ProductoWeb> salida,
            int[] secuencia) {


        String nombreCategoria =
                categoria.getNombre();


        /*
         * Productos.
         */
        for (Producto producto :
                categoria.getProductos()) {


            String padre =
                    categoriaPadre == null
                            ? nombreCategoria
                            : categoriaPadre;


            salida.add(
                    new ProductoWeb(
                            secuencia[0]++,
                            producto.getNombre(),
                            producto.getPrecio(),
                            nombreCategoria,
                            padre,
                            emojiPara(
                                    nombreCategoria,
                                    producto.getNombre()
                            )
                    )
            );
        }


        /*
         * Subcategorías.
         */
        for (Categoria subcategoria :
                categoria.getSubcategorias()) {


            String nuevoPadre;


            if ("Catálogo general".equals(
                    categoria.getNombre())) {

                nuevoPadre =
                        subcategoria.getNombre();

            } else {

                nuevoPadre =
                        categoriaPadre == null
                                ? categoria.getNombre()
                                : categoriaPadre;
            }


            recorrerCatalogo(
                    subcategoria,
                    nuevoPadre,
                    salida,
                    secuencia
            );
        }
    }


    /**
     * ============================================================
     * CONVERTIR A JSON
     * ============================================================
     */
    private static String convertirAJson(
            List<ProductoWeb> productos) {


        StringBuilder json =
                new StringBuilder("[");


        for (int i = 0;
             i < productos.size();
             i++) {


            ProductoWeb p =
                    productos.get(i);


            if (i > 0) {
                json.append(",");
            }


            json.append("{")

                    .append("\"id\":")
                    .append(p.id)
                    .append(",")

                    .append("\"name\":\"")
                    .append(
                            escaparJson(
                                    p.nombre
                            )
                    )
                    .append("\",")

                    .append("\"price\":")
                    .append(p.precio)
                    .append(",")

                    .append("\"category\":\"")
                    .append(
                            escaparJson(
                                    p.categoria
                            )
                    )
                    .append("\",")

                    .append("\"parent\":\"")
                    .append(
                            escaparJson(
                                    p.padre
                            )
                    )
                    .append("\",")

                    .append("\"emoji\":\"")
                    .append(
                            escaparJson(
                                    p.emoji
                            )
                    )
                    .append("\"")

                    .append("}");
        }


        json.append("]");


        return json.toString();
    }


    /**
     * Escapa caracteres especiales JSON.
     */
    private static String escaparJson(
            String texto) {

        if (texto == null) {
            return "";
        }


        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }


    /**
     * ============================================================
     * EMOJIS
     * ============================================================
     */
    private static String emojiPara(
            String categoria,
            String producto) {


        if ("Teléfonos".equals(categoria)) {
            return "📱";
        }


        if ("Computadoras".equals(categoria)) {
            return "💻";
        }


        if ("Accesorios".equals(categoria)) {

            String nombre =
                    producto.toLowerCase();

            if (nombre.contains("teclado")) {
                return "⌨️";
            }

            if (nombre.contains("mouse")) {
                return "🖱️";
            }

            return "🎧";
        }


        if ("Hombre".equals(categoria)) {

            String nombre =
                    producto.toLowerCase();

            if (nombre.contains("pantalón")
                    || nombre.contains("pantalon")) {

                return "👖";
            }

            return "👕";
        }


        if ("Mujer".equals(categoria)) {

            String nombre =
                    producto.toLowerCase();

            if (nombre.contains("vestido")) {
                return "👗";
            }

            return "👚";
        }


        if ("Cocina".equals(categoria)) {

            String nombre =
                    producto.toLowerCase();

            if (nombre.contains("olla")) {
                return "🍲";
            }

            return "🥤";
        }


        if ("Iluminación".equals(categoria)) {
            return "💡";
        }


        return "📦";
    }


    /**
     * ============================================================
     * RESPUESTA DE TEXTO
     * ============================================================
     */
    private static void enviarTexto(
            HttpExchange exchange,
            int codigo,
            String contenido,
            String contentType)
            throws IOException {


        byte[] bytes =
                contenido.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        contentType
                );


        exchange.sendResponseHeaders(
                codigo,
                bytes.length
        );


        try (OutputStream salida =
                     exchange.getResponseBody()) {

            salida.write(bytes);
        }
    }


    /**
     * ============================================================
     * CONTENT TYPE
     * ============================================================
     */
    private static String tipoContenido(
            Path archivo) {


        String nombre =
                archivo
                        .getFileName()
                        .toString()
                        .toLowerCase();


        if (nombre.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        }


        if (nombre.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }


        if (nombre.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        }


        if (nombre.endsWith(".json")) {
            return "application/json; charset=UTF-8";
        }


        if (nombre.endsWith(".png")) {
            return "image/png";
        }


        if (nombre.endsWith(".jpg")
                || nombre.endsWith(".jpeg")) {

            return "image/jpeg";
        }


        if (nombre.endsWith(".gif")) {
            return "image/gif";
        }


        if (nombre.endsWith(".svg")) {
            return "image/svg+xml";
        }


        if (nombre.endsWith(".ico")) {
            return "image/x-icon";
        }


        if (nombre.endsWith(".webp")) {
            return "image/webp";
        }


        if (nombre.endsWith(".woff")) {
            return "font/woff";
        }


        if (nombre.endsWith(".woff2")) {
            return "font/woff2";
        }


        if (nombre.endsWith(".ttf")) {
            return "font/ttf";
        }


        return "application/octet-stream";
    }


    /**
     * ============================================================
     * CLASE PRODUCTO WEB
     * ============================================================
     */
    private static class ProductoWeb {

        final int id;

        final String nombre;

        final double precio;

        final String categoria;

        final String padre;

        final String emoji;


        ProductoWeb(
                int id,
                String nombre,
                double precio,
                String categoria,
                String padre,
                String emoji) {

            this.id = id;

            this.nombre = nombre;

            this.precio = precio;

            this.categoria = categoria;

            this.padre = padre;

            this.emoji = emoji;
        }
    }
}
