package es.poo.actores;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Vector2D;
import es.poo.ventana.Ventana;

/**
 * La clase que extiende a Alien y representa a los objetos Ojo.<br><br>
 * 
 * Los Ojos son un tipo de Alien que aparecen para sorprender al jugador desde fuera de los bordes,
 * digamos que son obstaculos que se mueven por el area de movimiento del Jugador. Tiene un 
 * movimiento recto y horizontal que puede ser a la izquierda o a la derecha.<br><br>
 * 
 * Para su correcto funcionamiento han aparecer en una de las 3 filas en las que se mueve el Jugador (eje Y) 
 * y tienen que aparecer fuera del plano, es decir fuera del rango de los -100px y los 1200 px (eje X), 
 * con el objetivo de sorprender al Jugador.
 */
public class Ojos extends Alien{
	
	private static final BufferedImage[] framesOjo = Recursos.ojos;
	
	private int sentido;
	
	private double velocidad;

	/**
	 * Constructor de la clase Ojos.
	 * 
	 * @param posicion 	- Posicion inicial del Ojo.
	 * @param velocidad	- Velocidad de desplazamiento del Ojo.
	 * @param imagen	- Imagen que representa al Ojo.
	 * @param sentido	- La direccion en la que se mueve el Ojo (0 = hacia la derecha, 1 = hacia la izquierda).
	 */
	public Ojos(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion, double velocidad, int sentido) {
		super(posicion, imagen, vida, puntuacion);
		
		this.velocidad = velocidad;
		
		setFrames(framesOjo);
		
		this.sentido = sentido; 	// Sentido = 0(derecha) o otro(izquierda).
		if(sentido == 0) {
			indice = 4;
		}
		else {
			indice = 0;
		}
		
	}

	@Override
	public void actualizar() {
		
		if(temporizador.getActivo() == false) {
			indice++;
			if(indice >= 8 && sentido == 0) {
				indice = 4;
			}
			else if(indice >= 4 && sentido != 0){
				indice = 0;
			}
			temporizador.iniciar(FPS);
		}
		
		temporizador.actualizar();
	
		if(sentido == 0) {
			posicion = posicion.addX(velocidad);
			if(posicion.getX() > Ventana.ANCHO) {
				EstadoJuego.getFigurasMoviles().remove(this);
			}
		}
		else {
			posicion = posicion.subX(velocidad);
			
			if(posicion.getX() < -100) {
				EstadoJuego.getFigurasMoviles().remove(this);
			}
		}
		
		setCentro();
		colision();
	}
	
	public void dibujar(Graphics g) {
		super.dibujar(g);
		
		/* Debug gráfico
		g.setColor(Color.YELLOW);
		g.drawString("Xe", (int)getCentro().getX(), (int)getCentro().getY());
		
		g.drawRect((int)posicion.getX(), (int)posicion.getY(), super.ancho, super.alto);
		*/
	}
	

	/**
	 * Método totalmente sobreescrito que comprueba que la posicion inicial del Ojo está en alguna de las filas del
	 * jugador.
	 * 
	 * @throws IllegalArgumentException - Si la posicion inicial del Ojo está fuera de las filas del Jugador.
	 */
	protected void checkPosicion() throws IllegalArgumentException {
		
		double y = posicion.getY();
	
		if(y == EstadoJuego.getFilaJug(1) || y == EstadoJuego.getFilaJug(2) || y == EstadoJuego.getFilaJug(3)) {
			return;
		}
		else {
			throw new IllegalArgumentException("Para el correcto funcionamiento del juego se recomienda colocar"
					+ "los aliens Ojos en \nalguna de las filas del Jugador.");
		}
	}

	public void iniciarCronometro() {
		// Método vacío
	}
}
