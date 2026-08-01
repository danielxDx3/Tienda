# Tienda electronica
# Sistema de categorías y productos

## Descripción general

Este proyecto representa el catálogo de una tienda mediante una estructura de categorías, subcategorías y productos.

El catálogo funciona como un árbol. En la parte superior se encuentra la categoría principal llamada **Catálogo general**. Dentro de ella se encuentran las categorías principales:

* Electrónica
* Ropa
* Hogar

Cada categoría puede contener otras subcategorías y también productos. Por ejemplo, la categoría **Electrónica** contiene las subcategorías **Teléfonos**, **Computadoras** y **Accesorios**.

## Funcionamiento del programa

Al ejecutar el programa se crea automáticamente el catálogo con todas sus categorías, subcategorías y productos.

Después aparece un menú con las siguientes opciones:

1. Mostrar productos y categorías.
2. Buscar un producto.
3. Buscar una categoría.
4. Salir del programa.

El usuario selecciona una opción escribiendo su número correspondiente.

## Mostrar productos y categorías

Esta opción muestra todo el catálogo en forma de árbol.

Primero se presenta la categoría principal y debajo aparecen sus categorías, subcategorías y productos. La separación visual permite identificar a qué categoría pertenece cada producto.

Por ejemplo, un teléfono aparecerá dentro de la ruta:

**Catálogo general > Electrónica > Teléfonos**

Mientras que una licuadora aparecerá dentro de:

**Catálogo general > Hogar > Cocina**

## Búsqueda de productos

Para buscar un producto, el usuario selecciona la opción **Buscar producto** y escribe el nombre completo o una parte del nombre.

No es necesario escribir exactamente todo el nombre del producto. Por ejemplo:

* Al escribir `Laptop`, el sistema encuentra **Laptop Lenovo** y **Laptop HP**.
* Al escribir `Samsung`, encuentra **Samsung Galaxy S24**.
* Al escribir `inalámbrico`, encuentra **Mouse inalámbrico**.
* Al escribir `lámpara`, encuentra **Lámpara LED**.

La búsqueda recorre todas las categorías y subcategorías del catálogo. Por cada coincidencia se muestra:

* El nombre del producto.
* Su precio.
* La ruta de categorías donde se encuentra.

La búsqueda no distingue entre mayúsculas y minúsculas. También elimina las diferencias provocadas por las tildes. Por ello, escribir `lampara` puede encontrar el producto **Lámpara LED**.

Cuando no existe ningún producto que coincida con el texto ingresado, el programa muestra el mensaje **No se encontraron productos**.

## Búsqueda de categorías

Para buscar una categoría, el usuario selecciona la opción **Buscar categoría** e ingresa su nombre.

En este caso, la búsqueda utiliza el nombre completo de la categoría. Algunos ejemplos son:

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

El sistema comienza en la categoría principal y recorre todas las subcategorías hasta encontrar una coincidencia.

La búsqueda tampoco distingue entre mayúsculas, minúsculas o tildes. Por ejemplo, se puede escribir `electronica` para encontrar la categoría **Electrónica**.

Cuando encuentra la categoría, muestra:

* El nombre de la categoría.
* Los productos que pertenecen directamente a ella.
* Las subcategorías que contiene.

Por ejemplo, al buscar **Electrónica**, se muestran las subcategorías Teléfonos, Computadoras y Accesorios. Como los productos están dentro de estas subcategorías, el programa indica que Electrónica no posee productos directos.

Al buscar **Teléfonos**, se muestran directamente productos como iPhone 15, Samsung Galaxy S24 y Xiaomi Redmi Note 13.

Si la categoría ingresada no existe, aparece el mensaje **Categoría no encontrada**.

## Organización de los productos

Cada producto contiene dos datos principales:

* Nombre.
* Precio.

Al mostrarse en pantalla, el precio aparece con dos decimales. Por ejemplo:

**Laptop Lenovo - $750.00**

El sistema solamente permite establecer precios iguales o mayores que cero.

## Ruta de un producto

Cada resultado de búsqueda guarda el producto encontrado y su ubicación dentro del catálogo.

Esta ubicación se denomina **ruta** y permite saber exactamente en qué categoría se encuentra el producto.

Por ejemplo:

**Producto:** MacBook Air - $1199.00
**Ruta:** Catálogo general > Electrónica > Computadoras

La ruta es especialmente útil cuando existen muchas categorías o productos con nombres similares.

## Validaciones del sistema

El programa incluye las siguientes validaciones:

* No permite realizar búsquedas vacías.
* Evita agregar categorías repetidas dentro de una misma categoría.
* No permite agregar categorías vacías.
* No permite agregar productos vacíos.
* Controla las opciones incorrectas del menú.
* Informa cuando un producto o categoría no existe.
* No diferencia mayúsculas, minúsculas ni tildes durante las búsquedas.

## Estructura utilizada

La organización del catálogo es una estructura no lineal de tipo árbol.

Una categoría puede contener:

* Una lista de productos.
* Una lista de subcategorías.

Cada subcategoría puede contener nuevas subcategorías, permitiendo ampliar el catálogo sin cambiar su funcionamiento principal.

La búsqueda se realiza de manera recursiva. Esto significa que el sistema revisa una categoría y después continúa revisando cada una de sus subcategorías hasta encontrar el elemento solicitado o terminar de recorrer todo el catálogo.
