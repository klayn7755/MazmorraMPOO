/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package excepciones;

/** Se lanza cuando un ataque o habilidad se dirige a un objetivo más lejos que el alcance. */
public class FueraDeAlcanceException extends Exception {

    public FueraDeAlcanceException(String mensaje) {
        super(mensaje);
    }
}