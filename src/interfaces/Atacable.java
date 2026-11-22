/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package interfaces;

/** Contrato para todo ser que puede recibir daño en combate. */
public interface Atacable {

    void recibirDanio(int danio);

    boolean estaVivo();

    String getNombre();

    int getUltimoDanioRecibido();
}