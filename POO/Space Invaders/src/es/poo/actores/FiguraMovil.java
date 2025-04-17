package es.poo.actores;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import es.poo.estado.EstadoJuego;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

/**
 * La clase abstracta FiguraMovil extiende la clase Figura, esta representa a todo objeto
 * gráfico que pueda moverse y tenga una animación.
 */
public abstract class FiguraMovil extends Figura {
	
	//-------------------------------------[ Parámetros de FiguraMovil] ----------------------------------//
	
	protected Vector2D centro;				// Posicion del centro de la Figura.
	
	protected int alto;						// Alto de la Figura.
	
	protected int ancho;					// Ancho de la Figura.
	
	protected BufferedImage[] frames;		// Vector de imagenes que componen la animacion.
	
	protected int indice;					// Indice del fotograma actual de la animacion.
	
	protected int FPS;						// FPS para marcar la velocidad de la animacion.
	
	protected Cronometro temporizador;		// Cronometro para controlar la animacion.
	
	private boolean muerto;					// Booleano que indica si ha muerto o no.
	
	protected Random random = new Random();	// Generador de numeros aleatorios.
	
	
	//-------------------------------------[ Métodos de FiguraMovil ] ----------------------------------//
	
	/**
	 * Método sobreescrito constructor de FiguraMovil que inicializa todas las variables. Establece
	 * el alto y el ancho, la posicion central y prepara el objeto para animarlo.
	 * 
	 * @param posicion	- Posicion inicial de la Figura. 
	 * @param imagen	- Imagen que representa a la Figura.
	 */
	public FiguraMovil(Vector2D posicion, BufferedImage imagen) {
		super(posicion, imagen);
		
		FPS = 125;
		
		alto = imagen.getHeight();
		ancho = imagen.getWidth();
		
		centro = new Vector2D(posicion.getX() + ancho/2, posicion.getY() + alto/2);

		indice = 0;
		
		muerto = false;
		
		temporizador = new Cronometro();
		temporizador.iniciar(FPS);
	}
	
	/**
	 * Método que establece los FPS de la Figura.
	 * @param FPS - Frames por Segundo
	 */
	protected void setFPS(int FPS) {
		this.FPS = FPS;
	}
	
	/**
	 * Método que calcula el punto central de la Figura.
	 */
	protected void setCentro() {
		centro = new Vector2D(posicion.getX() + ancho/2, posicion.getY() + alto/2);
	}
	
	/**
	 * Método para establecer un conjunto de imagenes que formaran la animacion.
	 * 
	 * @param frames - Conjunto(Vector) de Imagenes.
	 */
	protected void setFrames(BufferedImage[] frames) {
		this.frames = frames;
	}
	
	/**
	 * Método que establece el estado de "muerto" de la figura.
	 * 
	 * @param muerto - Estado "muerto" de la Figura.
	 */
	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}
	
	/**
	 * Método para obtener la posición central de la Figura.
	 * 
	 * @return Posición central de la imagen.
	 */
	public Vector2D getCentro() {
		return centro;
	}
	
	/**
	 * Método que devuelve el booleano muerto.
	 * 
	 * @return True si la figura está muerta, False en otro caso.
	 */
	public boolean getMuerto() {
		return muerto;
	}
	
	@Override
	/**
	 * Método que actualiza el estado de la Figura.
	 * <br> Se encarga de animar la figura y de calcular constantemente el punto
	 * central y comprobar si está colisionando con otra FiguraMovil.
	 */
	public void actualizar() {
		
		if(temporizador.getActivo() == false) {
			indice++;
			if(indice >= frames.length) {
				indice=0;
			}
			temporizador.iniciar(FPS);
		}
		
		setCentro();
		
		temporizador.actualizar();
		
		colision();
	}
	
	/**
	 * Método que dibuja la figura en la pantalla.
	 * 
	 * @param g - Objeto gráfico.
	 */
	public void dibujar(Graphics g) {
		g.drawImage(frames[indice], (int)posicion.getX(), (int)posicion.getY(), null);
	}
	
	/**
	 * Método que comprueba si una figura está colisionando con alguna otra figura.
	 */
	protected void colision() {
		
		ArrayList<FiguraMovil> figurasMoviles = EstadoJuego.getFigurasMoviles();
		
		for(int i = 0; i < figurasMoviles.size(); i++) {
			FiguraMovil figura = figurasMoviles.get(i);
			
			if(figura.equals(this)) {	//Evitar comprobar la colision con el objeto mismo.
				continue;
			}
			
			// Calculamos la distancia entre los 2 puntos centrales de las 2 Figuras.
			double distancia = figura.getCentro().getDistancia(this.centro);
		
			/* Si es menor que mas o menos la mitad del ancho de ambas figuras significa
			que estan en colision */
			if(distancia < (figura.ancho/2.5 + this.ancho/2.5) && figurasMoviles.contains(this) 
				&& figura.muerto == false && this.muerto == false) {
				colisionObjetos(figura, this);
			}
		}	
	}
	
	
	/**
	 * Maneja la lógica de colisión entre 2 Figuras.
	 * 
	 * @param a - Objeto FiguraMovil
	 * @param b - Objeto FiguraMovil
	 */
	private void colisionObjetos(FiguraMovil a, FiguraMovil b) {
		
		// Cuando el Jugador es golpeado, entra en un estado de inmortalidad.
		if((a instanceof Jugador &&  ((Jugador)a).getInmortal() == true) || (b instanceof Jugador && ((Jugador) b).getInmortal() == true)) {
			return;
		}
		
		// Evitamos que el Jugador pueda golpearse a si mismo.
		if((a instanceof Disparo && b instanceof Jugador) || (a instanceof Jugador && b instanceof Disparo)) {
			if((a instanceof Disparo && ((Disparo) a).esDisparoNave()) || (b instanceof Disparo && ((Disparo) b).esDisparoNave())) {
				return;
			}
		}
		
		// Si un Disparo impacta un Alien, nos encontramos varios casos:
		if((a instanceof Disparo && b instanceof Alien) || (a instanceof Alien && b instanceof Disparo)) {
			// Evitamos que los disparos de los Aliens impacten en otros Aliens.
			if((a instanceof Disparo && !((Disparo) a).esDisparoNave()) || (b instanceof Disparo && !((Disparo) b).esDisparoNave())) {
				return;
			}
			
			// Si el Alien es un Fantasma, y está activado el modo Fantasma evadirá el disparo.
			if((a instanceof Fantasma && ((Fantasma) a).getFantasma()) || (b instanceof Fantasma && ((Fantasma) b).getFantasma())) {
				return;
			}
			
			// Los fantasmas evadirán uno de cada tres tiros.
			if((a instanceof Fantasma && ((Fantasma) a).getEvasionFantasma()) || (b instanceof Fantasma && ((Fantasma) b).getEvasionFantasma())) {
				return;
			}
			if(a instanceof Fantasma || b instanceof Fantasma) {
				int evadir = random.nextInt(2);
				if(evadir == 1) {
					if(a instanceof Fantasma) {
						((Fantasma) a).activarFantasma();
					}
					else {
						((Fantasma) b).activarFantasma();
					}
					return;
				}
			}
			
			if(a instanceof Slime && ((Slime) a).getAcorazado() || b instanceof Slime && ((Slime) b).getAcorazado()) {
				if(a instanceof Disparo) {
					((Disparo) a).reflejarDisparo();
					((Disparo) a).setVelocidad(Slime.getVelocidadReflejo());
					EstadoJuego.addReflejo(b.getPosicion());
				}
				else {
					((Disparo) b).reflejarDisparo();
					((Disparo) b).setVelocidad(Slime.getVelocidadReflejo());
					EstadoJuego.addReflejo(a.getPosicion());
				}
				return;
			}
		}
		
		// Evitamos que los disparos de los Aliens le den a los Ojos.
		if((a instanceof Disparo && b instanceof Ojos) || (a instanceof Ojos && b instanceof Disparo)) {
			if((a instanceof Disparo && !((Disparo) a).esDisparoNave()) || (b instanceof Disparo && !((Disparo) b).esDisparoNave())) {
				return;
			}
		}
		
		// Evitamos que los disparos impacten entre sí.
		if(a instanceof Disparo && b instanceof Disparo) {
			/*
			if((!((Disparo) a).esDisparoNave()) && b instanceof DisparoCalabaza) {
				return;
			}
			else if((!((Disparo) b).esDisparoNave()) && a instanceof DisparoCalabaza) {
				return;
			}
			else if(((Disparo) a).esDisparoNave() && !(b instanceof DisparoCalabaza)) {
				return;
			}
			else if(((Disparo) b).esDisparoNave() && !(a instanceof DisparoCalabaza)) {
				return;
			}
			else if(((Disparo) b).esDisparoNave() && !(a instanceof DisparoCalabaza)) {
				return;
			}
			*/
			return;
		}
		
		if((a instanceof Puas && b instanceof Alien) || (a instanceof Alien && b instanceof Puas)) {
			return;
		}

		// Evitamos que los Aliens impacten entre sí.
		if(a instanceof Alien && b instanceof Alien) {
			return;
		}
		
		// Evitamos que las Puas impacten entre sí.
		if(a instanceof Puas && b instanceof Puas) {
			return;
		}
		
		// En caso de impacto añadimos una explosión.
		EstadoJuego.addExplosion(a.getCentro());
		a.kill();
		b.kill();
	}
	
	/**
	 * Método para marcar la Figura como muerta.
	 */
	protected void kill() {
		muerto = true;
	}
}
