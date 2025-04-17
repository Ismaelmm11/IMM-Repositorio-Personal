package es.poo.actores;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

public class Pulpo extends Alien{

	private static final BufferedImage[] FRAMES_PULPO = Recursos.pulpo; 

	private static final int[] CADENCIAS = {3000, 5500, 9500};
	
	private static final double VEL_MOVIMIENTO = 2.5;

	private ArrayList<Vector2D> camino = null;

	private int indiceCamino;
	
	private Cronometro cronoDisparo;

	public Pulpo(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion) throws IllegalArgumentException {
		super(posicion, imagen, vida, puntuacion);

		
		setFrames(FRAMES_PULPO);
	}


	public void actualizar() {

		super.actualizar();

		if(cronoDisparo.getActivo() == false) {
			disparar();
		}

		if(checkCamino()) {
			movimientoPulpo();
		}

		cronoDisparo.actualizar();
	}

	private void movimientoPulpo() {

		if(indiceCamino == 0) {

			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subX(VEL_MOVIMIENTO);
			}
			else
			{
				indiceCamino++;
			}
		}
		else {
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.addX(VEL_MOVIMIENTO);
			}
			else
			{
				indiceCamino--;
			}
		}
	}

	private void disparar() {

		Disparo disparo = new Disparo(new Vector2D(posicion.getX()+ancho/4, posicion.getY()+alto/2),
				7.5,
				Recursos.disparoEvil[0],
				Recursos.disparoEvil,
				this);

		EstadoJuego.addFiguraMovil(disparo);

		cronoDisparo.iniciar(getCadenciaAleatoria());
	}

	public void setCaminoDerecha(int distancia) {

		camino = new ArrayList<Vector2D>();

		checkDistanciaDer(distancia);
		
		indiceCamino = 1;

		camino.add(new Vector2D(posicion.getX(), posicion.getY()));

		camino.add(new Vector2D(posicion.getX() + distancia, posicion.getY()));

	}

	public void setCaminoIzquierda(int distancia) {

		camino = new ArrayList<Vector2D>();

		checkDistanciaIzq(distancia);
		
		indiceCamino = 0;

		camino.add(new Vector2D(posicion.getX() - distancia, posicion.getY()));

		camino.add(new Vector2D(posicion.getX(), posicion.getY()));

	}
	
	private void checkDistanciaIzq(int distancia) {
		
		checkDistancia(distancia);

		if((posicion.getX() - distancia) < 0) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" que los Pulpos\n usen distancias que no hagan que se salgan de la pantalla.");
		}
	}
	
	private void checkDistanciaDer(int distancia) {
		checkDistancia(distancia);

		if((posicion.getX() + distancia) > 1100) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" que los Pulpos\n usen distancias que no hagan que se salgan de la pantalla.");
		}
	}
	
	private void checkDistancia(int distancia) {
		
		if(distancia%50 != 0) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" que los Pulpos\nse muevan distancias que sean múltiplos de 50.");
		}
		
		if(distancia < 150) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" que los Pulpos\nse muevan distancias que superiores o iguales a los 150 px.");
		}
		
		if(distancia > 1000) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" que los Pulpos\nse muevan distancias que inferiores o iguales a los 1000 px.");
		}
	}

	private boolean checkCamino() {
		if(camino == null) {
			return false;
		}
		return true;
	}

	private int getCadenciaAleatoria() {
		return CADENCIAS[random.nextInt(3)];
	}
	/*
	protected void checkPosicion() throws IllegalArgumentException{

		super.checkPosicion();

		double ejeX = posicion.getX();

		if(ejeX < 250 || ejeX > 900) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" colocar Flames\nen posiciones entre los 200 y 900 px en el eje X.");
		}
	}
	*/


	@Override
	public void iniciarCronometro() {
		cronoDisparo = new Cronometro();
		cronoDisparo.iniciar(getCadenciaAleatoria());
		
	}
}