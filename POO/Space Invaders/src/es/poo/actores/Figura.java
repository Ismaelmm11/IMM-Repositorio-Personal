package es.poo.actores;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import es.poo.math.Vector2D;

/**
 * Clase abstracta Figura que representa todos los objetos gráficos del videojuego.
 * Proporciona las propiedades básicas, así como métodos para actualizar su estado y imprimirse
 * en el Canvas.
 */
public abstract class Figura {

	//-------------------------------------[ Variables de las Figuras] ---------------------------------------------//
	
	protected BufferedImage imagen;		// Imagen que representa a la Figura.

	protected Vector2D posicion;		// Posicion de la Figura en el sistema de coordenadas.
	
	
	//-------------------------------------[ Métodos de Figura] ---------------------------------------------//
	
	
	/**
	 * Constructor de la clase Figura.
	 * @param posicion	- Posicion que tomará la Figura.
	 * @param imagen	- Imagen que representa a la Figura.
	 */
	public Figura(Vector2D posicion, BufferedImage imagen) {
		this.posicion = posicion;
		this.imagen = imagen;
	}
	
	
	/**
	 * Método que devuelve la posición actual de la Figura.
	 * @return Posición actual de la figura.
	 */
	public Vector2D getPosicion() {
		return posicion;
	}

	
	/**
	 * Método para establecer la posición de la Figura.
	 * @param posicion - Nueva posición de la Figura.
	 */
	public void setPosicion(Vector2D posicion) {
		this.posicion = posicion;
	}
	
	
	/**
	 * Método abstracto para actualizar el estado de la Figura.
	 * <br>Debe ser implementado por las <b>sublclases.</b>
	 */
	public abstract void actualizar();
	
	
	/**
	 * Método abstracto para dibujar la Figura en la pantalla.
	 * <br>Debe ser implementado por las <b>sublclases.</b>
	 * 
	 * @param g - Objeto gráfico.
	 */
	public abstract void dibujar(Graphics g);
	
	
}
