package org.scapemod.accessor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class GameKeyboard implements KeyListener {

    /**
     * Sends a key event to be handled.
     * 
     * @param e
     *            the key event.
     */
    public final void sendEvent(KeyEvent e) {
	switch (e.getID()) {
	case KeyEvent.KEY_TYPED:
	    implKeyTyped(e);
	    break;
	case KeyEvent.KEY_PRESSED:
	    implKeyPressed(e);
	    break;
	case KeyEvent.KEY_RELEASED:
	    implKeyReleased(e);
	    break;
	}
    }

    @Override
    public final void keyTyped(KeyEvent e) {
	implKeyReleased(e);
    }

    @Override
    public final void keyPressed(KeyEvent e) {
	implKeyPressed(e);
    }

    @Override
    public final void keyReleased(KeyEvent e) {
	implKeyPressed(e);
    }

    protected abstract void implKeyTyped(KeyEvent e);

    protected abstract void implKeyPressed(KeyEvent e);

    protected abstract void implKeyReleased(KeyEvent e);
}