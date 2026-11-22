/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Héroe resistente de combate cuerpo a cuerpo con armadura pesada. */
public class Guerrero extends Heroe {

    public Guerrero(String nombre) {
        super(nombre, 40, 6, 3, 8);
    }

    @Override
    public String getNombreClase() { return "Guerrero"; }

    @Override
    public String getNombreHabilidad() { return "Golpe Brutal"; }

    @Override
    public int getCostoHabilidad() { return 3; }

    @Override
    public int getAlcance() { return 1; }

    @Override
    public int getAlcanceHabilidad() { return 1; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(6);
    }

    /** La armadura pesada reduce 2 puntos extra de cada golpe recibido. */
    @Override
    public void recibirDanio(int danio) {
        super.recibirDanio(Math.max(1, danio - 2));
    }

    @Override
    protected String ejecutarHabilidad(Entidad objetivo) {
        objetivo.recibirDanio(calcularDanio() * 2);
        return getNombre() + " descarga un Golpe Brutal sobre " + objetivo.getNombre()
                + " y le causa " + objetivo.getUltimoDanioRecibido() + " de daño.";
    }
}