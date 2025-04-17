package es.poo.estado;

import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import es.poo.grafico.Animacion;
import es.poo.grafico.Recursos;
import es.poo.interfaz.Accion;
import es.poo.interfaz.Boton;
import es.poo.math.Vector2D;
import es.poo.tablas.Puntuacion;

public class EstadoMenu extends Estado {

	private static final int X_CENTRO = 465;
	private static final int[] FILA = {-75, 250, 325, 400, 475};
	
	private ArrayList<Boton> botones;
	
	private Animacion menuFondo;
	
	public EstadoMenu() {
		
		try {
			Puntuacion.leer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		menuFondo = new Animacion(Recursos.fondoMenu, 150, new Vector2D(0, -10), true);
		
		botones = new ArrayList<Boton>();
		
		Boton btnJugar = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[1]),
				"JUGAR",
				true,
				new Accion() {
						public void hacerAccion() {
							//Estado.cambiarEstado(new EstadoJuego());
							mostrarMenuNiveles();
						}
				});
		
		Boton btnPuntuacion = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[2]),
				"PUNTUACIÓN",
				true,
				new Accion() {
						public void hacerAccion() {
							Estado.cambiarEstado(new EstadoPuntuacion());
						}
				});
		
		Boton btnSalir = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[3]),
				"SALIR", 
				true,
				new Accion() {
						public void hacerAccion() {
							System.exit(0);
						}
				});
		
		Boton btnLvl1 = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[1]),
				"LVL. FACIL",
				false,
				new Accion() {
						public void hacerAccion() {
							EstadoJuego nivel1 = EstadoJuego.getEstadoJuego();
							nivel1.iniciarLvl1();
							Estado.cambiarEstado(nivel1);
						}
				});
		
		Boton btnLvl2 = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[2]),
				"LVL. MEH",
				false,
				new Accion() {
						public void hacerAccion() {
							EstadoJuego nivel2 = EstadoJuego.getEstadoJuego();
							nivel2.iniciarLvl2();
							Estado.cambiarEstado(nivel2);
						}
				});
		
		Boton btnLvl3 = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[3]),
				"LVL. DIABLO", 
				false,
				new Accion() {
						public void hacerAccion() {
							EstadoJuego nivel3 = EstadoJuego.getEstadoJuego();
							nivel3.iniciarLvl3();
							Estado.cambiarEstado(nivel3);
						}
				});
		
		Boton btnVolver = new Boton(Recursos.botonBlue, Recursos.botonGris,
				new Vector2D(X_CENTRO, FILA[4]), 
				"VOLVER", 
				false,
				new Accion() {
						public void hacerAccion() {
							mostrarMenuPrincipal();
						}
				});
		
		botones.add(btnJugar);
		botones.add(btnPuntuacion);
		botones.add(btnSalir);
		botones.add(btnLvl1);
		botones.add(btnLvl2);
		botones.add(btnLvl3);
		botones.add(btnVolver);
	}
	
	
	@Override
	public void actualizar() {
		for(int i = 0; i < botones.size(); i++) {
			Boton boton = botones.get(i);
			boton.actualizar();
		}
		
		menuFondo.actualizar();
	}

	@Override
	public void dibujar(Graphics g) {
		
		g.drawImage(menuFondo.getCurrentFrame(), 
				(int)menuFondo.getPosicion().getX(),
				(int)menuFondo.getPosicion().getY(), 
				null);
		
		g.drawImage(Recursos.logo, X_CENTRO - 70, FILA[0], null);
		
		for(int i = 0; i < botones.size(); i++) {
			Boton boton = botones.get(i);
			boton.dibujar(g);
		}
	}
	
	private void mostrarMenuPrincipal() {
		for(int i = 0; i < botones.size(); i++) {
			Boton boton = botones.get(i);
			if(i <= 2) {
				boton.setVisible(true);
			}
			else {
				boton.setVisible(false);
			}
		}
	}
	
	private void mostrarMenuNiveles() {
		for(int i = 0; i < botones.size(); i++) {
			Boton boton = botones.get(i);
			if(i <= 2) {
				boton.setVisible(false);
			}
			else {
				boton.setVisible(true);
			}
		}
	}

}
