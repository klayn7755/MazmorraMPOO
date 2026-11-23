/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Clase abstracta de los monstruos: detectan al heroe, se acercan y atacan. */
public abstract class Enemigo extends Entidad {

    private static final int RADIO_DETECCION = 6;

    public Enemigo(String nombre, int vidaMaxima, int ataque, int defensa, int recompensa) {
        super(nombre, vidaMaxima, ataque, defensa);
        setRecompensa(recompensa);
    }

    /** Casillas que avanza por turno. */
    public int getVelocidad() {
        return 1;
    }

    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        if (distanciaA(objetivo) > RADIO_DETECCION) {
            return getNombre() + " vigila la zona.";
        }
        if (distanciaA(objetivo) <= getAlcance()) {
            return atacar(objetivo);
        }
        int pasos = 0;
        while (pasos < getVelocidad() && distanciaA(objetivo) > getAlcance()) {
            if (!tablero.acercar(this, objetivo)) {
                break;
            }
            pasos++;
        }
        return pasos > 0 ? getNombre() + " se acerca..." : getNombre() + " no encuentra el camino.";
    }
}
