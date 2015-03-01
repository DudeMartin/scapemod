package org.scapemod.accessor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class GameMouse implements MouseListener, MouseMotionListener {

    /**
     * The X-coordinate of the mouse's position.
     */
    protected int mouseX;

    /**
     * The Y-coordinate of the mouse's position.
     */
    protected int mouseY;

    /**
     * Returns the X-coordinate of the mouse's position.
     * 
     * @return the mouse X coordinate.
     */
    public final int getMouseX() {
	return mouseX;
    }

    /**
     * Returns the Y-coordinate of the mouse's position.
     * 
     * @return the mouse Y coordinate.
     */
    public final int getMouseY() {
	return mouseY;
    }

    /**
     * Sends a mouse event to be handled.
     * 
     * @param e
     *            the mouse event.
     */
    public final void sendEvent(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	switch (e.getID()) {
	case MouseEvent.MOUSE_DRAGGED:
	    implMouseDragged(e);
	    break;
	case MouseEvent.MOUSE_MOVED:
	    implMouseMoved(e);
	    break;
	case MouseEvent.MOUSE_CLICKED:
	    implMouseClicked(e);
	    break;
	case MouseEvent.MOUSE_PRESSED:
	    implMousePressed(e);
	    break;
	case MouseEvent.MOUSE_RELEASED:
	    implMouseReleased(e);
	    break;
	case MouseEvent.MOUSE_ENTERED:
	    implMouseEntered(e);
	    break;
	case MouseEvent.MOUSE_EXITED:
	    implMouseExited(e);
	    break;
	}
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseDragged(e);
	e.consume();
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseMoved(e);
	e.consume();
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseClicked(e);
	e.consume();
    }

    @Override
    public final void mousePressed(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMousePressed(e);
	e.consume();
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseReleased(e);
	e.consume();
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseEntered(e);
	e.consume();
    }

    @Override
    public final void mouseExited(MouseEvent e) {
	mouseX = e.getX();
	mouseY = e.getY();
	implMouseExited(e);
	e.consume();
    }

    protected abstract void implMouseDragged(MouseEvent e);

    protected abstract void implMouseMoved(MouseEvent e);

    protected abstract void implMouseClicked(MouseEvent e);

    protected abstract void implMousePressed(MouseEvent e);

    protected abstract void implMouseReleased(MouseEvent e);

    protected abstract void implMouseEntered(MouseEvent e);

    protected abstract void implMouseExited(MouseEvent e);
}