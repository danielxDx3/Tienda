# ⚡ ElectroShop - Tienda Electrónica Web

ElectroShop es una tienda electrónica desarrollada como proyecto académico utilizando **HTML, CSS y JavaScript**.

El proyecto presenta un catálogo de productos con categorías, buscador, filtros, ordenamiento y un carrito de compras interactivo.

## 👨‍💻 Autor

**Erick Daniel Moreno Garcia**

## 🛠️ Tecnologías utilizadas

* HTML5
* CSS3
* JavaScript
* LocalStorage
* Google Fonts - Inter

## 📂 Estructura del proyecto

```text
Tienda_electronica_web/
│
├── index.html
│
├── README.md
│
├── css/
│   └── estilos.css
│
├── js/
│   └── app.js
│
└── img/
```

### Descripción de los archivos

* `index.html`: contiene la estructura principal de la página web.
* `css/estilos.css`: contiene todos los estilos, diseño responsive y apariencia visual.
* `js/app.js`: contiene la lógica de productos, búsqueda, filtros, ordenamiento y carrito.
* `img/`: carpeta destinada a almacenar imágenes del proyecto.

## ✨ Funcionalidades

La tienda cuenta con las siguientes características:

* Catálogo de productos.
* 17 productos disponibles.
* Diferentes categorías de productos.
* Buscador por nombre.
* Filtro por categoría.
* Filtro por precio máximo.
* Ordenamiento de productos.
* Carrito de compras.
* Aumento y disminución de cantidades.
* Eliminación de productos del carrito.
* Cálculo automático del total.
* Opción para vaciar el carrito.
* Simulación de finalización de compra.
* Almacenamiento del carrito mediante `localStorage`.
* Menú adaptable para dispositivos móviles.
* Diseño responsive.

## 🛒 Categorías disponibles

El catálogo contiene productos organizados en categorías como:

* Electrónica
* Teléfonos
* Computadoras
* Accesorios
* Ropa
* Hombre
* Mujer
* Hogar
* Cocina
* Iluminación

## 🚀 Cómo ejecutar el proyecto

No es necesario instalar programas adicionales, utilizar una base de datos ni configurar un servidor.

### Opción 1: Ejecutar directamente

1. Descargar o clonar el proyecto.
2. Abrir la carpeta `Tienda_electronica_web`.
3. Buscar el archivo `index.html`.
4. Hacer doble clic sobre el archivo.
5. La página se abrirá en el navegador.

Se recomienda utilizar:

* Google Chrome
* Microsoft Edge
* Opera
* Mozilla Firefox

### Opción 2: Ejecutar desde IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **File → Open**.
3. Seleccionar la carpeta del proyecto.
4. Abrir el archivo `index.html`.
5. Hacer clic derecho.
6. Seleccionar **Open In → Browser**.

## 💾 LocalStorage

El carrito de compras utiliza `localStorage`.

Esto permite conservar los productos agregados al carrito incluso si se actualiza o se cierra temporalmente la página.

La información se almacena en el navegador con la clave:

```javascript
electroshop-cart
```

## 🔎 Buscador y filtros

El usuario puede buscar productos escribiendo palabras como:

```text
iPhone
Laptop
Samsung
Audífonos
```

También puede combinar la búsqueda con filtros de:

* Categoría
* Precio máximo
* Orden de precio
* Orden alfabético

## 📱 Diseño responsive

La página está preparada para adaptarse a diferentes tamaños de pantalla:

* Computadoras
* Laptops
* Tablets
* Teléfonos móviles

En dispositivos pequeños, el menú y los filtros cambian su comportamiento para facilitar la navegación.

## ℹ️ Información adicional

Este proyecto corresponde a una versión web de una tienda electrónica y fue desarrollado con fines académicos.

Actualmente:

* No utiliza servidor.
* No utiliza Java.
* No utiliza una base de datos.
* No realiza pagos reales.
* La finalización de compra es solamente una simulación.

## 📌 Estado del proyecto

✅ Página principal
✅ Catálogo de productos
✅ Buscador
✅ Filtros
✅ Ordenamiento
✅ Carrito de compras
✅ LocalStorage
✅ Diseño responsive
✅ Simulación de compra

---

© 2026 Erick Daniel Moreno Garcia. Todos los derechos reservados.
