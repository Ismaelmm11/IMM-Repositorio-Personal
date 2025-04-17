package es.poo.estado;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import es.poo.grafico.Multimedia;
import es.poo.grafico.Recursos;
import es.poo.grafico.Texto;
import es.poo.math.Vector2D;

public class EstadoCarga extends Estado{

	private Thread hiloCarga;
	
	private static final int CENTRO_X = 600;
	private static final int CENTRO_Y = 350; 
	
	private static final int ANCHO_BARRA = 500;
	private static final int ALTO_BARRA = 50;
	
	private Font fuente;
	
	private Texto texto;
	
	public EstadoCarga(Thread hiloCarga) {
		this.hiloCarga = hiloCarga;
		this.hiloCarga.start();
		
		fuente = Multimedia.CargarFuente("/fuente/Pixel NES.otf", 42);
		
		texto = new Texto(Color.WHITE, fuente, new Vector2D(CENTRO_X, CENTRO_Y-50), "CARGANDO...");
	}
	
	
	@Override
	public void actualizar() {
		if(Recursos.carga == true) {
			Estado.cambiarEstado(new EstadoMenu());
			try {
				hiloCarga.join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	//500 50
	
	@Override
	public void dibujar(Graphics g) {
		GradientPaint gp = new GradientPaint(
				 CENTRO_X - ANCHO_BARRA/2,
				 CENTRO_Y - ALTO_BARRA/2,
				 Color.WHITE,
				 CENTRO_X + ANCHO_BARRA/2,
				 CENTRO_Y + ALTO_BARRA/2,
				 Color.BLUE);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(gp);
		
		float porcentaje = Recursos.conteoCarga / Recursos.NUM_RECURSOS;
	
		g2d.fillRect(CENTRO_X - ANCHO_BARRA/2,
				 CENTRO_Y - ALTO_BARRA/2,
				 (int)(ANCHO_BARRA * porcentaje),
				 ALTO_BARRA);
	
		g2d.drawRect(CENTRO_X - ANCHO_BARRA/2,
				 CENTRO_Y - ALTO_BARRA/2,
				 ANCHO_BARRA,
				 ALTO_BARRA);
		
		texto.dibujarTxt(g);
		
	}
}
