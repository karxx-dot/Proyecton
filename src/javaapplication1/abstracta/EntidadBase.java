/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1.abstracta;

import java.time.LocalDateTime;

public abstract class EntidadBase {
    private int id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public EntidadBase() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public EntidadBase(int id) {
        this();
        setId(id);
    }

    public int getId() { return id; }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public abstract String mostrarInformacion();

    @Override
    public String toString() {
        return "ID: " + id +
               " | Creado: " + fechaCreacion +
               " | Actualizado: " + fechaActualizacion;
    }
}