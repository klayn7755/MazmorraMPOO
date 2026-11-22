/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package logica;

import excepciones.FueraDeAlcanceException;
import excepciones.MovimientoInvalidoException;
import excepciones.RecursoInsuficienteException;
import interfaces.Interactuable;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.*;

/** Controla el flujo completo del juego: menús, turnos, acciones del jugador y fin de partida. */
public class Juego {

    private static final int LIMITE_TURNOS = 80;
    private static final int[] DELTA_FILA = {-1, 1, 0, 0};
    private static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    /** Plano del mapa: '#' muro, '.' suelo. */
    private static final String[] PLANO_MAPA = {
        "##############",
        "#............#",
        "#.##......##.#",
        "#.#..........#",
        "#....##..#...#",
        "#....##..#...#",
        "#.#..........#",
        "#.##...##.##.#",
        "#............#",
        "##############"
    };

    private final LectorConsola lector;
    private Tablero tablero;
    private Heroe heroe;
    private ArrayList<Entidad> enemigos;
    private int turno;
    private boolean jefeDerrotado;
    private boolean abandono;

    public Juego() {
        lector = new LectorConsola();
        enemigos = new ArrayList<>();
    }

    public int getTurno() { return turno; }

    public Heroe getHeroe() { return heroe; }

    // =====================================================================
    //  MENÚ PRINCIPAL
    // =====================================================================

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = lector.leerEntero("Elige una opcion: ", 1, 3);
            switch (opcion) {
                case 1:
                    jugarPartida();
                    break;
                case 2:
                    mostrarInstrucciones();
                    break;
                default:
                    salir = true;
            }
        }
        System.out.println("\nHasta la próxima aventura");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n==========================================");
        System.out.println("      LAS MAZMORRAS DE ELDORIA");
        System.out.println("==========================================");
        System.out.println("  1) Nueva partida");
        System.out.println("  2) Instrucciones");
        System.out.println("  3) Salir");
    }

    private void mostrarInstrucciones() {
        System.out.println("\n--------------- INSTRUCCIONES ---------------");
        System.out.println("Eres un aventurero que desciende a una mazmorra infestada de monstruos.");
        System.out.println("OBJETIVO: derrotar al Dragon Ancestral (D) antes de que acaben " + LIMITE_TURNOS + " turnos.");
        System.out.println("DERROTA: morir, abandonar o agotar los turnos.");
        System.out.println();
        System.out.println("Cada turno realizas UNA accion y luego actuan los enemigos:");
        System.out.println("  - Moverte: una casilla arriba, abajo, izquierda o derecha.");
        System.out.println("  - Atacar: ataque basico segun el alcance de tu clase (distancia en casillas rectas).");
        System.out.println("  - Habilidad: ataque especial que gasta energia.");
        System.out.println("  - Interactuar: abre cofres (C) o bebe de fuentes (F) en una casilla vecina.");
        System.out.println("  - Usar objeto: consume pociones del inventario (maximo 5).");
        System.out.println("  - Descansar: recupera energia y algo de vida.");
        System.out.println();
        System.out.println("Clases: Guerrero (resistente), Mago (ataque a distancia), Picaro (criticos).");
        System.out.println("Los enemigos te persiguen si te acercas, explora con cuidado");
        mostrarLeyenda();
    }

    private void mostrarLeyenda() {
        System.out.println("Leyenda: @ tú | g Goblin | e Esqueleto | T Troll | D Dragón | C/c cofre | F/f fuente | # muro");
    }

    // =====================================================================
    //  PREPARACIÓN DE LA PARTIDA
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
        System.out.println("\n=== CREACION DE PERSONAJE ===");
        String nombre = lector.leerTexto("Nombre de tu heroe: ");
        System.out.println("  1) Guerrero - vida 40, defensa alta, cuerpo a cuerpo");
        System.out.println("  2) Mago     - vida 26, mucha energía, ataca a distancia");
        System.out.println("  3) Picaro   - vida 30, n variable y golpes criticos");
        int clase = lector.leerEntero("Elige tu clase: ", 1, 3);
        switch (clase) {
            case 1:
                heroe = new Guerrero(nombre);
                break;
            case 2:
                heroe = new Mago(nombre);
                break;
            default:
                heroe = new Picaro(nombre);
        }

        enemigos = new ArrayList<>();
        try {
            heroe.agregarObjeto(new Objeto("Pocion de vida", TipoObjeto.POCION_VIDA, 15));
            heroe.agregarObjeto(new Objeto("Pocion de vida", TipoObjeto.POCION_VIDA, 15));
            heroe.agregarObjeto(new Objeto("Pocion de energia", TipoObjeto.POCION_ENERGIA, 6));

            tablero = new Tablero(PLANO_MAPA);
            tablero.colocarEntidad(heroe, 1, 1);
            tablero.colocarObjeto(new Cofre(new Objeto("Pocion de vida", TipoObjeto.POCION_VIDA, 15), 15), 2, 6);
            tablero.colocarObjeto(new Cofre(new Objeto("Pocion de energia", TipoObjeto.POCION_ENERGIA, 6), 20), 5, 11);
            tablero.colocarObjeto(new Cofre(new Objeto("Pocion de vida", TipoObjeto.POCION_VIDA, 15), 25), 8, 2);
            tablero.colocarObjeto(new Fuente(2, 15, 5), 5, 2);

            colocarEnemigo(new Goblin(), 3, 9);
            colocarEnemigo(new Goblin(), 1, 11);
            colocarEnemigo(new Esqueleto(), 6, 7);
            colocarEnemigo(new Troll(), 8, 5);
            colocarEnemigo(new Dragon(), 8, 11);
        } catch (RecursoInsuficienteException | MovimientoInvalidoException e) {
            System.out.println("Error al preparar la partida: " + e.getMessage());
            return false;
        }

        turno = 0;
        jefeDerrotado = false;
        abandono = false;
        System.out.println("\n¡Bienvenido, " + heroe.getNombre() + " el " + heroe.getNombreClase()
                + "Empieza tu aventura.");
        return true;
    }

    private void colocarEnemigo(Entidad enemigo, int fila, int columna) throws MovimientoInvalidoException {
        tablero.colocarEntidad(enemigo, fila, columna);
        enemigos.add(enemigo);
    }

    // =====================================================================
    //  TURNOS
    // =====================================================================

    private void ejecutarTurno() {
        turno++;
        System.out.println("\n=================== TURNO " + turno + " / " + LIMITE_TURNOS + " ===================");
        tablero.imprimir();
        mostrarLeyenda();
        mostrarEstadoHeroe();

        boolean turnoConsumido = false;
        while (!turnoConsumido && !abandono && !jefeDerrotado) {
            turnoConsumido = procesarAccion(leerAccion());
        }
        if (!jefeDerrotado && !abandono) {
            faseEnemigos();
        }
    }

    private int leerAccion() {
        System.out.println("\n  1) Moverse");
        System.out.println("  2) Ataque básico");
        System.out.println("  3) Habilidad: " + heroe.getNombreHabilidad() + " (" + heroe.getCostoHabilidad()
                + " energía, alcance " + heroe.getAlcanceHabilidad() + ")");
        System.out.println("  4) Interactuar (cofre / fuente cercanos)");
        System.out.println("  5) Usar objeto");
        System.out.println("  6) Descansar");
        System.out.println("  7) Ver inventario y enemigos (no gasta turno)");
        System.out.println("  0) Abandonar partida");
        return lector.leerEntero("Accion: ", 0, 7);
    }

    /** Ejecuta la acción elegida. Devuelve true si consumió el turno. */
    private boolean procesarAccion(int opcion) {
        switch (opcion) {
            case 1:
                return moverHeroe();
            case 2:
                return atacar(false);
            case 3:
                return atacar(true);
            case 4:
                return interactuar();
            case 5:
                return usarObjeto();
            case 6:
                System.out.println(heroe.actuar(tablero, null));
                return true;
            case 7:
                mostrarInventarioYEnemigos();
                return false;
            default:
                return confirmarAbandono();
        }
    }

    /** Los enemigos actúan en orden; cada uno decide qué hacer según su propia clase (polimorfismo). */
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
        int nuevaFila = heroe.getFila() + DELTA_FILA[direccion - 1];
        int nuevaColumna = heroe.getColumna() + DELTA_COLUMNA[direccion - 1];
        try {
            tablero.moverEntidad(heroe, nuevaFila, nuevaColumna);
            System.out.println("Te mueves a (" + nuevaFila + "," + nuevaColumna + ").");
            return true;
        } catch (MovimientoInvalidoException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    private boolean atacar(boolean usarHabilidad) {
        Entidad objetivo = elegirObjetivo();
        if (objetivo == null) {
            return false;
        }
        try {
            String resultado = usarHabilidad ? heroe.usarHabilidad(objetivo) : heroe.atacar(objetivo);
            System.out.println(resultado);
            procesarBajas();
            return true;
        } catch (FueraDeAlcanceException | RecursoInsuficienteException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    private Entidad elegirObjetivo() {
        if (enemigos.isEmpty()) {
            System.out.println("No quedan enemigos.");
            return null;
        }
        System.out.println("Objetivos:");
        for (int i = 0; i < enemigos.size(); i++) {
            Entidad enemigo = enemigos.get(i);
            System.out.println("  " + (i + 1) + ") " + enemigo.describirEstado() + " en ("
                    + enemigo.getFila() + "," + enemigo.getColumna() + ") - distancia " + heroe.distanciaA(enemigo));
        }
        int opcion = lector.leerEntero("Objetivo (0=cancelar): ", 0, enemigos.size());
        return opcion == 0 ? null : enemigos.get(opcion - 1);
    }

    /** Retira del mapa a los enemigos muertos, otorga puntos y detecta la muerte del jefe. */
    private void procesarBajas() {
        Iterator<Entidad> iterador = enemigos.iterator();
        while (iterador.hasNext()) {
            Entidad enemigo = iterador.next();
            if (!enemigo.estaVivo()) {
                tablero.retirarEntidad(enemigo);
                heroe.registrarVictoria(enemigo.getPuntosOtorgados());
                System.out.println("¡Has derrotado a " + enemigo.getNombre() + "! (+"
                        + enemigo.getPuntosOtorgados() + " puntos)");
                if (enemigo.esJefe()) {
                    jefeDerrotado = true;
                }
                iterador.remove();
            }
        }
    }

    private boolean interactuar() {
        Interactuable objeto = tablero.obtenerInteractuableAdyacente(heroe);
        if (objeto == null) {
            System.out.println("[!] No hay nada con qué interactuar en las casillas vecinas.");
            return false;
        }
        try {
            System.out.println(objeto.interactuar(heroe));
            return true;
        } catch (RecursoInsuficienteException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    private boolean usarObjeto() {
        if (heroe.getInventario().isEmpty()) {
            System.out.println("[!] Tu inventario está vacío.");
            return false;
        }
        mostrarInventario();
        int opcion = lector.leerEntero("Objeto a usar (0=cancelar): ", 0, heroe.getInventario().size());
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
        int respuesta = lector.leerEntero("¿Seguro que quieres abandonar? (1=Sí, 0=No): ", 0, 1);
        if (respuesta == 1) {
            abandono = true;
            return true;
        }
        return false;
    }

    // =====================================================================
    //  PANTALLAS INFORMATIVAS
    // =====================================================================

    private void mostrarEstadoHeroe() {
        System.out.println("\n" + heroe.getNombre() + " el " + heroe.getNombreClase());
        System.out.println("  Vida    " + barra(heroe.getVidaActual(), heroe.getVidaMaxima()));
        System.out.println("  Energía " + barra(heroe.getEnergiaActual(), heroe.getEnergiaMaxima()));
        System.out.println("  Puntos: " + heroe.getPuntos() + " | Objetos: " + heroe.getInventario().size()
                + " | Enemigos restantes: " + enemigos.size());
    }

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
            return;
        }
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + inventario.get(i));
        }
    }

    private void mostrarInventarioYEnemigos() {
        mostrarInventario();
        System.out.println("Enemigos en la mazmorra:");
        for (Entidad enemigo : enemigos) {
            System.out.println("  - " + enemigo.describirEstado());
        }
    }

    private void mostrarResumenFinal() {
        String resultado;
        if (jefeDerrotado) {
            resultado = "¡VICTORIA! Derrotaste al Dragón Ancestral.";
        } else if (abandono) {
            resultado = "DERROTA: abandonaste la mazmorra.";
        } else if (!heroe.estaVivo()) {
            resultado = "DERROTA: tu héroe ha muerto.";
        } else {
            resultado = "DERROTA: se agotaron los turnos y la mazmorra se derrumbó.";
        }
        int bono = jefeDerrotado ? heroe.getVidaActual() * 2 + (LIMITE_TURNOS - turno) : 0;

        System.out.println("\n================ RESUMEN FINAL ================");
        System.out.println(resultado);
        System.out.println("Héroe: " + heroe.getNombre() + " (" + heroe.getNombreClase() + ")");
        System.out.println("Turnos jugados: " + turno + " / " + LIMITE_TURNOS);
        System.out.println("Enemigos derrotados: " + heroe.getEnemigosDerrotados());
        System.out.println("Puntos obtenidos: " + heroe.getPuntos());
        System.out.println("Bono de victoria: " + bono);
        System.out.println("PUNTAJE TOTAL: " + (heroe.getPuntos() + bono));
        if (!enemigos.isEmpty()) {
            System.out.println("Enemigos que quedaron en pie:");
            for (Entidad enemigo : enemigos) {
                System.out.println("  - " + enemigo.describirEstado());
            }
        }
        System.out.println("===============================================");
    }
}