package es.poo.actores;

import java.awt.image.BufferedImage;

import es.poo.math.Vector2D;

/**
 * Clase 
 */
public abstract class Alien extends FiguraMovil{

	//-------------------------------------[ Parámetros del Alien] ----------------------------------//
	
	private static final int[] ejesY = {50, 125, 200, 275, 350};	// Valores del eje Y válidos
	private static final int[] ejesX = {100, 1100};					// Valores del eje X válidos
	
	// Puntuacion obtenida al matar al Alien.
	private int puntuacion;
	
	private int vida;
	
	/**
	 * Constructor de la clase Alien.
	 * 
	 * @param posicion	- Posicion inicial del Alien.
	 * @param imagen	- Imagen que representa al Alien.
	 * 
	 * @throws IllegalArgumentException Si la posicion inicial es inválida.
	 */
	public Alien(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion) throws IllegalArgumentException {
		super(posicion, imagen);
		this.vida = vida;
		this.puntuacion = puntuacion;
		
		checkPosicion();
	}
	
	
	/**
	 * Método que establece la puntuacion del Alien.
	 * 
	 * @param puntuacion - Puntuación a establecer.
	 */
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	/**
	 * Método que devuelve la puntuación del Alien.
	 * 
	 * @return - Puntuación del Alien.
	 */
	public int getPuntuacion() {
		return puntuacion;
	}
	
	public void setPosicion(Vector2D posicion) {
		checkPosicion();
		this.posicion = posicion;
	}
	
	protected void kill() {
		vida--;
		if(vida == 0) {
			super.kill();
		}
	}
	
	/**
	 * Método que verifica si la posicion inicial del Alien es válida.
	 * 
	 * @throws IllegalArgumentException - Si la posición inicial no es válida.
	 */
	protected void checkPosicion() throws IllegalArgumentException {
		
		if(checkEjeY() == false) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+ "colocar en el eje Y\n de los Aliens alguno de los siguientes valores"
					+ "(50, 125, 200, 275, 350) en px.\n");
		}
		
		if(checkEjeX() == false) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación, se recomienda que"
					+	"el eje X de los \nAliens esté entre los valores de 100px y 1100px.");
		}
	}
	
	/**
	 * Método que valida si la coordenada Y de la posición original es válida.
	 * Solo es valida si el Alien se coloca en los 50px, 125px, 200px, 275px, 350px
	 * en el eje Y.
	 * 
	 * @return True si es válida, False en caso contrario.
	 */
	private boolean checkEjeY() {
		double ejeY = posicion.getY();
		
		for(int i = 0; i <= ejesY.length; i++) {
			if(ejeY == ejesY[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que valida si la coordenada X de la posición origial es válida.
	 * Solo es valida si el Alien se coloca entre los 100px y 1100px del eje X. 
	 * 
	 * @return True en caso de que sea válida, False en caso contrario.
	 */
	private boolean checkEjeX() {
		double ejeX = posicion.getX();
		
		if(ejeX >= ejesX[0] && ejeX <= ejesX[1]) {
			return true;
		}
		
		return false;
	}
	
	public void setVida(int vida) {
		this.vida = vida;
	}
	
	public int getVida() {
		return vida;
	}
	
	public abstract void iniciarCronometro();
}
