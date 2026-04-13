package javaapplication1.modelo;

import javaapplication1.abstracta.EntidadBase;

public class Usuario extends EntidadBase {
    private String nombreReal;
    private String nombreUsuario;
    private String password;
    private String rol;

    // Constructor para usuarios NUEVOS
    public Usuario(String nombreReal, String nombreUsuario, String password, String rol) {
        this.nombreReal    = nombreReal;
        this.nombreUsuario = nombreUsuario;
        this.password      = password;
        this.rol           = rol;
    }

    // Constructor para usuarios EXISTENTES (traídos de la BD)
    public Usuario(int id, String nombreReal, String nombreUsuario, String password, String rol) {
        super(id);
        this.nombreReal    = nombreReal;
        this.nombreUsuario = nombreUsuario;
        this.password      = password;
        this.rol           = rol;
    }

    // Getters
    public String getNombreReal()    { return nombreReal; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getPassword()      { return password; }
    public String getRol()           { return rol; }
    public String getUsername()      { return nombreUsuario; }
    public String getTipo()          { return rol; }

    // Setters
    public void setNombreReal(String nombreReal) {
        if (nombreReal == null || nombreReal.isBlank())
            throw new IllegalArgumentException("El nombre real es obligatorio");
        this.nombreReal = nombreReal;
        actualizarFecha();
    }

    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.length() < 4)
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 4 caracteres");
        this.nombreUsuario = nombreUsuario;
        actualizarFecha();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        this.password = password;
        actualizarFecha();
    }

    public void setRol(String rol) {
        this.rol = (rol == null || rol.isBlank()) ? "Usuario" : rol;
        actualizarFecha();
    }

    public void setTipo(String tipo)         { this.rol = tipo; }
    public void setUsername(String username) { this.nombreUsuario = username; }

    @Override
    public String mostrarInformacion() {
        return String.format("ID: %d | Usuario: %s | Rol: %s", getId(), nombreUsuario, rol);
    }

    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombreUsuario='" + nombreUsuario + "'}";
    }
}