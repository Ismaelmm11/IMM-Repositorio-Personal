package es.poo.actores;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

public class Calabaza extends Alien{

	private static final double VEL_DISPARO = 2.5;
	private static final BufferedImage[] frames1 = Recursos.calabaza;;
	private static final BufferedImage[] frames2 = Recursos.calabazaActiva;;

	private ArrayList<Vector2D> camino;

	private int indiceCamino;

	private boolean disparar;

	private Cronometro tiempoDisparo;

	public Calabaza(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion) throws IllegalArgumentException {
		super(posicion, imagen, vida, puntuacion);

		setCaminoDerecha();
		
		setFrames(frames1);

		disparar = false;

		indiceCamino = 0;
	}

	public void actualizar() {

		super.actualizar();

		dispararCalabaza();

		movimientoCalabaza();

		tiempoDisparo.actualizar();
	}


	private void dispararCalabaza() {
		if(tiempoDisparo.getActivo() == false && disparar == true) {
			disparar = false;

			FiguraMovil disparo = new DisparoCalabaza(new Vector2D(getCentro().getX() - 16, getCentro().getY()),
					VEL_DISPARO,
					Recursos.disparoCalabaza[0],
					Recursos.disparoCalabaza,
					this);

			EstadoJuego.addFiguraMovil(disparo);

			setFrames(frames1);
		}
	}

	private void movimientoCalabaza() {

		int numAleatorio;

		if(tiempoDisparo.getActivo() == false) {
			if(indiceCamino == 0) {
				if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
					posicion = posicion.subX(3);
				}
				else {
					numAleatorio = random.nextInt(10);
					if(numAleatorio == 0) {
						tiempoDisparo.iniciar(2000);
						disparar = true;
						setFrames(frames2);
					}
					indiceCamino++;
				}
			}
			else if (indiceCamino == 1){
				if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
					posicion = posicion.addX(3);
				}
				else {
					numAleatorio = random.nextInt(10);
					if(numAleatorio == 0) {
						tiempoDisparo.iniciar(2000);
						disparar = true;
						setFrames(frames2);
					}
					indiceCamino--;
				}
			}
		}
	}
	
	public void setCaminoDerecha() {
		
		camino = new ArrayList<Vector2D>();
		
		camino.add(new Vector2D(posicion.getX(), posicion.getY()));
		
		camino.add(new Vector2D(posicion.getX() + 270, posicion.getY()));
		
	}
	
	public void setCaminoIzquierda() {
		
		camino = new ArrayList<Vector2D>();
		
		camino.add(new Vector2D(posicion.getX() - 270, posicion.getY()));
		
		camino.add(new Vector2D(posicion.getX(), posicion.getY()));
	}

	protected void checkPosicion() throws IllegalArgumentException {

		super.checkPosicion();
		
		double ejeX = posicion.getX();

		if(ejeX < 100 || ejeX > 966) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación, se recomienda que"
					+	"el eje X de las \nCalabazas se encuentre entre los 400 y 700 px.");
		}	
	}

	public void iniciarCronometro() {
		tiempoDisparo = new Cronometro();
	}
}
