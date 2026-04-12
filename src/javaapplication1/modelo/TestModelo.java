package javaapplication1.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Clase de prueba para verificar el funcionamiento
 * del módulo de préstamos y cronograma de cuotas.
 */
public class TestModelo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Pedir datos del cliente
        System.out.println("=== Registro de Cliente ===");
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Cédula: ");
        String cedula = sc.nextLine();

        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        Cliente cliente = new Cliente(id, nombre, cedula, telefono, email);

        // Pedir datos del préstamo
        System.out.println("\n=== Registro de Préstamo ===");
        System.out.print("Monto: ");
        double monto = sc.nextDouble();

        System.out.print("Número de cuotas: ");
        int cuotas = sc.nextInt();

        System.out.print("Tasa de interés anual (%): ");
        double tasa = sc.nextDouble();

        Prestamo prestamo = new Prestamo(monto, cuotas, tasa, LocalDate.now());

        // Generar cronograma
        List<Cuota> cronograma = prestamo.generarCronograma();

        // Mostrar información del cliente
        System.out.println("\n=== Información del Cliente ===");
        System.out.println(cliente.mostrarInformacion());

        // Mostrar cronograma de cuotas
        System.out.println("\n=== Cronograma de Préstamo ===");
        for (Cuota c : cronograma) {
            System.out.println(c.mostrarInformacion());
        }

        sc.close();
    }
}