package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class Devolucion {

    private String codigoPrestamo;
    private String carneUsuario;
    private String codigoLibro;
    private String codigoEjemplar;
    private String fechaDevolucion;
    private boolean atrasado;
    private double multa;

    public Devolucion() {
        this.codigoPrestamo = "";
        this.carneUsuario = "";
        this.codigoLibro = "";
        this.codigoEjemplar = "";
        this.fechaDevolucion = "";
        this.atrasado = false;
        this.multa = 0;
    }

    public String getCodigoPrestamo() {
        return codigoPrestamo;
    }

    public void setCodigoPrestamo(String codigoPrestamo) {
        this.codigoPrestamo = codigoPrestamo;
    }

    public String getCarneUsuario() {
        return carneUsuario;
    }

    public void setCarneUsuario(String carneUsuario) {
        this.carneUsuario = carneUsuario;
    }

    public String getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(String codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public void setCodigoEjemplar(String codigoEjemplar) {
        this.codigoEjemplar = codigoEjemplar;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

}
