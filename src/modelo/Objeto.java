/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Objeto consumible que se guarda en el inventario del héroe. */
public class Objeto {

    private String nombre;
    private TipoObjeto tipo;
    private int potencia;

    public Objeto(String nombre, TipoObjeto tipo, int potencia) {
        setNombre(nombre);
        setTipo(tipo);
        setPotencia(potencia);
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El objeto necesita un nombre.");
        }
        this.nombre = nombre.trim();
    }

    public TipoObjeto getTipo() { return tipo; }

    public void setTipo(TipoObjeto tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El objeto necesita un tipo.");
        }
        this.tipo = tipo;
    }

    public int getPotencia() { return potencia; }

    public void setPotencia(int potencia) {
        if (potencia <= 0) {
            throw new IllegalArgumentException("La potencia debe ser positiva.");
        }
        this.potencia = potencia;
    }

    /** Consume el objeto aplicando su efecto sobre el héroe y devuelve el mensaje del resultado. */
    public String aplicarEn(Heroe heroe) {
        switch (tipo) {
            case POCION_VIDA:
                heroe.curar(potencia);
                return "Bebes " + nombre + " y recuperas " + potencia + " de vida.";
            case POCION_ENERGIA:
                heroe.recuperarEnergia(potencia);
                return "Bebes " + nombre + " y recuperas " + potencia + " de energía.";
            default:
                return "El objeto no tiene efecto.";
        }
    }

    @Override
    public String toString() {
        return nombre + " (+" + potencia + ")";
    }
}