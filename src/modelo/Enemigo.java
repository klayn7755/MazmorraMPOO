/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.FueraDeAlcanceException;

/** Clase abstracta de los monstruos. Define una IA básica: detectar, acercarse y atacar. */
public abstract class Enemigo extends Entidad {

    protected Enemigo(String nombre, int vidaMaxima, int ataqueBase, int defensa, int puntosOtorgados) {
        super(nombre, vidaMaxima, ataqueBase, defensa);
        setPuntosOtorgados(puntosOtorgados);
    }

    /** Casillas que puede avanzar por turno. */
    public int getVelocidad() {
        return 1;
    }

    /** Distancia a partir de la cual el enemigo nota al héroe. */
    public int getRadioDeteccion() {
        return 6;
    }

    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        if (distanciaA(objetivo) > getRadioDeteccion()) {
            return getNombre() + " permanece al acecho.";
        }
        if (distanciaA(objetivo) <= getAlcance()) {
            try {
                return atacar(objetivo);
            } catch (FueraDeAlcanceException e) {
                return getNombre() + " falla su ataque.";
            }
        }
        int pasos = 0;
        while (pasos < getVelocidad() && distanciaA(objetivo) > getAlcance()
                && tablero.acercarEntidad(this, objetivo)) {
            pasos++;
        }
        return pasos > 0 ? getNombre() + " avanza hacia ti." : getNombre() + " no encuentra camino.";
    }
}