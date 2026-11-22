/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Héroe ágil de daño variable con posibilidad de golpe crítico. */
public class Picaro extends Heroe {

    public Picaro(String nombre) {
        super(nombre, 30, 5, 2, 10);
    }

    @Override
    public String getNombreClase() { return "Pícaro"; }

    @Override
    public String getNombreHabilidad() { return "Puñalada Trapera"; }

    @Override
    public int getCostoHabilidad() { return 3; }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int getAlcanceHabilidad() { return 1; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(8);
    }

    /** 30 % de probabilidad de crítico (daño x3). */
    @Override
    protected String ejecutarHabilidad(Entidad objetivo) {
        int danio = calcularDanio();
        String detalle = "";
        if (tirarDado(20) >= 15) {
            danio *= 3;
            detalle = " ¡GOLPE CRÍTICO!";
        }
        objetivo.recibirDanio(danio);
        return getNombre() + " clava una Puñalada Trapera a " + objetivo.getNombre()
                + " y le causa " + objetivo.getUltimoDanioRecibido() + " de daño." + detalle;
    }
}