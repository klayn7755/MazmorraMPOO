/*
- Proyecto Final - Modelos de Programación Orientada a Objetos
- Facultad de Ingeniería, UNAM
- Integrantes: Kev
- Grupo: 5
- Semestre: 2027-1
 */
package logica;

import excepciones.RecursoInsuficienteException;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.Entidad;
import modelo.Heroe;
import modelo.Tablero;

/** Gestiona los ataques del héroe, el turno de los enemigos y la derrota de monstruos. */
public class GestorCombate {

    private final LectorConsola lector;
    private final ConsolaUI ui;
    private boolean jefeDerrotado;

    public GestorCombate(LectorConsola lector, ConsolaUI ui) {
        this.lector = lector;
        this.ui = ui;
        this.jefeDerrotado = false;
    }

    public boolean isJefeDerrotado() {
        return jefeDerrotado;
    }

    public void reiniciar() {
        this.jefeDerrotado = false;
    }

    public boolean atacar(Heroe heroe, ArrayList<Entidad> enemigos, Tablero tablero, boolean conHabilidad) {
        Entidad objetivo = elegirObjetivo(heroe, enemigos);
        if (objetivo == null) {
            return false;
        }
        int alcance = conHabilidad ? heroe.getAlcanceHabilidad() : heroe.getAlcance();
        if (heroe.distanciaA(objetivo) > alcance) {
            System.out.println("[advertencia] " + objetivo.getNombre() + " esta a " + heroe.distanciaA(objetivo)
                    + " casillas y tu alcance es " + alcance + ".");
            return false;
        }
        try {
            System.out.println(conHabilidad ? heroe.usarHabilidad(objetivo) : heroe.atacar(objetivo));
        } catch (RecursoInsuficienteException e) {
            System.out.println("[advertencia] " + e.getMessage());
            return false;
        }
        retirarMuertos(heroe, enemigos, tablero);
        return true;
    }

    private Entidad elegirObjetivo(Heroe heroe, ArrayList<Entidad> enemigos) {
        if (enemigos.isEmpty()) {
            System.out.println("No quedan enemigos.");
            return null;
        }
        System.out.println("Objetivos:");
        for (int i = 0; i < enemigos.size(); i++) {
            Entidad e = enemigos.get(i);
            System.out.println("  " + (i + 1) + ") " + e.getNombre() + " " + ui.barra(e.getVida(), e.getVidaMaxima())
                    + " en (" + e.getFila() + "," + e.getColumna() + ") a " + heroe.distanciaA(e) + " casillas");
        }
        int opcion = lector.leerEntero("Objetivo (0=cancelar): ", 0, enemigos.size());
        return opcion == 0 ? null : enemigos.get(opcion - 1);
    }

    private void retirarMuertos(Heroe heroe, ArrayList<Entidad> enemigos, Tablero tablero) {
        Iterator<Entidad> iterador = enemigos.iterator();
        while (iterador.hasNext()) {
            Entidad enemigo = iterador.next();
            if (!enemigo.estaVivo()) {
                tablero.retirar(enemigo);
                heroe.registrarVictoria(enemigo.getRecompensa());
                System.out.println("Derrotaste a " + enemigo.getNombre() + " +" + enemigo.getRecompensa() + " puntos.");
                if (enemigo.esJefe()) {
                    jefeDerrotado = true;
                }
                iterador.remove();
            }
        }
    }

    /** Cada enemigo actúa según su propia clase (polimorfismo). */
    public void faseEnemigos(Heroe heroe, ArrayList<Entidad> enemigos, Tablero tablero) {
        System.out.println("\n--- Turno de los enemigos ---");
        heroe.alIniciarTurno();
        for (Entidad enemigo : enemigos) {
            String efecto = enemigo.alIniciarTurno();
            if (!efecto.isEmpty()) {
                System.out.println(efecto);
            }
            System.out.println(enemigo.actuar(tablero, heroe));
            if (!heroe.estaVivo()) {
                System.out.println("¡" + heroe.getNombre() + " ha caido en combate!");
                break;
            }
        }
    }
}