/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.FueraDeAlcanceException;
import interfaces.Atacable;
import java.util.Random;

/** Clase base abstracta de todo ser con vida que habita el tablero (héroes y enemigos). */
public abstract class Entidad implements Atacable {

    private static final Random DADO = new Random();

    private String nombre;
    private int vidaMaxima;
    private int vidaActual;
    private int ataqueBase;
    private int defensa;
    private int puntosOtorgados;
    private int ultimoDanioRecibido;
    private int fila;
    private int columna;

    protected Entidad(String nombre, int vidaMaxima, int ataqueBase, int defensa) {
        setNombre(nombre);
        setVidaMaxima(vidaMaxima);
        setVidaActual(vidaMaxima);
        setAtaqueBase(ataqueBase);
        setDefensa(defensa);
    }

    // ---------- Métodos abstractos: cada subclase define su propio comportamiento ----------

    /** Carácter con el que se dibuja la entidad en el mapa. */
    public abstract char getSimbolo();

    /** Daño de un ataque básico (antes de aplicar la defensa del objetivo). */
    public abstract int calcularDanio();

    /** Distancia máxima (en casillas) a la que puede atacar. */
    public abstract int getAlcance();

    /** Acción que realiza la entidad durante su turno. */
    public abstract String actuar(Tablero tablero, Entidad objetivo);

    // ---------- Métodos concretos (con ganchos que las subclases pueden sobrescribir) ----------

    /** Efecto pasivo al iniciar el turno; por defecto no hace nada. */
    public String alIniciarTurno() {
        return "";
    }

    /** Indica si derrotar a esta entidad otorga la victoria. */
    public boolean esJefe() {
        return false;
    }

    protected final int tirarDado(int caras) {
        return DADO.nextInt(caras) + 1;
    }

    /** Ataque básico: valida el alcance, calcula el daño y se lo aplica al objetivo. */
    public String atacar(Entidad objetivo) throws FueraDeAlcanceException {
        int distancia = distanciaA(objetivo);
        if (distancia > getAlcance()) {
            throw new FueraDeAlcanceException(objetivo.getNombre() + " está a " + distancia
                    + " casillas y el alcance es " + getAlcance() + ".");
        }
        objetivo.recibirDanio(calcularDanio());
        return nombre + " ataca a " + objetivo.getNombre() + " y le causa "
                + objetivo.getUltimoDanioRecibido() + " de daño.";
    }

    @Override
    public void recibirDanio(int danio) {
        aplicarDanio(Math.max(1, danio - defensa));
    }

    /** Daño que ignora la defensa (hechizos, aliento de fuego). */
    public void recibirDanioDirecto(int danio) {
        aplicarDanio(Math.max(1, danio));
    }

    private void aplicarDanio(int danioReal) {
        ultimoDanioRecibido = danioReal;
        setVidaActual(vidaActual - danioReal);
    }

    protected void restaurarVida(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La curación debe ser positiva.");
        }
        setVidaActual(vidaActual + cantidad);
    }

    @Override
    public boolean estaVivo() {
        return vidaActual > 0;
    }

    /** Distancia Manhattan entre dos entidades. */
    public int distanciaA(Entidad otra) {
        return Math.abs(fila - otra.fila) + Math.abs(columna - otra.columna);
    }

    public String describirEstado() {
        return nombre + " [Vida " + vidaActual + "/" + vidaMaxima + "]";
    }

    // ---------- Getters y setters validados ----------

    @Override
    public String getNombre() { return nombre; }

    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getVidaMaxima() { return vidaMaxima; }

    public final void setVidaMaxima(int vidaMaxima) {
        if (vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva.");
        }
        this.vidaMaxima = vidaMaxima;
    }

    public int getVidaActual() { return vidaActual; }

    /** La vida siempre se mantiene entre 0 y la vida máxima. */
    public final void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaActual, vidaMaxima));
    }

    public int getAtaqueBase() { return ataqueBase; }

    public final void setAtaqueBase(int ataqueBase) {
        if (ataqueBase < 0) {
            throw new IllegalArgumentException("El ataque no puede ser negativo.");
        }
        this.ataqueBase = ataqueBase;
    }

    public int getDefensa() { return defensa; }

    public final void setDefensa(int defensa) {
        if (defensa < 0) {
            throw new IllegalArgumentException("La defensa no puede ser negativa.");
        }
        this.defensa = defensa;
    }

    public int getPuntosOtorgados() { return puntosOtorgados; }

    public final void setPuntosOtorgados(int puntosOtorgados) {
        if (puntosOtorgados < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        }
        this.puntosOtorgados = puntosOtorgados;
    }

    @Override
    public int getUltimoDanioRecibido() { return ultimoDanioRecibido; }

    public int getFila() { return fila; }

    public final void setFila(int fila) {
        if (fila < 0) {
            throw new IllegalArgumentException("La fila no puede ser negativa.");
        }
        this.fila = fila;
    }

    public int getColumna() { return columna; }

    public final void setColumna(int columna) {
        if (columna < 0) {
            throw new IllegalArgumentException("La columna no puede ser negativa.");
        }
        this.columna = columna;
    }
}