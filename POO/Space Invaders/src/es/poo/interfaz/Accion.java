package es.poo.interfaz;

/**
 * Interfaz que va a servir para especificar una accion al realizar.
 * Está pensada para usarse con la clase Boton, para así asignarle a cada
 * botón la accion que deseemos hacer.
 */
public interface Accion {

	/**
	 * Método abstracto en el que se debe definir una accion a realizar.
	 */
	public abstract void hacerAccion();
	
}
