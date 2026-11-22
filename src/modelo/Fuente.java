/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import interfaces.Interactuable;

/** Fuente mágica con usos limitados que restaura vida y energía. */
public class Fuente implements Interactuable {

    private int usosRestantes;
    private int curacion;
    private int energia;

    public Fuente(int usosRestantes, int curacion, int energia) {
        setUsosRestantes(usosRestantes);
        setCuracion(curacion);
        setEnergia(energia);
    }

    @Override
    public String interactuar(Heroe heroe) {
        if (usosRestantes <= 0) {
            return "La fuente se ha secado.";
        }
        heroe.curar(curacion);
        heroe.recuperarEnergia(energia);
        usosRestantes--;
        return "Bebes de la fuente mágica: +" + curacion + " de vida y +" + energia
                + " de energía (usos restantes: " + usosRestantes + ").";
    }

    @Override
    public boolean estaDisponible() { return usosRestantes > 0; }

    @Override
    public char getSimbolo() { return usosRestantes > 0 ? 'F' : 'f'; }

    public int getUsosRestantes() { return usosRestantes; }

    public void setUsosRestantes(int usosRestantes) {
        if (usosRestantes < 0) {
            throw new IllegalArgumentException("Los usos no pueden ser negativos.");
        }
        this.usosRestantes = usosRestantes;
    }

    public int getCuracion() { return curacion; }

    public void setCuracion(int curacion) {
        if (curacion <= 0) {
            throw new IllegalArgumentException("La curación debe ser positiva.");
        }
        this.curacion = curacion;
    }

    public int getEnergia() { return energia; }

    public void setEnergia(int energia) {
        if (energia <= 0) {
            throw new IllegalArgumentException("La energía debe ser positiva.");
        }
        this.energia = energia;
    }
}