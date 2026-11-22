/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package interfaces;

import excepciones.RecursoInsuficienteException;
import modelo.Heroe;

/** Contrato para los objetos del mapa con los que el héroe puede interactuar. */
public interface Interactuable {

    String interactuar(Heroe heroe) throws RecursoInsuficienteException;

    boolean estaDisponible();

    char getSimbolo();
}