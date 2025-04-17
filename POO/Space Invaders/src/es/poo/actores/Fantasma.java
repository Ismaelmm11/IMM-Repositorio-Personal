package es.poo.actores;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import es.poo.grafico.Recursos;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

public class Fantasma extends Alien {

	private BufferedImage[] frames1;
	private BufferedImage[] frames2;
	private BufferedImage[] frames3;
	
	private ArrayList<Vector2D> camino;
	
	private int indiceCamino;
	private int indiceAntCamino;
	
	private boolean fantasma;
	private boolean evasionFantasma;
	
	private int debug;
	
	Cronometro tiempoFantasma;

	public Fantasma(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion)
			throws IllegalArgumentException {
		super(posicion, imagen, vida, puntuacion);
		
		fantasma = false;
		evasionFantasma = false;
		
		frames1 = Recursos.fantasma;
		frames2 = Recursos.fantasmaDespierto;
		frames3 = Recursos.fantasmaTransparente;
		
		setFrames(frames1);
		
		debug = (int)posicion.getY();
		
		camino = crearCaminoFantasma();
		
		indiceCamino = 0;
	}
	
	public void actualizar() {
		
		super.actualizar();
		
		if(tiempoFantasma.getActivo() == false) {
			if(fantasma == false) {
				setFrames(frames1);
			}
			evasionFantasma = false;
		}
		
		
		//System.out.println(posicion.getDistancia(camino.get(indiceCamino)));

		
		movimientoFantasmal();
		
		tiempoFantasma.actualizar();
	}
	/*
	public void dibujar(Graphics g) {
		
		super.dibujar(g);
		
		
		 //Debug gráfico(muestra los puntos por donde pasa el fantasma):
		g.setColor(Color.red);
		
		g.drawRect(1000, debug, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
		g.drawRect(1100, debug + 100, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		g.drawRect(1100, 400, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
		g.drawRect(1000, 500, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		g.drawRect(900, 575, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
		g.drawRect(100, 500, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		g.drawRect(200, 575, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
		g.drawRect(0, debug + 100, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		g.drawRect(0, 400, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
		g.drawRect(100, debug, Recursos.flame[0].getWidth(), Recursos.flame[0].getHeight());
		
	}
	*/
	public boolean getFantasma() {
		return fantasma;
	}
	
	public void activarFantasma() {
		tiempoFantasma.iniciar(1000);
		setFrames(frames3);
		evasionFantasma = true;
	}
	
	public boolean getEvasionFantasma() {
		return evasionFantasma;
	}
	
	protected void kill() {
		if(fantasma == true) {
			return;
		}
		super.kill();
	}

	private ArrayList<Vector2D> crearCaminoFantasma(){
		
		ArrayList<Vector2D> caminoFantasma = new ArrayList<Vector2D>();
		
		// 2 puntos de reposo.
		caminoFantasma.add(new Vector2D(posicion.getX()+150, posicion.getY())); 
		caminoFantasma.add(new Vector2D(posicion.getX(), posicion.getY()));
		
		/*----------------- Puntos del Camino Fantasmal----------------------*/
		
		// Los 2 puntos iniciales del movimento. Están a la derecha.
		caminoFantasma.add(new Vector2D(1000, posicion.getY()));
		caminoFantasma.add(new Vector2D(1100, posicion.getY()+100));
		
		// Punto inferior-derecha, donde se bifurca el Camino Fantasmal.
		caminoFantasma.add(new Vector2D(1100, 400));
		
		// Son 2 puntos que trazan un recta para atacar al Jugador por arriba.
		caminoFantasma.add(new Vector2D(1000, 500));
		caminoFantasma.add(new Vector2D(100, 500));
		
		// Otros 2 puntos que trazan una recta para atacar al Jugador por abajo.
		caminoFantasma.add(new Vector2D(900, 575));
		caminoFantasma.add(new Vector2D(200, 575));
		
		// Punto donde se reunen las 2 bifurcaciones del Camino, 
		caminoFantasma.add(new Vector2D(0, 400));

		caminoFantasma.add(new Vector2D(0, posicion.getY()+100));
		caminoFantasma.add(new Vector2D(100, posicion.getY()));
		
		return caminoFantasma;
	}

	private void movimientoFantasmal() {
		int numAleatorio;

		switch(indiceCamino) {
		/* Movimiento a la derecha estándar. Al llegar al primer punto de descanso, aleatoriamente se
		 * podrá activar el Movimiento Fantasmal o puede volver al segundo punto de descanso (origen).*/
		case 0: 
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.addX(2.5);
			}
			else {
				numAleatorio = random.nextInt(5);
				if(numAleatorio != 0) {
					indiceCamino++;
				}
				else {
					setFrames(frames2);
					fantasma = true;
					indiceCamino = 2;
				}
			}
			break;
		// Movimiento a la izquierda estándar para volver al punto origen.
		case 1: 
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subX(2.5);
			}
			else {
				indiceCamino--;
			}
			break;
		/* El fantasma sale del bucle de los 2 puntos de descanso y empieza a realizar 
		 * el Movimiento Fantasmal. Se dirige a la derecha sin cambiar la velocidad*/
		case 2:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.addX(2.5);
			}
			else {
				indiceCamino++;
			}
			break;
		// Inicia una diagonal en direccion abajo-derecha aumentando la velocidad.
		case 3:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(5, 5));
			}
			else {
				indiceCamino++;
			}
			break;
		/* Comienza una veloz recta hacia abajo en direccion. Al llegar a su destino decide si
		 * tomar el camino que lo lleva a atacar al Jugador por la parte de arriba o el otro camino
		 * para atacarlo por abajo. */
		case 4:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.addY(12.5);
			}

			else {
				numAleatorio = random.nextInt(2);
				if(numAleatorio == 0) {
					indiceCamino++;
				}
				else {
					indiceCamino = 7;
				}
			}
			break;
		/* Realiza una diagonal hacia abajo-izquierda y se situa en la parte superior del área
		 * del Jugador.*/
		case 5:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(-10, 10));
				
			}
			else {
				indiceCamino++;
			}
			break;
		// Inicia el movimiento más rápido a la izquierda para cazar al Jugador.
		case 6:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subX(20);
			}
			else {
				indiceAntCamino = indiceCamino;
				indiceCamino = 9;
			}
			break;
		/* Realiza una diagonal hacia abajo-izquierda y se situa en la parte inferior del área
		* del Jugador.*/
		case 7:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(-10, 8.75));
			}
			else {
				indiceCamino++;
			}
			break;
		// Inicia el movimiento más rápido a la izquierda para cazar al Jugador.
		case 8:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subX(20);
			}
			
			else {
				indiceAntCamino = indiceCamino;
				indiceCamino = 9;
			}
			break;
		 /* Punto donde se reunen los 2 puntos Camino, dependiendo del movimiento anterior 
		  * se realizará la diagonal hacia arriba-izquierda de una manera u otra*/
		case 9:
			if(indiceAntCamino == 6 && posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(-10, -10));
			}
			else if(indiceAntCamino == 8 &&posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(-10, -8.75));
			}
			else {
				indiceCamino++;
			}
			break;
		// Hace una veloz recta hacia arriba.
		case 10:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subY(12.5);
			}
			else {
				indiceCamino++;
			}
			break;
		/* Finaliza el Movimiento Fantasmal con una diagonal hacia la arriba-derecha y hace
		*	que el Fantamsa vuelva al bucle de los puntos de descanso.*/
		case 11:
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.diagonal(new Vector2D(5, -5));
			}
			else {
				setFrames(frames1);
				fantasma = false;
				indiceCamino = 0;
			}
			break;
		}
	}
	
	protected void checkPosicion() throws IllegalArgumentException{
		
		super.checkPosicion();
		
		double ejeY = posicion.getY();
		double ejeX = posicion.getX();
		
		if(ejeY == 350) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación no se recomienda"
											+	" colocar Fantasmas\nen la 5º fila(350px).");
		}
		
		if(ejeX % 2.5 != 0) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
											+	" colocar Fantasmas\nen posiciones que sean multiplos de 2,5 en el eje X.");
		}
		
		if(ejeX < 250 || ejeX > 850) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" colocar Fantasmas\nen posiciones entre los 250 y 850 px en el eje X.");
		}
	}

	public void iniciarCronometro() {
		tiempoFantasma = new Cronometro();
	}
}
