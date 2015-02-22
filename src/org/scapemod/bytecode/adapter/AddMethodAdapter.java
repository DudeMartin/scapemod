package org.scapemod.bytecode.adapter;

import java.lang.reflect.Modifier;

import org.scapemod.bytecode.InstructionReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents an add method adapter. This adapter adds a method to a class.
 * 
 * @author Martin Tuskevicius
 */
public class AddMethodAdapter extends ClassVisitor {

    private final int access;
    private final String name;
    private final String descriptor;
    private final InstructionReader instructions;
    
    /**
     * Creates a new add method adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param access
     *            the method's access flags.
     * @param name
     *            the method's name.
     * @param descriptor
     *            the method's descriptor.
     * @param instructions
     *            the method's instructions. May be <code>null</code> for
     *            <code>abstract</code> methods.
     */
    public AddMethodAdapter(ClassVisitor cv,
	    int access,
	    String name,
	    String descriptor,
	    InstructionReader instructions) {
	super(Opcodes.ASM4, cv);
	this.access = access;
	this.name = name;
	this.descriptor = descriptor;
	this.instructions = instructions;
    }

    @Override
    public void visitEnd() {
	MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, null, null);
	if (!Modifier.isAbstract(access)) {
	    methodVisitor.visitCode();
	    instructions.insertInstructions(methodVisitor);
	}
	methodVisitor.visitMaxs(0, 0);
	methodVisitor.visitEnd();
	cv.visitEnd();
    }
}