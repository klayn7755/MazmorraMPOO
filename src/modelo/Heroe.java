/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import excepciones.FueraDeAlcanceException;
import excepciones.RecursoInsuficienteException;
import interfaces.Curable;
import java.util.ArrayList;

/** Clase abstracta de los personajes controlados por el jugador. Administra energía e inventario. */
public abstract class Heroe extends Entidad implements Curable {

    private static final int CAPACIDAD_INVENTARIO = 5;

    private int energiaMaxima;
    private int energiaActual;
    private int puntos;
    private int enemigosDerrotados;
    private final ArrayList<Objeto> inventario;

    protected Heroe(String nombre, int vidaMaxima, int ataqueBase, int defensa, int energiaMaxima) {
        super(nombre, vidaMaxima, ataqueBase, defensa);
        setEnergiaMaxima(energiaMaxima);
        setEnergiaActual(energiaMaxima);
        inventario = new ArrayList<>();
    }

    // ---------- Métodos abstractos propios de cada clase de héroe ----------

    public abstract String getNombreClase();

    public abstract String getNombreHabilidad();

    public abstract int getCostoHabilidad();

    public abstract int getAlcanceHabilidad();

    protected abstract String ejecutarHabilidad(Entidad objetivo);

    // ---------- Comportamiento común ----------

    /** Método plantilla: valida alcance y energía, y delega el efecto en la subclase. */
    public final String usarHabilidad(Entidad objetivo)
            throws RecursoInsuficienteException, FueraDeAlcanceException {
        int distancia = distanciaA(objetivo);
        if (distancia > getAlcanceHabilidad()) {
            throw new FueraDeAlcanceException(objetivo.getNombre() + " está a " + distancia
                    + " casillas y " + getNombreHabilidad() + " alcanza " + getAlcanceHabilidad() + ".");
        }
        gastarEnergia(getCostoHabilidad());
        return ejecutarHabilidad(objetivo);
    }

    public void gastarEnergia(int costo) throws RecursoInsuficienteException {
        if (costo > energiaActual) {
            throw new RecursoInsuficienteException("Energía insuficiente: necesitas " + costo
                    + " y tienes " + energiaActual + ".");
        }
        energiaActual -= costo;
    }

    public void recuperarEnergia(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La energía recuperada debe ser positiva.");
        }
        setEnergiaActual(energiaActual + cantidad);
    }

    @Override
    public void curar(int cantidad) {
        restaurarVida(cantidad);
    }

    public void agregarObjeto(Objeto objeto) throws RecursoInsuficienteException {
        if (inventario.size() >= CAPACIDAD_INVENTARIO) {
            throw new RecursoInsuficienteException("Inventario lleno (máximo " + CAPACIDAD_INVENTARIO + " objetos).");
        }
        inventario.add(objeto);
    }

    public String usarObjeto(int indice) throws RecursoInsuficienteException {
        if (indice < 0 || indice >= inventario.size()) {
            throw new RecursoInsuficienteException("No tienes ese objeto en el inventario.");
        }
        return inventario.remove(indice).aplicarEn(this);
    }

    public void sumarPuntos(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("No se pueden sumar puntos negativos.");
        }
        puntos += cantidad;
    }

    public void registrarVictoria(int puntosGanados) {
        enemigosDerrotados++;
        sumarPuntos(puntosGanados);
    }

    /** La acción de héroe en su turno es descansar: recupera energía y algo de vida. */
    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        int energia = getRecuperacionDescanso();
        recuperarEnergia(energia);
        curar(3);
        return getNombre() + " descansa: recupera " + energia + " de energía y 3 de vida.";
    }

    protected int getRecuperacionDescanso() {
        return 2;
    }

    /** Cada ronda el héroe recupera 1 punto de energía de forma pasiva. */
    @Override
    public String alIniciarTurno() {
        recuperarEnergia(1);
        return "";
    }

    @Override
    public char getSimbolo() {
        return '@';
    }

    @Override
    public String describirEstado() {
        return super.describirEstado() + " [Energía " + energiaActual + "/" + energiaMaxima + "]";
    }

    // ---------- Getters y setters validados ----------

    public int getEnergiaMaxima() { return energiaMaxima; }

    public final void setEnergiaMaxima(int energiaMaxima) {
        if (energiaMaxima <= 0) {
            throw new IllegalArgumentException("La energía máxima debe ser positiva.");
        }
        this.energiaMaxima = energiaMaxima;
    }

    public int getEnergiaActual() { return energiaActual; }

    public final void setEnergiaActual(int energiaActual) {
        this.energiaActual = Math.max(0, Math.min(energiaActual, energiaMaxima));
    }

    public int getPuntos() { return puntos; }

    public int getEnemigosDerrotados() { return enemigosDerrotados; }

    /** Devuelve una copia para que nadie modifique el inventario desde fuera. */
    public ArrayList<Objeto> getInventario() {
        return new ArrayList<>(inventario);
    }
}