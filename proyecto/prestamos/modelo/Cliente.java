package com.proyecto.prestamos.modelo;

import com.proyecto.prestamos.abstracta.EntidadBase;

/**
 * Clase que representa a un cliente dentro del sistema de préstamos.
 * * Persona 2 garantiza que los datos básicos estén validados y que
 * la entidad mantenga su rastro de auditoría mediante EntidadBase.
 */
public class Cliente extends EntidadBase {
    private String nombre;
    private String cedula;
    private String telefono;
    private String email;

    /**
     * Constructor para clientes NUEVOS (antes de guardarlos en BD).
     * El ID se queda en 0 por defecto hasta que la BD lo asigne.
     */
    public Cliente(String nombre, String cedula, String telefono, String email) {
        super(); // Inicializa las fechas de creación
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setEmail(email);
    }

    /**
     * Constructor para clientes EXISTENTES (traídos de la BD).
     * @param id identificador único ya existente.
     */
    public Cliente(int id, String nombre, String cedula, String telefono, String email) {
        super(id); // Valida que el ID sea positivo
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setEmail(email);
    }

    // --- Getters y Setters con lógica de negocio ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nombre;
        actualizarFecha();
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        this.cedula = cedula;
        actualizarFecha();
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        this.telefono = telefono;
        actualizarFecha();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        // Validación básica de formato de email
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        this.email = email;
        actualizarFecha();
    }

    @Override
    public String mostrarInformacion() {
        return String.format("ID: %d | Cliente: %s | Cédula: %s", 
                getId(), nombre, cedula);
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + getId() + ", nombre='" + nombre + '\'' + '}';
    }
}