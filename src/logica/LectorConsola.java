/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package logica;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Lee datos de consola sin que el programa se caiga por entradas inválidas. */
public class LectorConsola {

    private final Scanner scanner = new Scanner(System.in);

    /** Pide un número entero entre minimo y maximo; repite hasta que sea válido. */
    public int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                if (scanner.hasNextLine()) {
                    scanner.nextLine();   // consume el Enter que quedó pendiente
                }
                if (valor < minimo || valor > maximo) {
                    throw new IllegalArgumentException("Elige un número entre " + minimo + " y " + maximo + ".");
                }
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("[!] Eso no es un número. Intenta de nuevo.");
                scanner.nextLine();       // limpia el buffer para no ciclarse
            } catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
            } catch (NoSuchElementException e) {
                terminar();
            }
        }
    }

    /** Pide un texto que no esté vacío. */
    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                String texto = scanner.nextLine().trim();
                if (texto.isEmpty()) {
                    throw new IllegalArgumentException("No puede estar vacío.");
                }
                return texto;
            } catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
            } catch (NoSuchElementException e) {
                terminar();
            }
        }
    }

    private void terminar() {
        System.out.println("\nSe cerró la entrada. Saliendo del juego.");
        System.exit(0);
    }
}
