package org.scapemod.bytecode.adapter;

import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;
import org.scapemod.bytecode.asm.Type;

/**
 * Represents an add caller adapter. This adapter adds a <code>public</code>
 * method that calls another method.
 * 
 * @author Martin Tuskevicius
 */
public class AddCallerAdapter extends ClassVisitor {

    private final String callerMethodName;
    private final String methodName;
    private final String methodDescriptor;
    private final String ownerName;
    private final int dummyArgument;
    private final boolean isStatic;

    /**
     * Creates a new add caller adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param callerMethodName
     *            the name of the caller method.
     * @param methodName
     *            the name of the callee method.
     * @param methodDescriptor
     *            the descriptor of the method.
     * @param ownerName
     *            the internal name of the class owning the callee method.
     * @param dummyArgument
     *            the dummy argument. A value of 0 is treated as if there is no
     *            dummy argument.
     * @param isStatic
     *            a flag denoting if the callee method is <code>static</code>.
     */
    public AddCallerAdapter(ClassVisitor cv,
	    String callerMethodName,
	    String methodName,
	    String methodDescriptor,
	    String ownerName,
	    int dummyArgument,
	    boolean isStatic) {
	super(Opcodes.ASM4, cv);
	this.callerMethodName = callerMethodName;
	this.methodName = methodName;
	this.methodDescriptor = methodDescriptor;
	this.ownerName = ownerName;
	this.dummyArgument = dummyArgument;
	this.isStatic = isStatic;
    }

    @Override
    public void visitEnd() {
	boolean hasDummy = dummyArgument != 0;
	String callerMethodDescriptor = hasDummy ? methodDescriptor.replace("I)", ")") : methodDescriptor;
	Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
	MethodVisitor methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC, callerMethodName, callerMethodDescriptor, null, null);
	methodVisitor.visitCode();
	methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
	for (int i = 0; i < argumentTypes.length - (hasDummy ? 1 : 0); i++) {
	    methodVisitor.visitVarInsn(argumentTypes[i].getOpcode(Opcodes.ILOAD), i + 1);
	}
	if (hasDummy) {
	    methodVisitor.visitLdcInsn(dummyArgument);
	}
	methodVisitor.visitMethodInsn(isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, ownerName, methodName, methodDescriptor);
	methodVisitor.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
	methodVisitor.visitMaxs(0, 0);
	methodVisitor.visitEnd();
	cv.visitEnd();
    }
}