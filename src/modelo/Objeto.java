/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Objeto que se guarda en el inventario (por ejemplo, una poción). */
public final class Objeto {

    private String nombre;
    private int curaVida;
    private int curaEnergia;

    public Objeto(String nombre, int curaVida, int curaEnergia) {
        setNombre(nombre);
        setCuraVida(curaVida);
        setCuraEnergia(curaEnergia);
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El objeto necesita un nombre.");
        }
        this.nombre = nombre.trim();
    }

    public int getCuraVida() { return curaVida; }

    public void setCuraVida(int curaVida) {
        if (curaVida < 0) {
            throw new IllegalArgumentException("La curación no puede ser negativa.");
        }
        this.curaVida = curaVida;
    }

    public int getCuraEnergia() { return curaEnergia; }

    public void setCuraEnergia(int curaEnergia) {
        if (curaEnergia < 0) {
            throw new IllegalArgumentException("La energía no puede ser negativa.");
        }
        this.curaEnergia = curaEnergia;
    }

    @Override
    public String toString() {
        return nombre + " (vida +" + curaVida + ", energía +" + curaEnergia + ")";
    }
}
