package com.proyecto.prestamos.abstracta;

import java.time.LocalDateTime;

/**
 * Clase abstracta que define atributos comunes
 * para todas las entidades del sistema de préstamos.
 * 
 * Incluye control de ID, fechas de creación y actualización,
 * y obliga a las clases hijas a implementar un método
 * para mostrar información personalizada.
 */
public abstract class EntidadBase {
    private int id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /**
     * Constructor para nuevos registros (ej. simulador de préstamos).
     * Asigna automáticamente las fechas de creación y actualización.
     */
    public EntidadBase() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Constructor para registros existentes (ej. datos traídos de la BD).
     * @param id identificador único de la entidad
     */
    public EntidadBase(int id) {
        this();
        setId(id); // validación incluida
    }

    // Getters y Setters
    public int getId() { return id; }

    /**
     * Asigna un ID positivo a la entidad.
     * @param id identificador único
     * @throws IllegalArgumentException si el ID es <= 0
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    /**
     * Actualiza la fecha de modificación de la entidad.
     */
    public void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Método abstracto que obliga a las clases hijas
     * a mostrar información personalizada.
     * @return cadena con información de la entidad
     */
    public abstract String mostrarInformacion();

    /**
     * Representación en texto de la entidad.
     * Útil para depuración en consola.
     */
    @Override
    public String toString() {
        return "ID: " + id +
               " | Creado: " + fechaCreacion +
               " | Actualizado: " + fechaActualizacion;
    }
}