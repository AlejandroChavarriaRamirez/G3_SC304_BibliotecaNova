package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class Usuario {

    private String carne;
    private String nombre;
    private String carrera;
    private String telefono;
    private String estado;
    private int atrasos;

    public Usuario() {
        this.carne = "";
        this.nombre = "";
        this.carrera = "";
        this.telefono = "";
        this.estado = "Activo";
        this.atrasos = 0;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(int atrasos) {
        this.atrasos = atrasos;
    }

}
