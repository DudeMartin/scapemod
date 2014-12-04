package org.scapemod;

import java.awt.Canvas;

import org.scapemod.bytecode.asm.Type;

/**
 * Contains configuration variables for mod scripts.
 * 
 * @author Martin Tuskevicius
 */
public final class ModScriptConfiguration {

    /**
     * The format string for the address of a mod script.
     */
    public static String modScriptAddress = "";

    /**
     * The internal name of the custom <code>Canvas</code> class.
     */
    private static String customCanvasClassName = Type.getInternalName(Canvas.class);

    /**
     * Sets the internal name of the custom <code>Canvas</code> class.
     * 
     * @param customCanvasClass
     *            the class representation.
     */
    public static void setCustomCanvasClassName(Class<? extends Canvas> customCanvasClass) {
	customCanvasClassName = Type.getInternalName(customCanvasClass);
    }

    /**
     * Returns the internal name of the custom <code>Canvas</code> class.
     * 
     * <p>
     * Unless otherwise set, this returns the internal name of {@link Canvas}.
     * 
     * @return the custom name.
     */
    public static String getCustomCanvasClassName() {
	return customCanvasClassName;
    }

    /**
     * Returns the address of a mod script given a gamepack checksum.
     * 
     * @param gamepackChecksum
     *            the gamepack checksum.
     * @return the mod script address.
     */
    public static String getModScriptAddress(long gamepackChecksum) {
	return String.format(modScriptAddress, gamepackChecksum);
    }

    /**
     * Prevents external instantiation.
     */
    private ModScriptConfiguration() {

    }
}