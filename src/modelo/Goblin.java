/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: González Gaspar Sergio Alexander
 *              Macario Quintero Kevin Alexis 
 *              Trujillo Sedano Eduardo
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Enemigo débil pero rápido: avanza 2 casillas por turno. */
public class Goblin extends Enemigo {

    public Goblin() {
        super("Goblin", 12, 3, 0, 10);
    }

    @Override
    public char getSimbolo() { return 'g'; }

    @Override
    public int calcularDanio() {
        return getAtaque() + tirarDado(4);
    }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int getVelocidad() { return 2; }
}
