/*
- Proyecto Final - Modelos de Programación Orientada a Objetos
- Facultad de Ingeniería, UNAM
- Integrantes: Kev
- Grupo: 5
- Semestre: 2027-1
 */
package logica;

import excepciones.MovimientoInvalidoException;
import excepciones.RecursoInsuficienteException;
import java.util.ArrayList;
import java.util.Random;
import modelo.*;

/** Orquestador principal: coordina el ciclo de partida, menús y turnos entre los gestores. */
public class Juego {

    private static final int LIMITE_TURNOS = 60;

    /** Mapa: '#' muro, '.' suelo, '+' poción, '$' tesoro. */
    private static final String[] MAPA = {
        "##############",
        "#............#",
        "#.##...+..##.#",
        "#.#..........#",
        "#....##..#.$.#",
        "#....##..#...#",
        "#.#..$.......#",
        "#.##...##.##.#",
        "#..+.........#",
        "##############"
    };

    private static final String[] AMBIENTE = {
        "Una gota cae del techo y el eco recorre la mazmorra...",
        "Escuchas un rugido lejano. Algo grande te espera al fondo.",
        "Las antorchas parpadean. No estás solo aquí.",
        "Huele a azufre y a ceniza vieja.",
        "Un viento helado apaga una vela cercana."
    };

    private final LectorConsola lector = new LectorConsola();
    private final ConsolaUI ui = new ConsolaUI();
    private final GestorExploracion exploracion = new GestorExploracion(lector, ui);
    private final GestorCombate combate = new GestorCombate(lector, ui);
    private final Random azar = new Random();

    private Tablero tablero;
    private Heroe heroe;
    private ArrayList<Entidad> enemigos = new ArrayList<>();
    private int turno;
    private boolean abandono;

    public int getTurno() { return turno; }

    public Heroe getHeroe() { return heroe; }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            ui.mostrarBanner();
            System.out.println("  1) Nueva partida");
            System.out.println("  2) Instrucciones");
            System.out.println("  3) Salir");
            int opcion = lector.leerEntero("Elige una opción: ", 1, 3);
            if (opcion == 1) {
                jugarPartida();
            } else if (opcion == 2) {
                ui.mostrarInstrucciones(LIMITE_TURNOS);
            } else {
                salir = true;
            }
        }
        System.out.println("\n¡Hasta la próxima aventura!");
    }

    private void jugarPartida() {
        if (!prepararPartida()) {
            return;
        }
        while (!partidaTerminada()) {
            ejecutarTurno();
        }
        ui.mostrarResumenFinal(heroe, combate.isJefeDerrotado(), abandono, turno, LIMITE_TURNOS);
    }

    private boolean partidaTerminada() {
        return combate.isJefeDerrotado() || abandono || !heroe.estaVivo() || turno >= LIMITE_TURNOS;
    }

    private boolean prepararPartida() {
        System.out.println("\n=== CREA A TU HÉROE ===");
        String nombre = lector.leerTexto("Nombre: ");
        System.out.println("  1) Guerrero - vida 40, defensa alta. Habilidad: Golpe Brutal (daño x2)");
        System.out.println("  2) Mago     - vida 26, ataca de lejos. Habilidad: Bola de Fuego (ignora defensa)");
        System.out.println("  3) Pícaro   - vida 30, mucho daño. Habilidad: Puñalada Trapera (daño x3)");
        int clase = lector.leerEntero("Clase: ", 1, 3);
        if (clase == 1) {
            heroe = new Guerrero(nombre);
        } else if (clase == 2) {
            heroe = new Mago(nombre);
        } else {
            heroe = new Picaro(nombre);
        }

        enemigos = new ArrayList<>();
        combate.reiniciar();
        try {
            heroe.agregarObjeto(new Objeto("Poción de vida", 15, 0));
            heroe.agregarObjeto(new Objeto("Poción de vida", 15, 0));
            heroe.agregarObjeto(new Objeto("Poción de energía", 0, 6));

            tablero = new Tablero(MAPA);
            tablero.colocar(heroe, 1, 1);
            colocarEnemigo(new Goblin(), 3, 9);
            colocarEnemigo(new Goblin(), 1, 11);
            colocarEnemigo(new Troll(), 8, 6);
            colocarEnemigo(new Dragon(), 8, 11);
        } catch (RecursoInsuficienteException | MovimientoInvalidoException e) {
            System.out.println("Error al preparar la partida: " + e.getMessage());
            return false;
        }

        turno = 0;
        abandono = false;
        System.out.println("\n¡Que los dados te sean favorables, " + heroe.getNombre()
                + " el " + heroe.getClase() + "!");
        return true;
    }

    private void colocarEnemigo(Entidad enemigo, int fila, int columna) throws MovimientoInvalidoException {
        tablero.colocar(enemigo, fila, columna);
        enemigos.add(enemigo);
    }

    private void ejecutarTurno() {
        turno++;
        System.out.println("\n=================== TURNO " + turno + " / " + LIMITE_TURNOS + " ===================");
        if (turno % 4 == 0) {
            System.out.println("* " + AMBIENTE[azar.nextInt(AMBIENTE.length)]);
        }
        tablero.imprimir();
        ui.mostrarLeyenda();
        ui.mostrarEstadoHeroe(heroe, enemigos.size());

        boolean turnoUsado = false;
        while (!turnoUsado && !abandono && !combate.isJefeDerrotado()) {
            turnoUsado = procesarAccion(leerAccion());
        }
        if (!abandono && !combate.isJefeDerrotado()) {
            combate.faseEnemigos(heroe, enemigos, tablero);
        }
    }

    private int leerAccion() {
        System.out.println("\n  1) Moverse");
        System.out.println("  2) Atacar");
        System.out.println("  3) Habilidad: " + heroe.getNombreHabilidad() + " (cuesta "
                + heroe.getCostoHabilidad() + " de energía)");
        System.out.println("  4) Usar objeto");
        System.out.println("  5) Descansar");
        System.out.println("  6) Ver enemigos e inventario (no gasta turno)");
        System.out.println("  0) Abandonar");
        return lector.leerEntero("Acción: ", 0, 6);
    }

    private boolean procesarAccion(int opcion) {
        switch (opcion) {
            case 1:
                return exploracion.moverHeroe(heroe, tablero);
            case 2:
                return combate.atacar(heroe, enemigos, tablero, false);
            case 3:
                return combate.atacar(heroe, enemigos, tablero, true);
            case 4:
                return exploracion.usarObjeto(heroe);
            case 5:
                System.out.println(heroe.actuar(tablero, null));
                return true;
            case 6:
                ui.mostrarEnemigosEInventario(heroe, enemigos);
                return false;
            default:
                return confirmarAbandono();
        }
    }

    private boolean confirmarAbandono() {
        if (lector.leerEntero("¿Seguro que quieres abandonar? (1=Sí, 0=No): ", 0, 1) == 1) {
            abandono = true;
            return true;
        }
        return false;
    }
}