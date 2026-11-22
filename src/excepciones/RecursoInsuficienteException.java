/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package excepciones;

/** Se lanza cuando falta energía, espacio en el inventario u objetos para una acción. */
public class RecursoInsuficienteException extends Exception {

    public RecursoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}