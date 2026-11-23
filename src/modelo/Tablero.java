/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.MovimientoInvalidoException;

/** Mapa del juego: una matriz de terreno y una matriz de entidades (ambas 2D). */
public class Tablero {

    private final char[][] terreno;        // '#' muro, '.' suelo, '+' poción, '$' tesoro
    private final Entidad[][] entidades;   // quién está parado en cada casilla
    private final int filas;
    private final int columnas;

    public Tablero(String[] plano) {
        filas = plano.length;
        columnas = plano[0].length();
        terreno = new char[filas][columnas];
        entidades = new Entidad[filas][columnas];
        for (int f = 0; f < filas; f++) {
            if (plano[f].length() != columnas) {
                throw new IllegalArgumentException("Todas las filas del mapa deben medir lo mismo.");
            }
            terreno[f] = plano[f].toCharArray();
        }
    }

    public int getFilas() { return filas; }

    public int getColumnas() { return columnas; }

    /** Revisa que una casilla exista, no sea muro y esté libre. */
    private void validar(int fila, int columna) throws MovimientoInvalidoException {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            throw new MovimientoInvalidoException("Esa posicion está fuera del mapa.");
        }
        if (terreno[fila][columna] == '#') {
            throw new MovimientoInvalidoException("Hay un muro en ese camino.");
        }
        if (entidades[fila][columna] != null) {
            throw new MovimientoInvalidoException("Ahi está " + entidades[fila][columna].getNombre() + ".");
        }
    }

    public void colocar(Entidad entidad, int fila, int columna) throws MovimientoInvalidoException {
        validar(fila, columna);
        entidades[fila][columna] = entidad;
        entidad.setFila(fila);
        entidad.setColumna(columna);
    }

    /** Mueve una entidad una sola casilla (arriba, abajo, izquierda o derecha). */
    public void mover(Entidad entidad, int nuevaFila, int nuevaColumna) throws MovimientoInvalidoException {
        int pasos = Math.abs(nuevaFila - entidad.getFila()) + Math.abs(nuevaColumna - entidad.getColumna());
        if (pasos != 1) {
            throw new MovimientoInvalidoException("Solo puedes moverte una casilla a la vez.");
        }
        validar(nuevaFila, nuevaColumna);
        entidades[entidad.getFila()][entidad.getColumna()] = null;
        colocar(entidad, nuevaFila, nuevaColumna);
    }

    public void retirar(Entidad entidad) {
        entidades[entidad.getFila()][entidad.getColumna()] = null;
    }

    /** Da un paso hacia la meta. Devuelve false si está bloqueado. */
    public boolean acercar(Entidad entidad, Entidad meta) {
        int difFila = meta.getFila() - entidad.getFila();
        int difColumna = meta.getColumna() - entidad.getColumna();
        int pasoFila = Integer.signum(difFila);
        int pasoColumna = Integer.signum(difColumna);
        // Primero se intenta avanzar por el eje donde hay más distancia.
        int[][] intentos = Math.abs(difFila) >= Math.abs(difColumna)
                ? new int[][] {{pasoFila, 0}, {0, pasoColumna}}
                : new int[][] {{0, pasoColumna}, {pasoFila, 0}};
        for (int[] paso : intentos) {
            if (paso[0] == 0 && paso[1] == 0) {
                continue;
            }
            try {
                mover(entidad, entidad.getFila() + paso[0], entidad.getColumna() + paso[1]);
                return true;
            } catch (MovimientoInvalidoException e) {
                // Camino bloqueado: se prueba el otro eje.
            }
        }
        return false;
    }

    /** Recoge lo que haya en el suelo ('+' o '$') y deja la casilla vacía. */
    public char tomarItem(int fila, int columna) {
        char item = terreno[fila][columna];
        if (item == '+' || item == '$') {
            terreno[fila][columna] = '.';
            return item;
        }
        return '.';
    }

    public void ponerItem(int fila, int columna, char item) {
        terreno[fila][columna] = item;
    }

    /** Dibuja el mapa completo en consola. */
    public void imprimir() {
        System.out.print("     ");
        for (int c = 0; c < columnas; c++) {
            System.out.print((c % 10) + " ");
        }
        System.out.println();
        for (int f = 0; f < filas; f++) {
            System.out.printf("%2d   ", f);
            for (int c = 0; c < columnas; c++) {
                char simbolo = entidades[f][c] != null ? entidades[f][c].getSimbolo() : terreno[f][c];
                System.out.print(simbolo + " ");
            }
            System.out.println();
        }
    }
}
