package ec.edu.puce.modelo;

public class ResultadoBusqueda {

    private Producto producto;
    private String ruta;

    public ResultadoBusqueda(
            Producto producto,
            String ruta
    ) {
        this.producto = producto;
        this.ruta = ruta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void mostrarResultado() {

        System.out.println(producto);
        System.out.println("Ruta: " + ruta);
    }

    @Override
    public String toString() {

        return producto
                + "\nRuta: "
                + ruta;
    }
}