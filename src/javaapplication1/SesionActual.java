package javaapplication1;

public class SesionActual {
    private static String rol = "";

    public static void setRol(String r) { rol = r; }
    public static String getRol()       { return rol; }

    public static boolean esAdmin()     { return rol.equals("Admin"); }
    public static boolean esEmpleado()  { return rol.equals("Empleado"); }
}