/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Arquero no muerto: ataca desde 2 casillas de distancia. */
public class Esqueleto extends Enemigo {

    public Esqueleto() {
        super("Esqueleto Arquero", 18, 4, 1, 20);
    }

    @Override
    public char getSimbolo() { return 'e'; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(6);
    }

    @Override
    public int getAlcance() { return 2; }
}