package com.proyecto.prestamos.modelo;

import com.proyecto.prestamos.abstracta.OperacionBase;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un préstamo dentro del sistema.
 * 
 * Implementa la interfaz OperacionBase para garantizar
 * cálculos uniformes de cuotas, intereses y saldos.
 */
public class Prestamo implements OperacionBase {
    protected double monto;
    protected int numeroCuotas;
    protected double tasaInteresAnual; // en %
    protected LocalDate fechaInicio;

    // Constructor
    public Prestamo(double monto, int numeroCuotas, double tasaInteresAnual, LocalDate fechaInicio) {
        if (monto <= 0 || numeroCuotas <= 0 || tasaInteresAnual <= 0) {
            throw new IllegalArgumentException("Datos inválidos para el préstamo");
        }
        this.monto = monto;
        this.numeroCuotas = numeroCuotas;
        this.tasaInteresAnual = tasaInteresAnual;
        this.fechaInicio = fechaInicio;
    }

    // Getters
    public double getMonto() { return monto; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public double getTasaInteresAnual() { return tasaInteresAnual; }
    public LocalDate getFechaInicio() { return fechaInicio; }

    // Fórmula de amortización francesa (cuota fija)
    @Override
    public double calcularCuota(int numeroCuota) {
        double tasaMensual = tasaInteresAnual / 100.0 / 12.0;
        return redondear(monto * (tasaMensual / (1 - Math.pow(1 + tasaMensual, -numeroCuotas))));
    }

    @Override
    public double calcularInteres(int numeroCuota) {
        double tasaMensual = tasaInteresAnual / 100.0 / 12.0;
        double saldoPendiente = calcularSaldo(numeroCuota - 1);
        return redondear(saldoPendiente * tasaMensual);
    }

    @Override
    public double calcularSaldo(int numeroCuota) {
        double cuota = calcularCuota(1);
        double tasaMensual = tasaInteresAnual / 100.0 / 12.0;
        double saldo = monto;
        for (int i = 1; i <= numeroCuota; i++) {
            double interes = saldo * tasaMensual;
            double capital = cuota - interes;
            saldo -= capital;
        }
        return saldo < 0 ? 0 : redondear(saldo);
    }

    @Override
    public List<Cuota> generarCronograma() {
        List<Cuota> cronograma = new ArrayList<>();
        double cuotaFija = calcularCuota(1);
        double saldo = monto;

        for (int i = 1; i <= numeroCuotas; i++) {
            double interes = redondear(saldo * (tasaInteresAnual / 100.0 / 12.0));
            double capital = redondear(cuotaFija - interes);
            saldo = redondear(saldo - capital);

            // Todas las cuotas el mismo día del mes (día de fechaInicio)
            LocalDate fechaPago = fechaInicio.plusMonths(i);

            // Ajuste de seguridad: última cuota deja saldo en 0 exacto
            if (i == numeroCuotas) saldo = 0;

            cronograma.add(new Cuota(i, fechaPago, capital, interes, cuotaFija, saldo));
        }
        return cronograma;
    }

    // Método auxiliar para redondear a 2 decimales
    protected double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}