package org.scapemod.util;

import java.lang.reflect.Modifier;
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
    public static boolean isStatic(final String fieldName, final String ownerName, Map<String, ClassReader> readers) {
	final boolean[] isStatic = new boolean[1];
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