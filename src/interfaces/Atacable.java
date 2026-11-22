/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
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
