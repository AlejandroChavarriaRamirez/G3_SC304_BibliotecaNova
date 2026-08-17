package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class Reserva {

    private String codigoReserva;
    private String carneUsuario;
    private String codigoLibro;
    private String fechaReserva;

    public Reserva() {
        this.codigoReserva = "";
        this.carneUsuario = "";
        this.codigoLibro = "";
        this.fechaReserva = "";
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
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

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

}
