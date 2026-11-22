/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.RecursoInsuficienteException;
import interfaces.Interactuable;

/** Cofre del tesoro: entrega un objeto y puntos bonus una sola vez. */
public class Cofre implements Interactuable {

    private Objeto contenido;
    private int puntosBonus;
    private boolean abierto;

    public Cofre(Objeto contenido, int puntosBonus) {
        setContenido(contenido);
        setPuntosBonus(puntosBonus);
    }

    @Override
    public String interactuar(Heroe heroe) throws RecursoInsuficienteException {
        if (abierto) {
            return "El cofre ya está vacío.";
        }
        heroe.agregarObjeto(contenido); // Si el inventario está lleno, lanza la excepción y el cofre sigue cerrado.
        heroe.sumarPuntos(puntosBonus);
        abierto = true;
        return "Abres el cofre y obtienes: " + contenido + " (+" + puntosBonus + " puntos).";
    }

    @Override
    public boolean estaDisponible() { return !abierto; }

    @Override
    public char getSimbolo() { return abierto ? 'c' : 'C'; }

    public Objeto getContenido() { return contenido; }

    public void setContenido(Objeto contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException("El cofre necesita un contenido.");
        }
        this.contenido = contenido;
    }

    public int getPuntosBonus() { return puntosBonus; }

    public void setPuntosBonus(int puntosBonus) {
        if (puntosBonus < 0) {
            throw new IllegalArgumentException("Los puntos bonus no pueden ser negativos.");
        }
        this.puntosBonus = puntosBonus;
    }

    public boolean isAbierto() { return abierto; }

    public void setAbierto(boolean abierto) { this.abierto = abierto; }
}