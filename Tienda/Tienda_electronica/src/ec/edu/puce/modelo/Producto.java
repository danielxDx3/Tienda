package ec.edu.puce.modelo;

public class Producto {

    private String nombre;
    private double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {

        if (precio >= 0) {
            this.precio = precio;
        }
    }

    public void mostrarProducto() {

        System.out.printf(
                "Producto: %s | Precio: $%.2f%n",
                nombre,
                precio
        );
    }

    @Override
    public String toString() {

        return String.format(
                "%s - $%.2f",
                nombre,
                precio
        );
    }
}