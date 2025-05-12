package eft_sofia_medina;

/**
 *
 * @author sofia
 */

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.LinkedList;
public class EFT_Sofia_Medina {
    
    // Variables estáticas y finales, ya que no cambian su valor.
    static final String NOMBRE_TEATRO = "Teatro Moro";
    static final int TOTAL_ASIENTOS = 50;
    static final int PRECIO_VIP = 30000;
    static final int PRECIO_PALCO = 20000;
    static final int PRECIO_PLATEA_B = 18000;
    static final int PRECIO_PLATEA_A = 15000;
    static final int PRECIO_GALERIA = 13000;
    
    // Variables estáticas, porque todos los métodos pueden acceder.
    static Asiento[] asientos = new Asiento[TOTAL_ASIENTOS];
    static String[] rutsClientes = new String[TOTAL_ASIENTOS];
    static int[] descuentosAplicados = new int[TOTAL_ASIENTOS];
    
    // Incluir LinkedList para reservas y descuentos
    static LinkedList<Integer> asientosReservados = new LinkedList<>();
    static LinkedList<String> rutsReservas = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);
    
    // Comenzamos la ejecución
    public static void main(String[] args) {
        inicializarAsientos();
        System.out.println("\nBienvenid@ al sistema de entradas del " + NOMBRE_TEATRO + "."); 
        System.out.println("\nUsted será dirigido al menú principal."); 
        mostrarMenu();
    }
    
    // Separación de asientos
    static void inicializarAsientos() {
        for (int i = 0; i < TOTAL_ASIENTOS; i++) {
            String tipo;
            if (i < 10) tipo = "VIP";
            else if (i < 20) tipo = "Palco";
            else if (i < 30) tipo = "Platea Baja";
            else if (i < 40) tipo = "Platea Alta";
            else tipo = "Galería";
            asientos[i] = new Asiento(tipo);
        }
    }
    
    // Menú principal que se repite hasta que se seleccione opción 6. salir
    static void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL DEL " + NOMBRE_TEATRO.toUpperCase() + " ===");
            System.out.println("1. Reservar Entradas");
            System.out.println("2. Comprar Entradas");
            System.out.println("3. Modificar Venta");
            System.out.println("4. Eliminar Venta");
            System.out.println("5. Imprimir Boleta");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = validarEntradaNumerica();

            switch (opcion) {
                case 1 -> reservarEntradas();
                case 2 -> comprarEntradas();
                case 3 -> modificarVenta();
                case 4 -> eliminarVenta();
                case 5 -> imprimirBoleta();
                case 6 -> System.out.println("Gracias por usar el sistema de entradas del " + NOMBRE_TEATRO + ".");
                default -> System.out.println("Por favor ingrese un número del 1 al 6.");
            }
        } while (opcion != 6);
    }
    
    // Mapa de asientos, separados por zona, 10 asientos por cada uno
    static void mostrarMapaAsientos() {
        System.out.println("\n=== MAPA DE ASIENTOS DEL " + NOMBRE_TEATRO.toUpperCase() + " ===");
        System.out.println("\nZona VIP");
        for (int i = 0; i < 10; i++) System.out.print(formatoAsiento(i));
        System.out.println();

        System.out.println("\nZona Palco");
        for (int i = 10; i < 20; i++) System.out.print(formatoAsiento(i));
        System.out.println();
            
        System.out.println("\nZona Platea Baja");
        for (int i = 20; i < 30; i++) System.out.print(formatoAsiento(i));
        System.out.println();
        
        System.out.println("\nZona Platea Alta");
        for (int i = 30; i < 40; i++) System.out.print(formatoAsiento(i));
        System.out.println();
        
        System.out.println("\nZona Galería");
        for (int i = 40; i < 50; i++) System.out.print(formatoAsiento(i));
        System.out.println();
    }
        
    
    // Validacion de entrada como formato de rut valido
    static String rutValido() {
        String rut;
        do { 
            System.out.print("Ingrese su RUT sin puntos ni guión: ");
            rut = scanner.next();
        } while (!rut.matches("\\d{7,9}"));
        return rut;
    }
    
    // Reserva de entradas unidas al RUT del cliente usando LinkedList
    static void reservarEntradas() {
        String rut = rutValido();
        mostrarMapaAsientos();

        System.out.print("¿Cuántos asientos desea reservar?: ");
        int cantidad = validarEntradaNumerica(); 
        
        for (int i = 0; i < cantidad; i++) {
            System.out.print("Ingrese el número de asiento que desea reservar: ");
            int asiento = validarAsientoEnRango();

            if (asientos[asiento-1].estado.equals("Disponible")) {
                // PUNTO DEBUG: CONFIRMAR QUE SE RESERVE UN ASIENTO DISPONIBLE
                asientos[asiento-1].estado = "Reservado";
                
                // Guardar información
                asientosReservados.add(asiento); 
                rutsReservas.add(rut);
                System.out.println("Asiento " + asiento + " reservado exitosamente.");
            } else {
                System.out.println("Asiento no disponible.");
            }
        }
        System.out.println("Utilice la opción 2 para comprar su entrada reservada.");
    }
    
    // Compra de entradas unidas al RUT del cliente, permite comprar las reservadas si es el mismo cliente
    static void comprarEntradas() {
        String rut = rutValido();
        mostrarMapaAsientos();

        System.out.print("¿Cuántos asientos desea comprar?: ");
        int cantidad = validarEntradaNumerica();
        
        int[] seleccionados = new int[cantidad];
        boolean todosDisponibles = true;
        
        for (int i = 0; i < cantidad; i++) {
            System.out.print("Seleccione número de asiento: ");
            int asiento = validarAsientoEnRango();
            
            Asiento a = asientos[asiento - 1];
            
            if (a.estado.equals("Disponible")) {
                // todo ok, procede
            } else if (a.estado.equals("Reservado")) {
                boolean reservadoPorCliente = false;
                for (int j = 0; j < asientosReservados.size(); j++) {
                    if (asientosReservados.get(j) == asiento && rutsReservas.get(j).equals(rut)) {
                        reservadoPorCliente = true;
                        break;
                    }
                }

                if (!reservadoPorCliente) {
                    System.out.println("Lo sentimos, el asiento " + asiento + " está reservado por otro cliente. La compra ha sido cancelada.");
                    todosDisponibles = false;
                    break;
                }
            } else {
                System.out.println("Lo sentimos, el asiento " + asiento + " ya está vendido. La compra ha sido cancelada.");
                todosDisponibles = false;
                break;
            }
            seleccionados[i]= asiento;
        }
        
        if (!todosDisponibles) return;

        int total = 0;
        
        for (int asiento : seleccionados) {
            System.out.println("A continuación ingrese la información correspondiente para el asiento " + asiento + ".");
            int edad = validarEdad();
            String genero = validarGenero();
            boolean esEstudiante = validarEstudiante();
            
            int precioBase = precioAsiento(asiento-1);
            int desc = aplicarDescuento(edad, genero, esEstudiante);
            // PUNTO DEBUG: CONFIRMAR QUE ESTE TOMANDO LOS DATOS INGRESADOS (EDAD, GENERO, ESTUDIANTE) Y APLICAR EL DESCUENTO QUE LE CORRESPONDA
            
            int precioFinal = precioBase - (precioBase * desc / 100);
            
            // Guardar resultados
            asientos[asiento-1].estado = "Vendido";
            rutsClientes[asiento-1] = rut;
            descuentosAplicados[asiento-1] = desc;
            // PUNTO DEBUG: CONFIRMAR QUE ESTE GUARDANDO EL DESCUENTO CORRESPONDIENTE AL ASIENTO COMPRADO
            
            // Remover reserva si es que existía
            for (int j = 0; j < asientosReservados.size(); j++) {
                if (asientosReservados.get(j) == asiento && rutsReservas.get(j).equals(rut)) {
                    asientosReservados.remove(j);
                    rutsReservas.remove(j);
                    break;
                }
            }
            total += precioFinal;
        }
        System.out.println("¡Gracias por su compra! Utilice la opción 5 para imprimir su boleta.");
        
    }
    
    // Permite cambiar de asiento una vez realizada la venta, validando que corresponda al cliente con su RUT
    static void modificarVenta() {
        String rut = rutValido();
        mostrarMapaAsientos();
        
        System.out.println("Ingrese el número de asiento a modificar: ");
        int asientoMod = validarAsientoEnRango();
        if (rutsClientes[asientoMod-1] != null && rutsClientes[asientoMod-1].equals(rut)) {
            // PUNTO DEBUG: CONFIRMAR QUE SE MODIFICA UNA VENTA DEL RUT INGRESADO
            System.out.print("Ingrese el número del nuevo asiento: ");
            int nuevo = validarAsientoEnRango();
            
            if (asientos[nuevo-1].estado.equals("Disponible")) {
                asientos[asientoMod - 1].estado = "Disponible";
                asientos[nuevo - 1].estado = "Vendido";
                
                rutsClientes[nuevo-1] = rut;
                rutsClientes[asientoMod-1]=null;
                
                descuentosAplicados[nuevo - 1] = descuentosAplicados[asientoMod - 1];
                descuentosAplicados[asientoMod - 1] = 0; 
                
                System.out.println("La modificación ha sido realizada con éxito.");
            } else {
                System.out.println("El asiento " + nuevo + " ingresado no está disponible.");
            }    
        } else {
            System.out.println("Usted no tiene una compra registrada para el asiento " + asientoMod + ".");
        }    
    }        

    // Eliminar venta, se valida cliente con RUT
    static void eliminarVenta() {
        String rut = rutValido();
        mostrarMapaAsientos();
        System.out.println("Ingrese el número de asiento a eliminar: ");
        int asiento = validarAsientoEnRango();
        if (rutsClientes[asiento-1] != null && rutsClientes[asiento-1].equals(rut)) {
            // PUNTO DEBUG: CONFIRMAR QUE SE ELIMINA UNA VENTA DEL RUT INGRESADO
            asientos[asiento - 1].estado = "Disponible";
            rutsClientes[asiento-1] = null;
            System.out.println("La venta del asiento " + asiento+ " ha sido eliminada.");
        } else {
            System.out.println("No se encontró una compra asociada al RUT " + rut + " para el asiento " + asiento + ".");
        }
    }
    
    
    // Boleta con nombre teatro, nombre cliente, asientos comprados con sus precios iniciales, desc y finales, total pagado
    static void imprimirBoleta() {
        String rut = rutValido();
       
        int total = 0;
        
        // Para que no imprima boletas si no ha comprado aún
        boolean hayCompras = false;
        
        for (int i = 0; i < TOTAL_ASIENTOS; i++) {
            if (rut.equals(rutsClientes[i])) {
                hayCompras = true;
                break;
            }
        }
        
        if (!hayCompras) {
            System.out.println("No hay compradas asociadas al RUT " + rut + ".");
            return;
        }
        
        // Si hay compras, imprime desde aquí
        System.out.println("\n============= Boleta =============");
        System.out.println("\nTeatro: " + NOMBRE_TEATRO);
        System.out.println("\nID: " + rut);
        System.out.println("");

        for (int i = 0; i < TOTAL_ASIENTOS; i++) {
            if (rut.equals(rutsClientes[i])) {
                int precioBase = precioAsiento(i);
                int desc = descuentosAplicados[i];
                // PUNTO DEBUG: CONFIRMAR QUE SE ESTA LEYENDO EL DESCUENTO CORRECTO PARA CADA ASIENTO
                String nombre = nombreDescuento(desc);
                int precioFinal = precioBase - (precioBase * desc / 100);

                System.out.println("- Asiento " + (i+1));
                System.out.println("Precio Base: $" + precioBase);
                System.out.println("Descuento aplicado: " + nombre + " (" + desc + "%)");
                System.out.println("Precio Final del Asiento: $" + precioFinal);
                System.out.println("");
                total += precioFinal;
            }
        }
        
        System.out.println();
        System.out.println("\nTotal pagado: $" + total);
        System.out.println("==================================\n");
    }
     
    
    // Descuentos 
    static int aplicarDescuento(int edad, String genero, boolean esEstudiante) {
        int desc = 0;
        if (edad < 12) desc = 10;
        if (genero.equalsIgnoreCase("F")) desc = Math.max(desc,20);
        if (esEstudiante) desc = Math.max(desc, 15);
        if (edad >= 60) desc = Math.max(desc, 25);
        return desc;        
    }
    
    // Descuento para ser mostrado en la boleta
    static String nombreDescuento(int porcentaje) {
        return switch (porcentaje) {
            case 10 -> "Niño";
            case 15 -> "Estudiante";
            case 20 -> "Mujer";
            case 25 -> "Tercera Edad";
            default -> "Sin Descuento";   
        };
    }

    
    // True o false de que se use un número y no una letra cuando se le pregunta al cliente
    static int validarEntradaNumerica() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingrese un número.");
                scanner.next();
            }
        }
    }
    
    // Validar asientos en rango de 1 al total asientos
    static int validarAsientoEnRango() {
        int numero;
        do {
            numero = validarEntradaNumerica();
            if (numero < 1 || numero > TOTAL_ASIENTOS) {
            System.out.println("Por favor ingrese un número del 1 al " + TOTAL_ASIENTOS +".");
            }
        } while (numero < 1 || numero > TOTAL_ASIENTOS);
        return numero;
    }
    
    // Validar edad valida de 1 a 100 segun lo acordado
    static int validarEdad() {
        int edad;
        do {
            System.out.println("Ingrese su edad: ");
            edad = validarEntradaNumerica();
            if (edad < 1 || edad > 100) {
                System.out.println("Por favor ingrese una edad válida.");
            }
        } while (edad < 1 || edad > 100);
        return edad;
                
    }
    
    // Validar genero para que solo ingrese M o F
    static String validarGenero() {
        String genero;
        do {
            System.out.println("Ingrese su género (M/F): ");
            genero = scanner.next().trim().toUpperCase();
            
            if (!genero.equals("M") && !genero.equals("F")) {
                System.out.println("Por favor ingrese solo 'M' o 'F'.");
            }
        } while (!genero.equals("M") && !genero.equals("F"));
        return genero;
    }
    
    // Validar estudiante para que solo ingrese S o N
    static boolean validarEstudiante() {
        String est;
        do {
            System.out.println("¿Es usted estudiante? (S/N): ");
            est = scanner.next().trim().toUpperCase();
            
            if (!est.equals("S") && !est.equals("N")) {
                System.out.println("Por favor ingrese solo 'S' para responder si o 'N' para responder no.");
            }
        } while (!est.equals("S") && !est.equals("N"));
        return est.equals("S");
    }
    
    // Cambio para el estado de los asientos en el mapa que se muestra al cliente
    static String formatoAsiento(int i) {
        if (asientos[i].estado.equals("Disponible")) return String.format("[%2d] ", i +1);
        else if (asientos[i].estado.equals("Reservado")) return "[R ] ";
        else return "[X ] ";
    }
    
    // Obtener precios iniciales para cada tipo de entrada
    static int precioAsiento(int index) {
        String tipo = asientos[index].tipo;
        if (tipo.equals("VIP")) {
            return PRECIO_VIP;
        } else if (tipo.equals("Palco")) {
            return PRECIO_PALCO;    
        } else if (tipo.equals("Platea Baja")) {
            return PRECIO_PLATEA_B;
        } else if (tipo.equals("Platea Alta")) {
            return PRECIO_PLATEA_A;
        } else
            return PRECIO_GALERIA;
        }
    }
    
    // Cosntructor de Asiento
    class Asiento {
        String tipo;
        String estado;

        Asiento(String tipo) {
            this.tipo = tipo;
            this.estado = "Disponible";
        }
    }




