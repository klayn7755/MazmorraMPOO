/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: González Gaspar Sergio Alexander
 *              Macario Quintero Kevin Alexis 
 *              Trujillo Sedano Eduardo
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import interfaces.Atacable;
import java.util.Random;

/** Clase base abstracta de todo ser vivo del juego (héroes y enemigos). */
public abstract class Entidad implements Atacable {

    private static final Random DADO = new Random();

    private String nombre;
    private int vidaMaxima;
    private int vida;
    private int ataque;
    private int defensa;
    private int recompensa;   // puntos que da al ser derrotada
    private int fila;
    private int columna;

    public Entidad(String nombre, int vidaMaxima, int ataque, int defensa) {
        setNombre(nombre);
        setVidaMaxima(vidaMaxima);
        setVida(vidaMaxima);
        setAtaque(ataque);
        setDefensa(defensa);
    }

    // ---------- Métodos abstractos: cada subclase los define a su manera ----------

    /** Letra con la que se dibuja en el mapa. */
    public abstract char getSimbolo();

    /** Daño de un ataque normal. */
    public abstract int calcularDanio();

    /** Distancia máxima (en casillas) a la que puede atacar. */
    public abstract int getAlcance();

    /** Lo que hace la entidad en su turno. */
    public abstract String actuar(Tablero tablero, Entidad objetivo);

    // ---------- Métodos concretos ----------

    /** Efecto automático al empezar cada turno (por defecto, ninguno). */
    public String alIniciarTurno() {
        return "";
    }

    public boolean esJefe() {
        return false;
    }

    protected final int tirarDado(int caras) {
        return DADO.nextInt(caras) + 1;
    }

    /** Ataque estilo Dungeons & Dragons: se tira un d20 para ver si acierta. */
    public String atacar(Entidad objetivo) {
        int tirada = tirarDado(20);
        boolean acierta = tirada == 20 || (tirada != 1 && tirada + ataque >= 10 + objetivo.getDefensa());
        if (!acierta) {
            return " [" + tirada + "] " + nombre + " ataca a " + objetivo.getNombre() + " pero falla.";
        }
        int danio = calcularDanio();
        String extra = "";
        if (tirada == 20) {
            danio *= 2;
            extra = " GOLPE CRITICO!";
        }
        int real = objetivo.recibirDanio(danio);
        return "[" + tirada + "] " + nombre + " golpea a " + objetivo.getNombre()
                + " y causa " + real + " de dano." + extra;
    }

    @Override
    public int recibirDanio(int danio) {
        int real = Math.max(1, danio - defensa);
        setVida(vida - real);
        return real;
    }

    @Override
    public boolean estaVivo() {
        return vida > 0;
    }

    /** Distancia en casillas rectas (sin diagonales). */
    public int distanciaA(Entidad otra) {
        return Math.abs(fila - otra.fila) + Math.abs(columna - otra.columna);
    }

    // ---------- Getters y setters (con validación) ----------

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
            throw new IllegalArgumentException("La vida maxima debe ser positiva.");
        }
        this.vidaMaxima = vidaMaxima;
    }

    public int getVida() { return vida; }

    /** La vida siempre queda entre 0 y la vida máxima. */
    public final void setVida(int vida) {
        this.vida = Math.max(0, Math.min(vida, vidaMaxima));
    }

    public int getAtaque() { return ataque; }

    public final void setAtaque(int ataque) {
        if (ataque < 0) {
            throw new IllegalArgumentException("El ataque no puede ser negativo.");
        }
        this.ataque = ataque;
    }

    public int getDefensa() { return defensa; }

    public final void setDefensa(int defensa) {
        if (defensa < 0) {
            throw new IllegalArgumentException("La defensa no puede ser negativa.");
        }
        this.defensa = defensa;
    }

    public int getRecompensa() { return recompensa; }

    public final void setRecompensa(int recompensa) {
        if (recompensa < 0) {
            throw new IllegalArgumentException("La recompensa no puede ser negativa.");
        }
        this.recompensa = recompensa;
    }

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
