package javaapplication1.modelo;

import javaapplication1.abstracta.EntidadBase;

/**
 * Clase que representa a un usuario administrativo del sistema.
 * Permite gestionar las credenciales de acceso.
 */
public class Usuario extends EntidadBase {
    private String nombreReal;
    private String nombreUsuario;
    private String password;
    private String rol; // Ejemplo: "Admin", "Empleado"

    /**
     * Constructor para usuarios NUEVOS.
     */
    public Usuario(String nombreReal, String nombreUsuario, String password, String rol) {
        super(); // Inicializa fechas de auditoría
        setNombreReal(nombreReal);
        setNombreUsuario(nombreUsuario);
        setPassword(password);
        setRol(rol);
    }

    /**
     * Constructor para usuarios EXISTENTES (traídos de la BD).
     */
    public Usuario(int id, String nombreReal, String nombreUsuario, String password, String rol) {
        super(id); // Valida ID positivo
        setNombreReal(nombreReal);
        setNombreUsuario(nombreUsuario);
        setPassword(password);
        setRol(rol);
    }

    // --- Getters y Setters con validaciones ---

    public String getNombreReal() { return nombreReal; }
    public void setNombreReal(String nombreReal) {
        if (nombreReal == null || nombreReal.isBlank()) {
            throw new IllegalArgumentException("El nombre real es obligatorio");
        }
        this.nombreReal = nombreReal;
        actualizarFecha();
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.length() < 4) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 4 caracteres");
        }
        this.nombreUsuario = nombreUsuario;
        actualizarFecha();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        this.password = password;
        actualizarFecha();
    }

    public String getRol() { return rol; }
    public void setRol(String rol) {
        if (rol == null || rol.isBlank()) {
            this.rol = "EMPLEADO"; // Valor por defecto
        } else {
            this.rol = rol.toUpperCase();
        }
        actualizarFecha();
    }

    @Override
    public String mostrarInformacion() {
        return String.format("ID: %d | Usuario: %s | Rol: %s", 
                getId(), nombreUsuario, rol);
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + getId() + ", nombreUsuario='" + nombreUsuario + '\'' + '}';
    }

    public String getUsername() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getTipo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setTipo(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setUsername(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}