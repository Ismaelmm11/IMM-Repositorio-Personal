package es.poo.actores;

import java.awt.image.BufferedImage;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Vector2D;

public class Puas extends FiguraMovil{
	
	private Vector2D puntoOg;
	private int direccionPua;
	
	private static final BufferedImage[][] framesPuas = {
			Recursos.puas1,
			Recursos.puas2,
			Recursos.puas3,
			Recursos.puas4,
			Recursos.puas5,
			Recursos.puas6
	};

	private Puas(Vector2D posicion, int direccionPua, BufferedImage imagen, BufferedImage[] frames) {
		super(posicion, imagen);
		
		this.direccionPua = direccionPua;
		
		setFrames(frames);
		
		puntoOg = new Vector2D(posicion.getX(), posicion.getY());
	}
	
	public void actualizar() {
		super.actualizar();
		
		double distancia = posicion.getDistancia(puntoOg);

		if(direccionPua == 0) {
			posicion = posicion.diagonal(new Vector2D(-3, 3));
		}
		else if(direccionPua == 1) {
			posicion = posicion.subX(4.5);
		}
		else if(direccionPua == 2) {
			posicion = posicion.diagonal(new Vector2D(-3, -3));
		}
		else if(direccionPua == 3) {
			posicion = posicion.diagonal(new Vector2D(3, -3));
		}
		else if(direccionPua == 4) {
			posicion = posicion.addX(4.5);
		}
		else {
			posicion = posicion.diagonal(new Vector2D(3, 3));
		}
		
		if(distancia > 125) {
			EstadoJuego.getFigurasMoviles().remove(this);
		}
	}
	
	public static void estrellaPuas(Vector2D posicion) {
		
		for(int i = 0; i < framesPuas.length; i++) {
			FiguraMovil pua = new Puas(new Vector2D(posicion.getX(), posicion.getY()),
					i,
					framesPuas[i][0],
					framesPuas[i]);
			EstadoJuego.addFiguraMovil(pua);
		}
	}

}
