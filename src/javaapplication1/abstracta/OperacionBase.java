package javaapplication1.abstracta;

import javaapplication1.modelo.Cuota;
import java.util.List;


public interface OperacionBase {

    double calcularCuota(int numeroCuota);

    double calcularInteres(int numeroCuota);

    double calcularSaldo(int numeroCuota);

    List<Cuota> generarCronograma();
}
