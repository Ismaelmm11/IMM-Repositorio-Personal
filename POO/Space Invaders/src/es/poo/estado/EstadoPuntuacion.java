package es.poo.estado;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import es.poo.grafico.Recursos;
import es.poo.grafico.Texto;
import es.poo.interfaz.Accion;
import es.poo.interfaz.Boton;
import es.poo.math.Vector2D;
import es.poo.tablas.Puntuacion;

public class EstadoPuntuacion extends Estado {
	
	private static final int X_VOLVER = 125;
	private static final int Y_VOLVER = 575;
	
	private static final int X_CENTRO = 600;
	private static final int Y_FILA = 80;

	private int numLista;
	
	private Boton btnMenu;
	private Boton btnAnterior;
	private Boton btnSiguiente;
	
	private Texto txtTitulo, txtNormal;
	
	private ArrayList<Puntuacion> tablaPuntuacionFacil;
	private ArrayList<Puntuacion> tablaPuntuacionMedio;
	private ArrayList<Puntuacion> tablaPuntuacionDificil;
	
	public EstadoPuntuacion() {
		
		numLista = 1;
		
		btnMenu = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_VOLVER, Y_VOLVER),
				"MENÚ",
				true,
				new Accion() {
					@Override
					public void hacerAccion() {
						Estado.cambiarEstado(new EstadoMenu());
					}
		});
		
		btnAnterior = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_VOLVER + 350, Y_VOLVER),
				"ANTERIOR",
				false,
				new Accion() {
					@Override
					public void hacerAccion() {
						pulsarBtnAnterior();
					}
		});
		
		btnSiguiente = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_VOLVER + 700, Y_VOLVER),
				"SIGUIENTE",
				true,
				new Accion() {
					@Override
					public void hacerAccion() {
						pulsarBtnSiguiente();
					}
		});
		
		txtTitulo = new Texto(Color.YELLOW, Recursos.fuenteM);
		txtNormal = new Texto(Color.WHITE, Recursos.fuenteP);
		
		tablaPuntuacionFacil = Puntuacion.getTablaPuntuacionFacil();
		tablaPuntuacionMedio = Puntuacion.getTablaPuntuacionMedio();
		tablaPuntuacionDificil = Puntuacion.getTablaPuntuacionDificil();
	}

	@Override
	public void actualizar() {
		btnMenu.actualizar();
		btnAnterior.actualizar();
		btnSiguiente.actualizar();
	}

	@Override
	public void dibujar(Graphics g) {
		btnMenu.dibujar(g);
		btnAnterior.dibujar(g);
		btnSiguiente.dibujar(g);
		
		if(numLista == 1) {
			txtTitulo.dibujarTxt(g, new Vector2D(X_CENTRO, Y_FILA), "NIVEL FÁCIL");
			imprimirLista(g, tablaPuntuacionFacil);
		}
		else if(numLista == 2) {
			txtTitulo.dibujarTxt(g, new Vector2D(X_CENTRO, Y_FILA), "NIVEL MEDIO");
			imprimirLista(g, tablaPuntuacionMedio);
		}
		else {
			txtTitulo.dibujarTxt(g, new Vector2D(X_CENTRO, Y_FILA), "NIVEL DIFÍCIL");
			imprimirLista(g, tablaPuntuacionDificil);
		}
		
	}
	
	private void imprimirLista(Graphics g, ArrayList<Puntuacion> tablaPuntuacion) {
		
		Vector2D posicionIzq = new Vector2D(X_CENTRO - 300, Y_FILA + 60);
		Vector2D posicionCen = new Vector2D(X_CENTRO, Y_FILA + 60);
		Vector2D posicionDer = new Vector2D(X_CENTRO + 300, Y_FILA + 60);
		
		txtTitulo.dibujarTxt(g, posicionIzq, "PUNTUACION");
		txtTitulo.dibujarTxt(g, posicionCen, "NOMBRE");
		txtTitulo.dibujarTxt(g, posicionDer, "FECHA");
	
		posicionIzq = new Vector2D(posicionIzq.getX(), posicionIzq.getY()+40);
		posicionCen = new Vector2D(posicionCen.getX(), posicionCen.getY()+40);
		posicionDer = new Vector2D(posicionDer.getX(), posicionDer.getY()+40);
		
		for(int i = tablaPuntuacion.size() - 1; i >= 0; i--) {
			
			Puntuacion puntuacion = tablaPuntuacion.get(i);
			
			txtNormal.dibujarTxt(g, posicionIzq, Integer.toString(puntuacion.getPuntos()));
			txtNormal.dibujarTxt(g, posicionCen, puntuacion.getNombre());
			txtNormal.dibujarTxt(g, posicionDer, puntuacion.getFecha());
			
			posicionIzq = new Vector2D(posicionIzq.getX(), posicionIzq.getY()+40);
			posicionCen = new Vector2D(posicionCen.getX(), posicionCen.getY()+40);
			posicionDer = new Vector2D(posicionDer.getX(), posicionDer.getY()+40);	
		}
	}
	
	private void pulsarBtnSiguiente() {
		if(numLista == 1) {
			btnAnterior.setVisible(true);
		}
		else if(numLista == 2) {
			btnSiguiente.setVisible(false);
		}
		numLista++;
	}
	
	private void pulsarBtnAnterior() {
		if(numLista == 2) {
			btnAnterior.setVisible(false);
		}
		else if(numLista == 3) {
			btnSiguiente.setVisible(true);
		}
		numLista--;
	}
}
