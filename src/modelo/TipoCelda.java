/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Tipos de terreno que puede tener una celda del tablero. */
public enum TipoCelda {
    SUELO('.'),
    MURO('#');

    private final char simbolo;

    TipoCelda(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public boolean esTransitable() {
        return this == SUELO;
    }
}