/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import interfaces.Curable;

/** Bestia resistente que regenera vida al inicio de cada turno. */
public class Troll extends Enemigo implements Curable {

    private static final int REGENERACION = 2;

    public Troll() {
        super("Troll", 30, 6, 2, 40);
    }

    @Override
    public char getSimbolo() { return 'T'; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(8);
    }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public void curar(int cantidad) {
        restaurarVida(cantidad);
    }

    @Override
    public String alIniciarTurno() {
        if (getVidaActual() < getVidaMaxima()) {
            curar(REGENERACION);
            return getNombre() + " regenera " + REGENERACION + " puntos de vida.";
        }
        return "";
    }
}