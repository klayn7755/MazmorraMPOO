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

// Se lanza cuando falta energía, espacio en el inventario u objetos.
public class RecursoInsuficienteException extends Exception {

    private static final long serialVersionUID = 1L;

    public RecursoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
