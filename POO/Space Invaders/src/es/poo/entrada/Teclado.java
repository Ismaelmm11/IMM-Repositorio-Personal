package es.poo.entrada;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener{

	private boolean[] teclas = new boolean[256];
	
	public static boolean UP, IZQ, DER, DOWN, SHOOT;
	
	public Teclado() {
		UP = false;
		IZQ = false;
		DER = false;
		DOWN = false;
		SHOOT = false;
	}
	
	public void actualizar() {
		UP = teclas[KeyEvent.VK_UP];
		IZQ = teclas[KeyEvent.VK_LEFT];
		DER = teclas[KeyEvent.VK_RIGHT];
		DOWN = teclas[KeyEvent.VK_DOWN];
		SHOOT = teclas[KeyEvent.VK_SPACE];
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode()); Imprime el valor de la tecla presionada.
		teclas[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		teclas[e.getKeyCode()] = false;
		
	}
	
	// Método vacío pues no nos interesa, pero hay que implementar al usar la interfaz KeyListener.
	@Override
	public void keyTyped(KeyEvent e) {}
}
