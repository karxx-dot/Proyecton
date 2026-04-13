package javaapplication1.abstracta;

import javaapplication1.modelo.Cuota;
import java.util.List;

/**
 * Interfaz que define el contrato financiero del sistema.
 * 
 * Persona 2 garantiza que cualquier tipo de préstamo
 * sepa calcular sus componentes básicos y generar
 * un cronograma de pagos completo.
 */
public interface OperacionBase {

    /**
     * Calcula el monto total de la cuota (Capital + Interés).
     * 
     * @param numeroCuota número de la cuota (1, 2, 3, ...)
     * @return monto total de la cuota
     */
    double calcularCuota(int numeroCuota);

    /**
     * Calcula solo la parte del interés de la cuota.
     * 
     * @param numeroCuota número de la cuota
     * @return monto del interés correspondiente
     */
    double calcularInteres(int numeroCuota);

    /**
     * Calcula cuánto dinero falta por pagar después de esta cuota.
     * 
     * @param numeroCuota número de la cuota
     * @return saldo restante después de aplicar el pago
     */
    double calcularSaldo(int numeroCuota);

    List<Cuota> generarCronograma();
}
