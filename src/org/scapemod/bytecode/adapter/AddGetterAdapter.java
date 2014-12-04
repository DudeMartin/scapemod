package org.scapemod.bytecode.adapter;

import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents an add getter adapter. This adapter adds a <code>public</code>
 * getter method for a specified field.
 * 
 * @author Martin Tuskevicius
 */
public class AddGetterAdapter extends ClassVisitor {

    private final String fieldName;
    private final String fieldDescriptor;
    private final String getterName;
    private final String getterDescriptor;
    private final String ownerName;
    private final int multiplier;
    private final boolean isStatic;
    private final int returnInstruction;
    
    /**
     * Creates a new add getter adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param fieldName
     *            the name of the field.
     * @param fieldDescriptor
     *            the descriptor of the field.
     * @param getterName
     *            the name of the getter method (that will be added).
     * @param getterDescriptor
     *            the descriptor of the getter method (that will be added).
     * @param ownerName
     *            the internal name of the class owning the field.
     * @param multiplier
     *            the value multiplier. A value of 1 is treated as if there is
     *            no multiplier.
     * @param isStatic
     *            a flag denoting if the field for which the getter is being
     *            created is <code>static</code>.
     */
    public AddGetterAdapter(ClassVisitor cv,
	    String fieldName,
	    String fieldDescriptor,
	    String getterName,
	    String getterDescriptor,
	    String ownerName,
	    int multiplier,
	    boolean isStatic) {
	super(Opcodes.ASM4, cv);
	this.fieldName = fieldName;
	this.fieldDescriptor = fieldDescriptor;
	this.getterName = getterName;
	this.getterDescriptor = getterDescriptor;
	this.ownerName = ownerName;
	this.multiplier = multiplier;
	this.isStatic = isStatic;
	this.returnInstruction = determineReturnInstruction();
    }
    
    @Override
    public void visitEnd() {
	MethodVisitor methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC, getterName, "()" + getterDescriptor, null, null);
	methodVisitor.visitCode();
	if (!isStatic) {
	    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
	}
	methodVisitor.visitFieldInsn(isStatic ? Opcodes.GETSTATIC : Opcodes.GETFIELD, ownerName, fieldName, fieldDescriptor);
	int maxStack = (returnInstruction == Opcodes.LRETURN) || (returnInstruction == Opcodes.DRETURN) ? 2 : 1;
	if (multiplier != 1) {
	    methodVisitor.visitLdcInsn(multiplier);
	    methodVisitor.visitInsn(Opcodes.IMUL);
	    maxStack++;
	}
	methodVisitor.visitInsn(returnInstruction);
	methodVisitor.visitMaxs(maxStack, 1);
	methodVisitor.visitEnd();
	cv.visitEnd();
    }

    private int determineReturnInstruction() {
	if (fieldDescriptor.length() > 1) {
	    return Opcodes.ARETURN;
	}
	switch (fieldDescriptor.charAt(0)) {
	case 'J':
	    return Opcodes.LRETURN;
	case 'F':
	    return Opcodes.FRETURN;
	case 'D':
	    return Opcodes.DRETURN;
	default:
	    return Opcodes.IRETURN;
	}
    }
}
