package org.scapemod.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A collection of class-related utility methods.
 * 
 * @author Martin Tuskevicius
 */
public final class ClassUtilities {

    /**
     * Prevents external instantiation.
     */
    private ClassUtilities() {

    }

    /**
     * Returns the declared methods in the provided class, <em>excluding</em>
     * the methods whose names start with one of the provided prefixes.
     * 
     * @param clazz
     *            the class representation.
     * @param prefixes
     *            the name prefixes.
     * @return the filtered methods.
     */
    public static Method[] filterMethods(Class<?> clazz, String... prefixes) {
	Method[] declared = clazz.getDeclaredMethods();
	ArrayList<Method> filtered = new ArrayList<Method>(declared.length);
	methodLoop: for (Method m : declared) {
	    for (String prefix : prefixes) {
		if (m.getName().startsWith(prefix)) {
		    continue methodLoop;
		}
	    }
	    filtered.add(m);
	}
	int filteredCount = filtered.size();
	return (filteredCount == declared.length) ? declared : filtered.toArray(new Method[filteredCount]);
    }

    /**
     * Filters out injected methods.
     * 
     * @param clazz
     *            the class representation.
     * @return the filtered methods.
     */
    public static Method[] filterInjectedMethods(Class<?> clazz) {
	return filterMethods(clazz, "get", "is");
    }
}
