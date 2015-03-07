package org.scapemod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private static volatile String modScriptAddress = "";

    /**
     * Contains mappings of internal superclass names to refactored class names.
     */
    private static Map<String, String> superclassMap = Collections.synchronizedMap(new HashMap<String, String>());
    
    /**
     * Prevents external instantiation.
     */
    private ModScriptConfiguration() {

    }

    /**
     * Sets the mod script address <em>format string</em>.
     * 
     * @param modScriptAddress
     *            the format string.
     * @throws IllegalArgumentException
     *             if the string does not contain a <code>"%d"</code>.
     */
    public static void setModScriptAddress(String modScriptAddress) {
	if (!modScriptAddress.contains("%d")) {
	    throw new IllegalArgumentException("Not a proper format string.");
	}
	ModScriptConfiguration.modScriptAddress = modScriptAddress;
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
     * Associates a superclass with a refactored class name.
     * 
     * @param refactoredName
     *            the refactored class name.
     * @param superclass
     *            the superclass.
     */
    public static void setSuperclass(String refactoredName, Class<?> superclass) {
	superclassMap.put(refactoredName, Type.getInternalName(superclass));
    }

    /**
     * Associates a superclass with a refactored class name.
     * 
     * @param accessor
     *            the accessor class.
     * @param superclass
     *            the superclass class.
     */
    public static void setSuperclass(Class<?> accessor, Class<?> superclass) {
	setSuperclass(accessor.getSimpleName(), superclass);
    }

    /**
     * Returns the internal name of the superclass associated with the provided
     * refactored class name.
     * 
     * @param refactoredName
     *            the refactored class name.
     * @return the internal superclass name.
     */
    public static String getSuperclass(String refactoredName) {
	return superclassMap.get(refactoredName);
    }
}