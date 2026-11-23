/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: González Gaspar Sergio Alexander
 *              Macario Quintero Kevin Alexis 
 *              Trujillo Sedano Eduardo
 * Grupo: 5
 * Semestre: 2027-1
 */
package excepciones;

// Se lanza cuando alguien intenta moverse a una casilla que no se puede pisar.
public class MovimientoInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public MovimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
