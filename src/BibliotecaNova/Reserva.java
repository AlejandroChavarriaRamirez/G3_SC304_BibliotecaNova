/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

import java.time.LocalDate;

/**
 * Reserva de un libro que en este momento no tiene ejemplares disponibles.
 *
 * @author aleja
 */
public class Reserva {

    private String codigoReserva;
    private String carneUsuario;
    private String codigoLibro;
    private LocalDate fechaReserva;

    public Reserva() {
        this.codigoReserva = "";
        this.carneUsuario = "";
        this.codigoLibro = "";
        this.fechaReserva = LocalDate.now();
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

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

}
