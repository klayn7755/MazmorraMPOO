/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: González Gaspar Sergio Alexander
 *              Macario Quintero Kevin Alexis 
 *              Trujillo Sedano Eduardo
 * Grupo: 5
 * Semestre: 2027-1
 */
package interfaces;

/** Todo lo que puede recibir daño en combate. */
public interface Atacable {

    /** Aplica el daño y devuelve cuánto daño real se causó. */
    int recibirDanio(int danio);

    boolean estaVivo();
}
