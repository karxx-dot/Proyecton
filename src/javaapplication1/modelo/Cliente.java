package javaapplication1.modelo;

import javaapplication1.abstracta.EntidadBase;


public class Cliente extends EntidadBase {
    private String nombre;
    private String cedula;
    private String telefono;
    private String email;

    
    public Cliente(String nombre, String cedula, String telefono, String email) {
        super();
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setEmail(email);
    }

    public Cliente(int id, String nombre, String cedula, String telefono, String email) {
        super(id); 
        setNombre(nombre);
        setCedula(cedula);
        setTelefono(telefono);
        setEmail(email);
    }

    // --- Getters y Setters  ---

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