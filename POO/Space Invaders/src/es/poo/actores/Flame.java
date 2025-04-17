package es.poo.actores;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

public class Flame extends Alien {

	private static final int CAD_METRALLETA = 250;

	private static final int DUR_METRALLETA = 1250;

	private static final int[] CAD_DISPARO = {2000, 3000, 4000, 5000};

	private static final BufferedImage[] flame = Recursos.flame;

	private int cadencia;

	private int indiceCamino;

	private boolean metralleta;

	private ArrayList<Vector2D> camino = null;

	private Cronometro disparo;

	private Cronometro crMetralleta;

	public Flame(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion) {
		super(posicion, imagen, vida, puntuacion);

		setFrames(flame);

		metralleta = false;

		cadencia = setCadenciaAleatoria();
	}

	public void actualizar() {

		super.actualizar();

		if(crMetralleta.getActivo() == false) {
			metralleta = false;
		}

		if(disparo.getActivo() == false) {
			if(metralleta == true) {
				disparo.iniciar(CAD_METRALLETA);
			}
			else {
				disparo.iniciar(cadencia);
			}
			disparar();
		}

		if(checkCamino()) {
			movimientoFlame();
		}
		

		actualizarCronometro();
	}

	private void activarMetralleta() {

		int numAleatorio = random.nextInt(9);

		if(numAleatorio == 0 && crMetralleta.getActivo() == false) {
			metralleta = true;
			crMetralleta.iniciar(DUR_METRALLETA);
			disparo.iniciar(CAD_METRALLETA);
		}
	}

	private void disparar() {
		Disparo disparo = new Disparo(new Vector2D(posicion.getX() + ancho/4, posicion.getY() + alto/2),
				5,
				Recursos.disparoFuego[0],
				Recursos.disparoFuego,
				this);

		disparo.setFPS(125);

		EstadoJuego.addFiguraMovil(disparo);
	}

	private void movimientoFlame() {

		if(indiceCamino == 0) {

			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.subX(2.5);
			}
			else
			{
				activarMetralleta();
				indiceCamino++;
			}
		}
		else {
			if(posicion.getDistancia(camino.get(indiceCamino)) != 0) {
				posicion = posicion.addX(2.5);
			}
			else
			{
				activarMetralleta();
				indiceCamino--;
			}
		}
	}

	private void actualizarCronometro() {
		disparo.actualizar();
		crMetralleta.actualizar();
	}


	public void setCaminoDerecha() {

		camino = new ArrayList<Vector2D>();
		
		indiceCamino = 1;

		camino.add(new Vector2D(posicion.getX(), posicion.getY()));

		camino.add(new Vector2D(posicion.getX() + 200, posicion.getY()));
	}


	public void setCaminoIzquierda() {

		camino = new ArrayList<Vector2D>();
		
		indiceCamino = 0;

		camino.add(new Vector2D(posicion.getX() - 200, posicion.getY()));

		camino.add(new Vector2D(posicion.getX(), posicion.getY()));
	}

	private int setCadenciaAleatoria() {

		int numAleatorio = random.nextInt(4);

		return CAD_DISPARO[numAleatorio];
	}
	
	private boolean checkCamino() {
		if(camino == null) {
			return false;
		}
		return true;
	}

	protected void checkPosicion() throws IllegalArgumentException{

		super.checkPosicion();

		double ejeX = posicion.getX();

		if(ejeX < 200 || ejeX > 900) {
			throw new IllegalArgumentException("Para el correcto funcionamiento de la aplicación se recomienda"
					+	" colocar Flames\nen posiciones entre los 200 y 900 px en el eje X.");
		}
	}

	@Override
	public void iniciarCronometro() {
		disparo = new Cronometro();
		crMetralleta = new Cronometro();
		disparo.iniciar(cadencia);
	}
}
