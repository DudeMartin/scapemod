package org.scapemod.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;

import org.scapemod.bytecode.asm.ClassReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.FieldVisitor;
import org.scapemod.bytecode.asm.Opcodes;

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

    /**
     * Determines if a field has the <code>static</code> access modifier.
     * 
     * @param fieldName
     *            the name of the field.
     * @param ownerName
     *            the internal name of the class containing the field.
     * @param readers
     *            a mapping of class names to <code>ClassReader</code> objects.
     * @return <code>true</code> if the field has the <code>static</code> access
     *         modifier, <code>false</code> otherwise.
     */
    public static boolean isStatic(String fieldName, String ownerName, Map<String, ClassReader> readers) {
	boolean[] isStatic = new boolean[1];
	readers.get(ownerName).accept(new ClassVisitor(Opcodes.ASM4, null) {
	    
	    @Override
	    public FieldVisitor visitField(int access,
		    String name,
		    String desc,
		    String signature,
		    Object value) {
		if (name.equals(fieldName)) {
		    isStatic[0] = Modifier.isStatic(access);
		}
		return super.visitField(access, name, desc, signature, value);
	    }
	}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
	return isStatic[0];
    }
}