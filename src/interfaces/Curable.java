/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package interfaces;

/** Contrato para quien puede recuperar puntos de vida. */
public interface Curable {

    void curar(int cantidad);
}