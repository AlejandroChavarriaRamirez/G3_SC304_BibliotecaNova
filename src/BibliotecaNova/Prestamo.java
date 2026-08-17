package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class Prestamo {

    private String codigoPrestamo;
    private String carneUsuario;
    private String codigoLibro;
    private String codigoEjemplar;
    private String fechaPrestamo;
    private String fechaVencimiento;
    private String estado;

    public Prestamo() {
        this.codigoPrestamo = "";
        this.carneUsuario = "";
        this.codigoLibro = "";
        this.codigoEjemplar = "";
        this.fechaPrestamo = "";
        this.fechaVencimiento = "";
        this.estado = "Activo";
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

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
