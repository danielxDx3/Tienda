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
import java.util.ArrayList;
import java.util.List;

/**
 * Servidor web de la Tienda Electrónica.
 *
 * Ejecuta el sitio HTML/CSS/JS y expone el catálogo Java en:
 *   GET /api/productos
 *
 * No necesita Spring, Maven ni librerías externas.
 */
public class ServidorWeb {

    private static final int PUERTO = 8085;
    // Localiza automáticamente la carpeta web, aunque IntelliJ ejecute el proyecto
    // desde una carpeta padre (caso común al descomprimir el ZIP).
    private static final Path CARPETA_WEB = localizarCarpetaWeb();
    private static final Categoria CATALOGO = Main.crearCatalogo();


    /**
     * Busca la carpeta web desde el directorio de trabajo actual y algunos
     * niveles cercanos. Esto evita el error "Archivo no encontrado" cuando
     * IntelliJ abre una carpeta exterior al proyecto real.
     */
    private static Path localizarCarpetaWeb() {
        Path actual = Paths.get("").toAbsolutePath().normalize();

        // 1) Proyecto abierto directamente: <proyecto>/web
        Path directa = actual.resolve("web");
        if (Files.isRegularFile(directa.resolve("index.html"))) {
            return directa.normalize();
        }

        // 2) Proyecto abierto con una carpeta contenedora extra:
        //    <carpeta>/Tienda_electronica_completo/web
        Path anidada = actual.resolve("Tienda_electronica_completo").resolve("web");
        if (Files.isRegularFile(anidada.resolve("index.html"))) {
            return anidada.normalize();
        }

        // 3) Buscar en subcarpetas inmediatas por si la carpeta tiene otro nombre.
        try {
            if (Files.isDirectory(actual)) {
                try (java.util.stream.Stream<Path> hijos = Files.list(actual)) {
                    Path encontrada = hijos
                            .filter(Files::isDirectory)
                            .map(dir -> dir.resolve("web"))
                            .filter(web -> Files.isRegularFile(web.resolve("index.html")))
                            .findFirst()
                            .orElse(null);
                    if (encontrada != null) {
                        return encontrada.toAbsolutePath().normalize();
                    }
                }
            }
        } catch (IOException ignored) {
            // Se usará la ruta directa para mostrar un mensaje claro al iniciar.
        }

        return directa.normalize();
    }

    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(PUERTO), 0);

        servidor.createContext("/api/productos", new ProductosHandler());
        servidor.createContext("/", new ArchivosWebHandler());

        servidor.setExecutor(null);
        servidor.start();

        System.out.println("============================================");
        System.out.println("       TIENDA ELECTRÓNICA - WEB + JAVA");
        System.out.println("============================================");
        System.out.println("Servidor iniciado correctamente.");
        System.out.println("Carpeta web detectada: " + CARPETA_WEB);
        System.out.println("Abrir en el navegador: http://localhost:" + PUERTO);
        System.out.println("API de productos:       http://localhost:" + PUERTO + "/api/productos");
        System.out.println("Presione Ctrl + C para detener el servidor.");
    }

    /** Devuelve los productos creados en Java como JSON. */
    private static class ProductosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarTexto(exchange, 405, "Método no permitido", "text/plain; charset=UTF-8");
                return;
            }

            List<ProductoWeb> productos = new ArrayList<>();
            int[] secuencia = {1};
            recorrerCatalogo(CATALOGO, null, productos, secuencia);

            String json = convertirAJson(productos);
            enviarTexto(exchange, 200, json, "application/json; charset=UTF-8");
        }
    }

    /** Sirve index.html, CSS, JS e imágenes desde la carpeta web. */
    private static class ArchivosWebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String rutaSolicitada = uri.getPath();

            if (rutaSolicitada == null || "/".equals(rutaSolicitada)) {
                rutaSolicitada = "/index.html";
            }

            Path archivo = CARPETA_WEB.resolve(rutaSolicitada.substring(1)).normalize();

            // Seguridad: impedir salir de la carpeta web con rutas tipo ../
            if (!archivo.startsWith(CARPETA_WEB)) {
                enviarTexto(exchange, 403, "Acceso denegado", "text/plain; charset=UTF-8");
                return;
            }

            if (!Files.exists(archivo) || Files.isDirectory(archivo)) {
                enviarTexto(exchange, 404, "Archivo no encontrado", "text/plain; charset=UTF-8");
                return;
            }

            byte[] contenido = Files.readAllBytes(archivo);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", tipoContenido(archivo));
            headers.set("Cache-Control", "no-cache");

            exchange.sendResponseHeaders(200, contenido.length);
            try (OutputStream salida = exchange.getResponseBody()) {
                salida.write(contenido);
            }
        }
    }

    private static void recorrerCatalogo(
            Categoria categoria,
            String categoriaPadre,
            List<ProductoWeb> salida,
            int[] secuencia) {

        String nombreCategoria = categoria.getNombre();

        for (Producto producto : categoria.getProductos()) {
            String padre = categoriaPadre == null ? nombreCategoria : categoriaPadre;
            salida.add(new ProductoWeb(
                    secuencia[0]++,
                    producto.getNombre(),
                    producto.getPrecio(),
                    nombreCategoria,
                    padre,
                    emojiPara(nombreCategoria, producto.getNombre())
            ));
        }

        for (Categoria subcategoria : categoria.getSubcategorias()) {
            String nuevoPadre;
            if ("Catálogo general".equals(categoria.getNombre())) {
                nuevoPadre = subcategoria.getNombre();
            } else {
                nuevoPadre = categoriaPadre == null ? categoria.getNombre() : categoriaPadre;
            }
            recorrerCatalogo(subcategoria, nuevoPadre, salida, secuencia);
        }
    }

    private static String convertirAJson(List<ProductoWeb> productos) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < productos.size(); i++) {
            ProductoWeb p = productos.get(i);
            if (i > 0) json.append(',');

            json.append('{')
                    .append("\"id\":").append(p.id).append(',')
                    .append("\"name\":\"").append(escaparJson(p.nombre)).append("\",")
                    .append("\"price\":").append(p.precio).append(',')
                    .append("\"category\":\"").append(escaparJson(p.categoria)).append("\",")
                    .append("\"parent\":\"").append(escaparJson(p.padre)).append("\",")
                    .append("\"emoji\":\"").append(escaparJson(p.emoji)).append("\"")
                    .append('}');
        }

        return json.append(']').toString();
    }

    private static String escaparJson(String texto) {
        if (texto == null) return "";
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String emojiPara(String categoria, String producto) {
        if ("Teléfonos".equals(categoria)) return "📱";
        if ("Computadoras".equals(categoria)) return "💻";
        if ("Accesorios".equals(categoria)) {
            if (producto.toLowerCase().contains("teclado")) return "⌨️";
            if (producto.toLowerCase().contains("mouse")) return "🖱️";
            return "🎧";
        }
        if ("Hombre".equals(categoria)) return producto.toLowerCase().contains("pantalón") ? "👖" : "👕";
        if ("Mujer".equals(categoria)) return producto.toLowerCase().contains("vestido") ? "👗" : "👚";
        if ("Cocina".equals(categoria)) return producto.toLowerCase().contains("olla") ? "🍲" : "🥤";
        if ("Iluminación".equals(categoria)) return "💡";
        return "📦";
    }

    private static void enviarTexto(
            HttpExchange exchange,
            int codigo,
            String contenido,
            String contentType) throws IOException {

        byte[] bytes = contenido.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(codigo, bytes.length);

        try (OutputStream salida = exchange.getResponseBody()) {
            salida.write(bytes);
        }
    }

    private static String tipoContenido(Path archivo) {
        String nombre = archivo.getFileName().toString().toLowerCase();
        if (nombre.endsWith(".html")) return "text/html; charset=UTF-8";
        if (nombre.endsWith(".css")) return "text/css; charset=UTF-8";
        if (nombre.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (nombre.endsWith(".json")) return "application/json; charset=UTF-8";
        if (nombre.endsWith(".png")) return "image/png";
        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) return "image/jpeg";
        if (nombre.endsWith(".gif")) return "image/gif";
        if (nombre.endsWith(".svg")) return "image/svg+xml";
        if (nombre.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    private static class ProductoWeb {
        final int id;
        final String nombre;
        final double precio;
        final String categoria;
        final String padre;
        final String emoji;

        ProductoWeb(int id, String nombre, double precio, String categoria, String padre, String emoji) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.categoria = categoria;
            this.padre = padre;
            this.emoji = emoji;
        }
    }
}
