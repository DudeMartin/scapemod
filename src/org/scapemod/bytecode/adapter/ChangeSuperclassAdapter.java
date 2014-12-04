package org.scapemod.bytecode.adapter;

import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents a change superclass adapter. This adapter changes the superclass
 * of a class.
 * 
 * @author Martin Tuskevicius
 */
public class ChangeSuperclassAdapter extends ClassVisitor {

    private final String superclassName;
    private String oldSuperclassName;
    
    /**
     * Creates a new change class adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param superclassName
     *            the internal name of the new superclass.
     */
    public ChangeSuperclassAdapter(ClassVisitor cv, String superclassName) {
	super(Opcodes.ASM4, cv);
	this.superclassName = superclassName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
	oldSuperclassName = superName;
	cv.visit(version, access, name, signature, superclassName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
	if (name.equals("<init>")) {
	    return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {
		
		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		    if(opcode == Opcodes.INVOKESPECIAL && owner.equals(oldSuperclassName)) {
			owner = superclassName;
		    }
		    mv.visitMethodInsn(opcode, owner, name, desc);
		}
	    };
	}
	return cv.visitMethod(access, name, desc, signature, exceptions);
    }
}
