/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Enemigo débil pero veloz: avanza dos casillas por turno. */
public class Goblin extends Enemigo {

    public Goblin() {
        super("Goblin", 12, 3, 0, 10);
    }

    @Override
    public char getSimbolo() { return 'g'; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(4);
    }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int getVelocidad() { return 2; }
}