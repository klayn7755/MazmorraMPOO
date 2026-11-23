/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Héroe ágil con mucho daño de golpe. */
public class Picaro extends Heroe {

    public Picaro(String nombre) {
        super(nombre, 30, 5, 2, 10);
    }

    @Override
    public String getClase() { return "Picaro"; }

    @Override
    public String getNombreHabilidad() { return "Punalada Trapera"; }

    @Override
    public int getCostoHabilidad() { return 4; }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int calcularDanio() {
        return getAtaque() + tirarDado(8);
    }

    /** Puñalada Trapera: daño triple. */
    @Override
    protected String lanzarHabilidad(Entidad objetivo) {
        int real = objetivo.recibirDanio(calcularDanio() * 3);
        return getNombre() + " clava una PUNALADA TRAPERA a " + objetivo.getNombre()
                + " y causa " + real + " de dano.";
    }
}
