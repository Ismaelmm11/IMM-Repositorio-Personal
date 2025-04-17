package es.poo.entrada;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class Raton implements MouseInputListener {

	// Coordenadas X e Y del ratón.
	public static int x, y;
	
	public static boolean CIR; //Click izquierdo del ratón

	
	/**
     * Método llamado cuando se pulsa un botón del ratón.
     * Verifica si se presiona el botón izquierdo del ratón y establece CIR a true.
     *
     * @param e - Evento del ratón.
     */
    @Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			CIR = true;
		}
		
	}

	
    /**
     * Método llamado cuando se libera un botón del ratón.
     * Verifica si se libera el botón izquierdo del ratón y establece CIR a false.
     *
     * @param e - Evento del ratón.
     */
    @Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			CIR = false;
		}
	}

    
    /**
     * Método llamado cuando se arrastra el ratón(mover el ratón cuando se le está dando click).
     * Actualiza las coordenadas actuales del ratón.
     *
     * @param e Evento del ratón.
     */
	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	
	/**
     * Método llamado cuando se mueve el ratón.
     * Actualiza las coordenadas actuales del ratón.
     *
     * @param e - Evento del ratón.
     */
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	
	//Métodos que están vacíos porque no nos interesan, pero estamos obligados a implementar al usar la interfaz MouseInputListener.
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
