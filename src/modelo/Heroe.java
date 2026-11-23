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

import excepciones.RecursoInsuficienteException;
import interfaces.Curable;
import java.util.ArrayList;

/** Clase abstracta de los héroes que puede elegir el jugador. */
public abstract class Heroe extends Entidad implements Curable {

    private static final int CAPACIDAD_INVENTARIO = 5;

    private int energiaMaxima;
    private int energia;
    private int puntos;
    private int derrotados;
    private final ArrayList<Objeto> inventario = new ArrayList<>();

    public Heroe(String nombre, int vidaMaxima, int ataque, int defensa, int energiaMaxima) {
        super(nombre, vidaMaxima, ataque, defensa);
        setEnergiaMaxima(energiaMaxima);
        setEnergia(energiaMaxima);
    }

    // ---------- Lo que cada clase de héroe debe definir ----------

    public abstract String getClase();

    public abstract String getNombreHabilidad();

    public abstract int getCostoHabilidad();

    protected abstract String lanzarHabilidad(Entidad objetivo);

    /** Alcance de la habilidad; por defecto igual al del ataque normal. */
    public int getAlcanceHabilidad() {
        return getAlcance();
    }

    // ---------- Comportamiento común ----------

    /** Revisa que haya energía suficiente y luego deja que cada clase lance su habilidad. */
    public final String usarHabilidad(Entidad objetivo) throws RecursoInsuficienteException {
        if (getCostoHabilidad() > energia) {
            throw new RecursoInsuficienteException("Energia insuficiente: necesitas "
                    + getCostoHabilidad() + " y tienes " + energia + ".");
        }
        energia -= getCostoHabilidad();
        return lanzarHabilidad(objetivo);
    }

    public void recuperarEnergia(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La energia recuperada debe ser positiva.");
        }
        setEnergia(energia + cantidad);
    }

    @Override
    public void curar(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La curacion debe ser positiva.");
        }
        setVida(getVida() + cantidad);
    }

    public void agregarObjeto(Objeto objeto) throws RecursoInsuficienteException {
        if (inventario.size() >= CAPACIDAD_INVENTARIO) {
            throw new RecursoInsuficienteException("Inventario lleno (maximo " + CAPACIDAD_INVENTARIO + ").");
        }
        inventario.add(objeto);
    }

    public String usarObjeto(int indice) throws RecursoInsuficienteException {
        if (indice < 0 || indice >= inventario.size()) {
            throw new RecursoInsuficienteException("No tienes ese objeto.");
        }
        Objeto objeto = inventario.remove(indice);
        if (objeto.getCuraVida() > 0) {
            curar(objeto.getCuraVida());
        }
        if (objeto.getCuraEnergia() > 0) {
            recuperarEnergia(objeto.getCuraEnergia());
        }
        return "Usas " + objeto.getNombre() + ": +" + objeto.getCuraVida() + " de vida, +"
                + objeto.getCuraEnergia() + " de energia.";
    }

    public void sumarPuntos(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("No se pueden sumar puntos negativos.");
        }
        puntos += cantidad;
    }

    public void registrarVictoria(int puntosGanados) {
        derrotados++;
        sumarPuntos(puntosGanados);
    }

    /** La acción "actuar" del héroe es descansar. */
    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        recuperarEnergia(3);
        curar(3);
        return getNombre() + " descansa y recupera 3 de energia y 3 de vida.";
    }

    /** Cada ronda el héroe recupera 1 punto de energía. */
    @Override
    public String alIniciarTurno() {
        recuperarEnergia(1);
        return "";
    }

    @Override
    public char getSimbolo() {
        return '@';
    }

    // ---------- Getters y setters ----------

    public int getEnergiaMaxima() { return energiaMaxima; }

    public final void setEnergiaMaxima(int energiaMaxima) {
        if (energiaMaxima <= 0) {
            throw new IllegalArgumentException("La energia maxima debe ser positiva.");
        }
        this.energiaMaxima = energiaMaxima;
    }

    public int getEnergia() { return energia; }

    public final void setEnergia(int energia) {
        this.energia = Math.max(0, Math.min(energia, energiaMaxima));
    }

    public int getPuntos() { return puntos; }

    public int getDerrotados() { return derrotados; }

    /** Devuelve una copia para que nadie modifique el inventario desde fuera. */
    public ArrayList<Objeto> getInventario() {
        return new ArrayList<>(inventario);
    }
}
