package org.scapemod;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.Map;

/**
 * Represents a game applet stub.
 * 
 * @author Martin Tuskevicius
 */
public class GameAppletStub implements AppletStub {

    private final URL base;
    private final Map<String, String> parameters;
    private boolean active;

    /**
     * Creates a new game applet stub.
     * 
     * @param base
     *            the base address of the game applet.
     * @param parameters
     *            the applet parameter map.
     */
    public GameAppletStub(URL base, Map<String, String> parameters) {
	this.base = base;
	this.parameters = parameters;
    }

    /**
     * Sets the <code>active</code> flag.
     * 
     * @param active
     *            if the applet is active.
     */
    public void setActive(boolean active) {
	this.active = active;
    }

    @Override
    public boolean isActive() {
	return active;
    }

    @Override
    public URL getDocumentBase() {
	return base;
    }

    @Override
    public URL getCodeBase() {
	return base;
    }

    @Override
    public String getParameter(String name) {
	return parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
	return null;
    }

    @Override
    public void appletResize(int width, int height) {

    }
}