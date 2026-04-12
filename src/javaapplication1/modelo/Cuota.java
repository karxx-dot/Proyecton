package javaapplication1.modelo;

import javaapplication1.abstracta.EntidadBase;
import java.time.LocalDate;

public class Cuota extends EntidadBase {
    private int numero;
    private LocalDate fecha;
    private double capital;
    private double interes;
    private double montoTotal; // Cambiado de 'cuota' para evitar confusión con el nombre de la clase
    private double saldoPendiente;

    /**
     * Constructor para el Simulador (Registros nuevos sin ID de BD).
     */
    public Cuota(int numero, LocalDate fecha, double capital, double interes, double montoTotal, double saldoPendiente) {
        super(); // Genera fechas de creación automáticamente
        this.numero = numero;
        this.fecha = fecha;
        setCapital(capital);
        setInteres(interes);
        setMontoTotal(montoTotal);
        setSaldoPendiente(saldoPendiente);
    }

    /**
     * Constructor para registros existentes en la Base de Datos.
     */
    public Cuota(int id, int numero, LocalDate fecha, double capital, double interes, double montoTotal, double saldoPendiente) {
        super(id);
        this.numero = numero;
        this.fecha = fecha;
        setCapital(capital);
        setInteres(interes);
        setMontoTotal(montoTotal);
        setSaldoPendiente(saldoPendiente);
    }

    // Método privado para asegurar precisión de 2 decimales
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    // Getters y Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; actualizarFecha(); }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; actualizarFecha(); }

    public double getCapital() { return capital; }
    public void setCapital(double capital) { 
        this.capital = redondear(capital); 
        actualizarFecha(); 
    }

    public double getInteres() { return interes; }
    public void setInteres(double interes) { 
        this.interes = redondear(interes); 
        actualizarFecha(); 
    }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { 
        this.montoTotal = redondear(montoTotal); 
        actualizarFecha(); 
    }

    public double getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(double saldo) { 
        this.saldoPendiente = redondear(saldo); 
        actualizarFecha(); 
    }

    @Override
    public String mostrarInformacion() {
        return String.format("Cuota #%d | Fecha: %s | Total: %.2f | Saldo: %.2f", 
                numero, fecha, montoTotal, saldoPendiente);
    }
}