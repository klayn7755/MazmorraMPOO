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

import interfaces.Curable;

//Bestia resistente que regenera vida al empezar cada turno
public class Troll extends Enemigo implements Curable {

    public Troll() {
        super("Troll", 30, 6, 2, 40);
    }

    @Override
    public char getSimbolo() { return 'T'; }

    @Override
    public int calcularDanio() {
        return getAtaque() + tirarDado(8);
    }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public void curar(int cantidad) {
        setVida(getVida() + cantidad);
    }

    @Override
    public String alIniciarTurno() {
        if (getVida() < getVidaMaxima()) {
            curar(2);
            return getNombre() + " regenera 2 puntos de vida.";
        }
        return "";
    }
}
