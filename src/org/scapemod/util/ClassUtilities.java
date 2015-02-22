package org.scapemod.util;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.scapemod.bytecode.asm.ClassReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.FieldVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
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

    /**
     * Determines if a method has the <code>static</code> access modifier.
     * 
     * @param methodName
     *            the name of the method.
     * @param methodDescriptor
     *            the descriptor of the method.
     * @param ownerName
     *            the internal name of the class containing the method.
     * @param readers
     *            a mapping of class names to <code>ClassReader</code> objects.
     * @return <code>true</code> if the method has the <code>static</code>
     *         access modifier, <code>false</code> otherwise.
     */
    public static boolean isStatic(final String methodName,
	    final String methodDescriptor,
	    final String ownerName,
	    Map<String, ClassReader> readers) {
	final boolean[] isStatic = new boolean[1];
	readers.get(ownerName).accept(new ClassVisitor(Opcodes.ASM4, null) {

	    @Override
	    public MethodVisitor visitMethod(int access,
		    String name,
		    String desc,
		    String signature,
		    String[] exceptions) {
		if(name.equals(methodName) && desc.equals(methodDescriptor)) {
		    isStatic[0] = Modifier.isStatic(access);
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	    }
	}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
	return isStatic[0];
    }
}