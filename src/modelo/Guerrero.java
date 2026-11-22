/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Héroe resistente que pelea cuerpo a cuerpo. */
public class Guerrero extends Heroe {

    public Guerrero(String nombre) {
        super(nombre, 40, 6, 3, 8);
    }

    @Override
    public String getClase() { return "Guerrero"; }

    @Override
    public String getNombreHabilidad() { return "Golpe Brutal"; }

    @Override
    public int getCostoHabilidad() { return 3; }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int calcularDanio() {
        return getAtaque() + tirarDado(6);
    }

    /** Golpe Brutal: daño doble. */
    @Override
    protected String lanzarHabilidad(Entidad objetivo) {
        int real = objetivo.recibirDanio(calcularDanio() * 2);
        return getNombre() + " descarga un GOLPE BRUTAL sobre " + objetivo.getNombre()
                + " y causa " + real + " de daño.";
    }
}
