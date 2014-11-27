package org.scapemod;

import java.util.Map;

/**
 * A <code>ClassLoader</code> implementation that defines classes before loading
 * them for the first time using a provided class definitions map.
 * 
 * @author Martin Tuskevicius
 */
public final class DefinableClassLoader extends ClassLoader {

    private final Map<String, byte[]> definitions;

    /**
     * Creates a new definable class loader.
     * 
     * @param definitions
     *            the class definitions map.
     */
    public DefinableClassLoader(Map<String, byte[]> definitions) {
	this.definitions = definitions;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
	byte[] classBytes = definitions.remove(name);
	if (classBytes != null) {
	    return defineClass(name, classBytes, 0, classBytes.length);
	}
	return super.findClass(name);
    }
}