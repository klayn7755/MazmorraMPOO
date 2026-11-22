/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Héroe frágil de ataque a distancia con mucha energía y hechizos que ignoran la defensa. */
public class Mago extends Heroe {

    public Mago(String nombre) {
        super(nombre, 26, 4, 1, 12);
    }

    @Override
    public String getNombreClase() { return "Mago"; }

    @Override
    public String getNombreHabilidad() { return "Bola de Fuego"; }

    @Override
    public int getCostoHabilidad() { return 4; }

    @Override
    public int getAlcance() { return 2; }

    @Override
    public int getAlcanceHabilidad() { return 4; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(6);
    }

    /** Meditar le devuelve más energía que a las otras clases. */
    @Override
    protected int getRecuperacionDescanso() {
        return 4;
    }

    @Override
    protected String ejecutarHabilidad(Entidad objetivo) {
        objetivo.recibirDanioDirecto(8 + tirarDado(8));
        return getNombre() + " lanza una Bola de Fuego a " + objetivo.getNombre()
                + " y le causa " + objetivo.getUltimoDanioRecibido() + " de daño mágico.";
    }
}