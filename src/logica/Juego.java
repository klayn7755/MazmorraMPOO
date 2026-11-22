/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package logica;

import excepciones.MovimientoInvalidoException;
import excepciones.RecursoInsuficienteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import modelo.*;

/** Controla todo el juego: menús, turnos, acciones del jugador y final de la partida. */
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

    private static final int[] DELTA_FILA = {-1, 1, 0, 0};
    private static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    private static final String[] AMBIENTE = {
        "Una gota cae del techo y el eco recorre la mazmorra...",
        "Escuchas un rugido lejano. Algo grande te espera al fondo.",
        "Las antorchas parpadean. No estás solo aquí.",
        "Huele a azufre y a ceniza vieja.",
        "Un viento helado apaga una vela cercana."
    };

    private final LectorConsola lector = new LectorConsola();
    private final Random azar = new Random();
    private Tablero tablero;
    private Heroe heroe;
    private ArrayList<Entidad> enemigos = new ArrayList<>();
    private int turno;
    private boolean jefeDerrotado;
    private boolean abandono;

    public int getTurno() { return turno; }

    public Heroe getHeroe() { return heroe; }

    // =====================================================================
    //  MENÚ PRINCIPAL
    // =====================================================================

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarBanner();
            System.out.println("  1) Nueva partida");
            System.out.println("  2) Instrucciones");
            System.out.println("  3) Salir");
            int opcion = lector.leerEntero("Elige una opción: ", 1, 3);
            if (opcion == 1) {
                jugarPartida();
            } else if (opcion == 2) {
                mostrarInstrucciones();
            } else {
                salir = true;
            }
        }
        System.out.println("\n¡Hasta la próxima aventura!");
    }

    private void mostrarBanner() {
        System.out.println();
        System.out.println("  ==============================================");
        System.out.println("     M A Z M O R R A S   D E   E L D O R I A");
        System.out.println("        ~ Una aventura de dados y dragones ~");
        System.out.println("  ==============================================");
        System.out.println("        /| ________________");
        System.out.println("  O|===|* >________________>");
        System.out.println("        \\|");
        System.out.println();
    }

    private void mostrarInstrucciones() {
        System.out.println("\n---------------- INSTRUCCIONES ----------------");
        System.out.println("Bajas a una mazmorra llena de monstruos. Tu misión: derrotar");
        System.out.println("al Dragón Ancestral (D) antes de que pasen " + LIMITE_TURNOS + " turnos.");
        System.out.println("Pierdes si tu vida llega a 0, abandonas o se acaban los turnos.");
        System.out.println();
        System.out.println("En cada turno haces UNA acción y después se mueven los monstruos.");
        System.out.println(" - Los ataques usan un dado de 20 caras (d20): un 1 siempre falla");
        System.out.println("   y un 20 es golpe crítico (daño doble).");
        System.out.println(" - La habilidad especial gasta energía. Descansar la recupera.");
        System.out.println(" - Pisa '+' para recoger pociones y '$' para ganar puntos.");
        System.out.println(" - El alcance se mide en casillas rectas (sin diagonales).");
        mostrarLeyenda();
    }

    private void mostrarLeyenda() {
        System.out.println("Leyenda: @ tú | g Goblin | T Troll | D Dragón | + poción | $ tesoro | # muro");
    }

    // =====================================================================
    //  PREPARAR Y JUGAR LA PARTIDA
    // =====================================================================

    private void jugarPartida() {
        if (!prepararPartida()) {
            return;
        }
        while (!partidaTerminada()) {
            ejecutarTurno();
        }
        mostrarResumenFinal();
    }

    private boolean partidaTerminada() {
        return jefeDerrotado || abandono || !heroe.estaVivo() || turno >= LIMITE_TURNOS;
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
        jefeDerrotado = false;
        abandono = false;
        System.out.println("\n¡Que los dados te sean favorables, " + heroe.getNombre()
                + " el " + heroe.getClase() + "!");
        return true;
    }

    private void colocarEnemigo(Entidad enemigo, int fila, int columna) throws MovimientoInvalidoException {
        tablero.colocar(enemigo, fila, columna);
        enemigos.add(enemigo);
    }

    // =====================================================================
    //  TURNOS
    // =====================================================================

    private void ejecutarTurno() {
        turno++;
        System.out.println("\n=================== TURNO " + turno + " / " + LIMITE_TURNOS + " ===================");
        if (turno % 4 == 0) {
            System.out.println("* " + AMBIENTE[azar.nextInt(AMBIENTE.length)]);
        }
        tablero.imprimir();
        mostrarLeyenda();
        mostrarEstadoHeroe();

        boolean turnoUsado = false;
        while (!turnoUsado && !abandono && !jefeDerrotado) {
            turnoUsado = procesarAccion(leerAccion());
        }
        if (!abandono && !jefeDerrotado) {
            faseEnemigos();
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

    /** Ejecuta la acción elegida. Devuelve true si gastó el turno. */
    private boolean procesarAccion(int opcion) {
        switch (opcion) {
            case 1:
                return moverHeroe();
            case 2:
                return atacar(false);
            case 3:
                return atacar(true);
            case 4:
                return usarObjeto();
            case 5:
                System.out.println(heroe.actuar(tablero, null));
                return true;
            case 6:
                mostrarEnemigosEInventario();
                return false;
            default:
                return confirmarAbandono();
        }
    }

    /** Cada enemigo actúa según su propia clase (polimorfismo: no hace falta preguntar su tipo). */
    private void faseEnemigos() {
        System.out.println("\n--- Turno de los enemigos ---");
        heroe.alIniciarTurno();
        for (Entidad enemigo : enemigos) {
            String efecto = enemigo.alIniciarTurno();
            if (!efecto.isEmpty()) {
                System.out.println(efecto);
            }
            System.out.println(enemigo.actuar(tablero, heroe));
            if (!heroe.estaVivo()) {
                System.out.println("¡" + heroe.getNombre() + " ha caído en combate!");
                break;
            }
        }
    }

    // =====================================================================
    //  ACCIONES DEL JUGADOR
    // =====================================================================

    private boolean moverHeroe() {
        int direccion = lector.leerEntero("Dirección (1=Arriba 2=Abajo 3=Izquierda 4=Derecha, 0=Cancelar): ", 0, 4);
        if (direccion == 0) {
            return false;
        }
        try {
            tablero.mover(heroe, heroe.getFila() + DELTA_FILA[direccion - 1],
                    heroe.getColumna() + DELTA_COLUMNA[direccion - 1]);
            recogerDelSuelo();
            return true;
        } catch (MovimientoInvalidoException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    /** Si el héroe pisa una poción o un tesoro, lo recoge. */
    private void recogerDelSuelo() {
        char item = tablero.tomarItem(heroe.getFila(), heroe.getColumna());
        if (item == '$') {
            heroe.sumarPuntos(20);
            System.out.println("¡Encuentras un tesoro! +20 puntos.");
        } else if (item == '+') {
            try {
                heroe.agregarObjeto(new Objeto("Poción de vida", 15, 0));
                System.out.println("¡Recoges una Poción de vida!");
            } catch (RecursoInsuficienteException e) {
                System.out.println("[!] " + e.getMessage() + " La poción se queda en el suelo.");
                tablero.ponerItem(heroe.getFila(), heroe.getColumna(), '+');
            }
        }
    }

    private boolean atacar(boolean conHabilidad) {
        Entidad objetivo = elegirObjetivo();
        if (objetivo == null) {
            return false;
        }
        int alcance = conHabilidad ? heroe.getAlcanceHabilidad() : heroe.getAlcance();
        if (heroe.distanciaA(objetivo) > alcance) {
            System.out.println("[!] " + objetivo.getNombre() + " está a " + heroe.distanciaA(objetivo)
                    + " casillas y tu alcance es " + alcance + ".");
            return false;
        }
        try {
            System.out.println(conHabilidad ? heroe.usarHabilidad(objetivo) : heroe.atacar(objetivo));
        } catch (RecursoInsuficienteException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
        retirarMuertos();
        return true;
    }

    private Entidad elegirObjetivo() {
        if (enemigos.isEmpty()) {
            System.out.println("No quedan enemigos.");
            return null;
        }
        System.out.println("Objetivos:");
        for (int i = 0; i < enemigos.size(); i++) {
            Entidad e = enemigos.get(i);
            System.out.println("  " + (i + 1) + ") " + e.getNombre() + " " + barra(e.getVida(), e.getVidaMaxima())
                    + " en (" + e.getFila() + "," + e.getColumna() + ") a " + heroe.distanciaA(e) + " casillas");
        }
        int opcion = lector.leerEntero("Objetivo (0=cancelar): ", 0, enemigos.size());
        return opcion == 0 ? null : enemigos.get(opcion - 1);
    }

    /** Quita del mapa a los enemigos derrotados y da los puntos. */
    private void retirarMuertos() {
        Iterator<Entidad> iterador = enemigos.iterator();
        while (iterador.hasNext()) {
            Entidad enemigo = iterador.next();
            if (!enemigo.estaVivo()) {
                tablero.retirar(enemigo);
                heroe.registrarVictoria(enemigo.getRecompensa());
                System.out.println("¡Derrotaste a " + enemigo.getNombre() + "! +" + enemigo.getRecompensa() + " puntos.");
                if (enemigo.esJefe()) {
                    jefeDerrotado = true;
                }
                iterador.remove();
            }
        }
    }

    private boolean usarObjeto() {
        ArrayList<Objeto> inventario = heroe.getInventario();
        if (inventario.isEmpty()) {
            System.out.println("[!] Tu inventario está vacío.");
            return false;
        }
        mostrarInventario();
        int opcion = lector.leerEntero("Objeto a usar (0=cancelar): ", 0, inventario.size());
        if (opcion == 0) {
            return false;
        }
        try {
            System.out.println(heroe.usarObjeto(opcion - 1));
            return true;
        } catch (RecursoInsuficienteException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    private boolean confirmarAbandono() {
        if (lector.leerEntero("¿Seguro que quieres abandonar? (1=Sí, 0=No): ", 0, 1) == 1) {
            abandono = true;
            return true;
        }
        return false;
    }

    // =====================================================================
    //  PANTALLAS
    // =====================================================================

    private void mostrarEstadoHeroe() {
        System.out.println("\n" + heroe.getNombre() + " el " + heroe.getClase());
        System.out.println("  Vida    " + barra(heroe.getVida(), heroe.getVidaMaxima()));
        System.out.println("  Energía " + barra(heroe.getEnergia(), heroe.getEnergiaMaxima()));
        System.out.println("  Puntos: " + heroe.getPuntos() + " | Objetos: " + heroe.getInventario().size()
                + " | Enemigos: " + enemigos.size());
    }

    /** Barra de progreso en texto, por ejemplo [######----] 6/10. */
    private String barra(int actual, int maximo) {
        int llenos = (int) Math.round(10.0 * actual / maximo);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            barra.append(i < llenos ? '#' : '-');
        }
        return barra.append("] ").append(actual).append("/").append(maximo).toString();
    }

    private void mostrarInventario() {
        ArrayList<Objeto> inventario = heroe.getInventario();
        System.out.println("Inventario:");
        if (inventario.isEmpty()) {
            System.out.println("  (vacío)");
        }
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + inventario.get(i));
        }
    }

    private void mostrarEnemigosEInventario() {
        mostrarInventario();
        System.out.println("Enemigos:");
        for (Entidad enemigo : enemigos) {
            System.out.println("  - " + enemigo.getNombre() + " " + barra(enemigo.getVida(), enemigo.getVidaMaxima()));
        }
    }

    private void mostrarResumenFinal() {
        String resultado;
        if (jefeDerrotado) {
            resultado = "*** ¡VICTORIA! Derrotaste al Dragón Ancestral ***";
        } else if (abandono) {
            resultado = "DERROTA: abandonaste la mazmorra.";
        } else if (!heroe.estaVivo()) {
            resultado = "DERROTA: tu héroe ha muerto.";
        } else {
            resultado = "DERROTA: se acabó el tiempo y la mazmorra se derrumbó.";
        }
        int bono = jefeDerrotado ? heroe.getVida() * 2 + (LIMITE_TURNOS - turno) : 0;
        int total = heroe.getPuntos() + bono;

        String rango;
        if (total >= 250) {
            rango = "Leyenda de Eldoria";
        } else if (total >= 150) {
            rango = "Aventurero veterano";
        } else if (total >= 60) {
            rango = "Aventurero";
        } else {
            rango = "Novato";
        }

        System.out.println("\n================= RESUMEN FINAL =================");
        System.out.println(resultado);
        System.out.println("Héroe: " + heroe.getNombre() + " (" + heroe.getClase() + ")");
        System.out.println("Turnos jugados: " + turno + " / " + LIMITE_TURNOS);
        System.out.println("Enemigos derrotados: " + heroe.getDerrotados());
        System.out.println("Puntos: " + heroe.getPuntos() + " + bono de victoria " + bono);
        System.out.println("PUNTAJE TOTAL: " + total + "  ->  Rango: " + rango);
        System.out.println("=================================================");
    }
}
