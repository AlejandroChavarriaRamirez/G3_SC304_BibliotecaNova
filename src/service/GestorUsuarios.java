/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import BibliotecaNova.ListaUsuarios;
import BibliotecaNova.Usuario;
import exceptions.DatosInvalidosException;
import exceptions.UsuarioDuplicadoException;

/**
 * Logica de los usuarios de la biblioteca. Se guardan en una lista doblemente
 * enlazada.
 *
 * @author Grupo 3
 */
public class GestorUsuarios {

    private ListaUsuarios lista;

    //Constructor
    public GestorUsuarios() {
        lista = new ListaUsuarios();
    }

    public ListaUsuarios getLista() {
        return lista;
    }

    //Registra un usuario nuevo
    public Usuario registrarUsuario(String carne, String nombre, String carrera, String telefono)
            throws DatosInvalidosException, UsuarioDuplicadoException {

        if (carne == null || carne.trim().isEmpty()) {
            throw new DatosInvalidosException("El carne es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre es obligatorio.");
        }

        //Revisa si ese carne ya existe
        if (lista.buscarUsuario(carne.trim()) != null) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con el carne " + carne.trim() + ".");
        }

        Usuario usuario = new Usuario();
        usuario.setCarne(carne.trim());
        usuario.setNombre(nombre.trim());
        usuario.setCarrera(carrera);
        usuario.setTelefono(telefono);
        usuario.setEstado("Activo");
        usuario.setAtrasos(0);

        lista.agregarUsuario(usuario);
        return usuario;
    }

    public Usuario buscarUsuario(String carne) {
        if (carne == null) {
            return null;
        }
        return lista.buscarUsuario(carne.trim());
    }

    public boolean eliminarUsuario(String carne) {
        if (carne == null) {
            return false;
        }
        return lista.eliminarUsuario(carne.trim());
    }

    /**
     * Reactiva manualmente a un usuario suspendido: lo deja activo otra vez y
     * le borra los atrasos acumulados.
     */
    public Usuario reactivarUsuario(String carne) throws DatosInvalidosException {

        Usuario usuario = buscarUsuario(carne);

        if (usuario == null) {
            throw new DatosInvalidosException("No existe un usuario con el carne " + carne + ".");
        }

        if (usuario.getEstado().equalsIgnoreCase("activo")) {
            throw new DatosInvalidosException("El usuario " + usuario.getNombre() + " ya esta activo.");
        }

        usuario.setEstado("Activo");
        usuario.setAtrasos(0);
        return usuario;
    }

    public String[][] obtenerMatrizUsuarios() {
        return lista.obtenerMatriz();
    }

    public int contarUsuarios() {
        return lista.contar();
    }

    public int contarSuspendidos() {
        return lista.contarSuspendidos();
    }

    public String recorridoInverso() {
        return lista.recorridoInverso();
    }

}
