package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class Ejemplar {

    private String codigoEjemplar;
    private String estado;

    public Ejemplar() {
        this.codigoEjemplar = "";
        this.estado = "Disponible";
    }

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public void setCodigoEjemplar(String codigoEjemplar) {
        this.codigoEjemplar = codigoEjemplar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
