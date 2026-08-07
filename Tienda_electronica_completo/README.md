# Tienda Electrónica — Java + Sitio Web en un solo proyecto

Este proyecto une el programa Java original y el sitio HTML/CSS/JavaScript.

## Estructura

```text
Tienda_electronica_completo/
├── src/
│   └── ec/edu/puce/modelo/
│       ├── Categoria.java
│       ├── Main.java
│       ├── Producto.java
│       ├── ResultadoBusqueda.java
│       └── ServidorWeb.java
├── web/
│   ├── index.html
│   ├── css/estilos.css
│   ├── js/app.js
│   └── img/
├── ejecutar.bat
├── ejecutar.sh
└── README.md
```

## Cómo ejecutar la versión web

### En IntelliJ IDEA

1. Abre la carpeta `Tienda_electronica_completo`.
2. Configura un JDK (Java 8 o superior).
3. Ejecuta la clase:
   `ec.edu.puce.modelo.ServidorWeb`
4. Abre en el navegador:
   `http://localhost:8085`

> Importante: el programa debe ejecutarse tomando la carpeta raíz del proyecto como **Working Directory**, porque ahí se encuentra la carpeta `web`.

### En Windows

Haz doble clic en `ejecutar.bat` y luego abre:

`http://localhost:8085`

## Cómo ejecutar la versión de consola

Ejecuta la clase:

`ec.edu.puce.modelo.Main`

## Integración realizada

- Java mantiene la estructura de categorías y productos.
- `ServidorWeb.java` crea un servidor HTTP local.
- `/api/productos` transforma el catálogo Java a JSON.
- `app.js` obtiene los productos con `fetch('/api/productos')`.
- HTML, CSS, filtros, carrito y localStorage siguen funcionando.
- Ya no existe un catálogo duplicado escrito manualmente en JavaScript.

## API

`GET http://localhost:8085/api/productos`

Devuelve los productos definidos en Java.
