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

/** Lectura segura desde consola: valida tipo y rango, y limpia el buffer ante entradas inválidas. */
public class LectorConsola {

    private final Scanner scanner = new Scanner(System.in);

    /** Pide un entero entre minimo y maximo (inclusive) y repite hasta recibir uno válido. */
    public int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Consume el salto de línea pendiente.
                }
                if (valor < minimo || valor > maximo) {
                    throw new IllegalArgumentException("Elige un numero entre " + minimo + " y " + maximo + ".");
                }
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida: escribe un numero entero.");
                scanner.nextLine(); // Descarta el texto incorrecto para no ciclarse.
            } catch (IllegalArgumentException e) {
                System.out.println("Alerta" + e.getMessage());
            } catch (NoSuchElementException e) {
                terminarPorFinDeEntrada();
            }
        }
    }

    /** Pide un texto no vacío. */
    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                String texto = scanner.nextLine().trim();
                if (texto.isEmpty()) {
                    throw new IllegalArgumentException("El texto no puede estar vacio.");
                }
                return texto;
            } catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
            } catch (NoSuchElementException e) {
                terminarPorFinDeEntrada();
            }
        }
    }

    private void terminarPorFinDeEntrada() {
        System.out.println("\nEntrada finalizada. Saliendo del juego.");
        System.exit(0);
    }
}