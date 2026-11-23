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

/** Héroe frágil que ataca desde lejos con hechizos. */
public class Mago extends Heroe {

    public Mago(String nombre) {
        super(nombre, 26, 4, 1, 12);
    }

    @Override
    public String getClase() { return "Mago"; }

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
        return getAtaque() + tirarDado(6);
    }

    /** Bola de Fuego: ignora la defensa del enemigo. */
    @Override
    protected String lanzarHabilidad(Entidad objetivo) {
        int danio = 8 + tirarDado(8);
        int real = objetivo.recibirDanio(danio + objetivo.getDefensa());
        return getNombre() + " lanza una BOLA DE FUEGO a " + objetivo.getNombre()
                + " y causa " + real + " de dano.";
    }
}
