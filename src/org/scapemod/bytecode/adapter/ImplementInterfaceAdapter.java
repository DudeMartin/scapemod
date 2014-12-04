package org.scapemod.bytecode.adapter;

import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents an implement interface adapter. This adapter appends a specified
 * interface to a class declaration.
 * 
 * @author Martin Tuskevicius
 */
public class ImplementInterfaceAdapter extends ClassVisitor {

    private final String interfaceName;

    /**
     * Creates a new implement interface adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param interfaceName
     *            the internal name of the interface class.
     */
    public ImplementInterfaceAdapter(ClassVisitor cv, String interfaceName) {
	super(Opcodes.ASM4, cv);
	this.interfaceName = interfaceName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
	String[] postImplement;
	if (interfaces == null || interfaces.length == 0) {
	    postImplement = new String[] { interfaceName };
	} else {
	    postImplement = new String[interfaces.length + 1];
	    System.arraycopy(interfaces, 0, postImplement, 0, interfaces.length);
	    postImplement[interfaces.length] = interfaceName;
	}
	cv.visit(version, access, name, signature, superName, postImplement);
    }
}
