/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

import interfaces.Interactuable;

/** Casilla del tablero: tiene un terreno y puede contener una entidad y/o un objeto interactuable. */
public class Celda {

    private TipoCelda tipo;
    private Entidad entidad;
    private Interactuable objeto;

    public Celda(TipoCelda tipo) {
        setTipo(tipo);
    }

    public TipoCelda getTipo() { return tipo; }

    public void setTipo(TipoCelda tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("La celda necesita un tipo de terreno.");
        }
        this.tipo = tipo;
    }

    public Entidad getEntidad() { return entidad; }

    public void setEntidad(Entidad entidad) { this.entidad = entidad; }

    public Interactuable getObjeto() { return objeto; }

    public void setObjeto(Interactuable objeto) { this.objeto = objeto; }

    /** Una celda está libre si es transitable y no tiene entidad ni objeto. */
    public boolean estaLibre() {
        return tipo.esTransitable() && entidad == null && objeto == null;
    }

    /** Símbolo a dibujar: prioriza la entidad, luego el objeto y por último el terreno. */
    public char getSimbolo() {
        if (entidad != null) {
            return entidad.getSimbolo();
        }
        if (objeto != null) {
            return objeto.getSimbolo();
        }
        return tipo.getSimbolo();
    }
}