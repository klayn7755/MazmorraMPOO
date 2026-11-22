/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.MovimientoInvalidoException;
import interfaces.Interactuable;

/** Mapa del juego representado con una matriz bidimensional de celdas. */
public class Tablero {

    private static final int[] DELTA_FILA = {-1, 1, 0, 0};
    private static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    private final Celda[][] celdas;
    private final int filas;
    private final int columnas;

    /** Construye el mapa desde un plano de texto: '#' es muro y cualquier otro carácter es suelo. */
    public Tablero(String[] plano) {
        if (plano == null || plano.length == 0) {
            throw new IllegalArgumentException("El plano del mapa no puede estar vacío.");
        }
        filas = plano.length;
        columnas = plano[0].length();
        celdas = new Celda[filas][columnas];
        for (int f = 0; f < filas; f++) {
            if (plano[f].length() != columnas) {
                throw new IllegalArgumentException("Todas las filas del plano deben medir lo mismo.");
            }
            for (int c = 0; c < columnas; c++) {
                celdas[f][c] = new Celda(plano[f].charAt(c) == '#' ? TipoCelda.MURO : TipoCelda.SUELO);
            }
        }
    }

    public int getFilas() { return filas; }

    public int getColumnas() { return columnas; }

    private boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    /** Verifica que una casilla pueda recibir una entidad u objeto; si no, lanza la excepción. */
    private void validarDestino(int fila, int columna) throws MovimientoInvalidoException {
        if (!estaDentro(fila, columna)) {
            throw new MovimientoInvalidoException("La posición (" + fila + "," + columna + ") está fuera del mapa.");
        }
        Celda destino = celdas[fila][columna];
        if (!destino.getTipo().esTransitable()) {
            throw new MovimientoInvalidoException("Un muro bloquea el paso en (" + fila + "," + columna + ").");
        }
        if (destino.getEntidad() != null) {
            throw new MovimientoInvalidoException("La casilla está ocupada por " + destino.getEntidad().getNombre() + ".");
        }
        if (destino.getObjeto() != null) {
            throw new MovimientoInvalidoException("Un objeto bloquea el paso en (" + fila + "," + columna + ").");
        }
    }

    public void colocarEntidad(Entidad entidad, int fila, int columna) throws MovimientoInvalidoException {
        validarDestino(fila, columna);
        celdas[fila][columna].setEntidad(entidad);
        entidad.setFila(fila);
        entidad.setColumna(columna);
    }

    /** Mueve una entidad exactamente una casilla en línea recta (sin diagonales). */
    public void moverEntidad(Entidad entidad, int nuevaFila, int nuevaColumna) throws MovimientoInvalidoException {
        int pasos = Math.abs(nuevaFila - entidad.getFila()) + Math.abs(nuevaColumna - entidad.getColumna());
        if (pasos != 1) {
            throw new MovimientoInvalidoException("Solo puedes moverte una casilla en línea recta.");
        }
        validarDestino(nuevaFila, nuevaColumna);
        celdas[entidad.getFila()][entidad.getColumna()].setEntidad(null);
        colocarEntidad(entidad, nuevaFila, nuevaColumna);
    }

    public void retirarEntidad(Entidad entidad) {
        celdas[entidad.getFila()][entidad.getColumna()].setEntidad(null);
    }

    public void colocarObjeto(Interactuable objeto, int fila, int columna) throws MovimientoInvalidoException {
        validarDestino(fila, columna);
        celdas[fila][columna].setObjeto(objeto);
    }

    /** Da un paso hacia la meta, probando primero el eje con mayor distancia. Devuelve si se pudo mover. */
    public boolean acercarEntidad(Entidad entidad, Entidad meta) {
        int difFila = meta.getFila() - entidad.getFila();
        int difColumna = meta.getColumna() - entidad.getColumna();
        int pasoFila = Integer.signum(difFila);
        int pasoColumna = Integer.signum(difColumna);
        int[][] intentos = Math.abs(difFila) >= Math.abs(difColumna)
                ? new int[][] {{pasoFila, 0}, {0, pasoColumna}}
                : new int[][] {{0, pasoColumna}, {pasoFila, 0}};
        for (int[] paso : intentos) {
            if (paso[0] == 0 && paso[1] == 0) {
                continue;
            }
            try {
                moverEntidad(entidad, entidad.getFila() + paso[0], entidad.getColumna() + paso[1]);
                return true;
            } catch (MovimientoInvalidoException e) {
                // Esa dirección está bloqueada: se prueba la siguiente.
            }
        }
        return false;
    }

    /** Busca un objeto disponible en las cuatro casillas vecinas de la entidad. */
    public Interactuable obtenerInteractuableAdyacente(Entidad entidad) {
        for (int i = 0; i < DELTA_FILA.length; i++) {
            int fila = entidad.getFila() + DELTA_FILA[i];
            int columna = entidad.getColumna() + DELTA_COLUMNA[i];
            if (estaDentro(fila, columna)) {
                Interactuable objeto = celdas[fila][columna].getObjeto();
                if (objeto != null && objeto.estaDisponible()) {
                    return objeto;
                }
            }
        }
        return null;
    }

    /** Dibuja el mapa completo en consola. */
    public void imprimir() {
        System.out.print("    ");
        for (int c = 0; c < columnas; c++) {
            System.out.print((c % 10) + " ");
        }
        System.out.println();
        for (int f = 0; f < filas; f++) {
            System.out.printf("%2d  ", f);
            for (int c = 0; c < columnas; c++) {
                System.out.print(celdas[f][c].getSimbolo() + " ");
            }
            System.out.println();
        }
    }
}