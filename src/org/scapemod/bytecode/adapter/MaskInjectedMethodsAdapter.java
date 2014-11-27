package org.scapemod.bytecode.adapter;

import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents a mask injected methods adapter. This adapter hides injected
 * getter methods.
 * 
 * @author Martin Tuskevicius
 */
public class MaskInjectedMethodsAdapter extends ClassVisitor {

    /**
     * Creates a new mask injected methods adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     */
    public MaskInjectedMethodsAdapter(ClassVisitor cv) {
	super(Opcodes.ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access,
	    String name,
	    String desc,
	    String signature,
	    String[] exceptions) {
	return new ReturnFilteredMethodsAdapter(super.visitMethod(access, name, desc, signature, exceptions));
    }

    private class ReturnFilteredMethodsAdapter extends MethodVisitor {

	public ReturnFilteredMethodsAdapter(MethodVisitor mv) {
	    super(Opcodes.ASM4, mv);
	}

	@Override
	public void visitMethodInsn(int opcode,
		String owner,
		String name,
		String desc) {
	    if (name.equals("getDeclaredMethods")) {
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/scapemod/util/ClassUtilities", "filterInjectedMethods", "(Ljava/lang/Class;)[Ljava/lang/reflect/Method;");
	    } else {
		super.visitMethodInsn(opcode, owner, name, desc);
	    }
	}
    }
}
