/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package excepciones;

/** Se lanza cuando una entidad intenta moverse o colocarse en una casilla no válida. */
public class MovimientoInvalidoException extends Exception {

    public MovimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}