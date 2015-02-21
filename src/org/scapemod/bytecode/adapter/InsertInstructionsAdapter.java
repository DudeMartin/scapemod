package org.scapemod.bytecode.adapter;

import java.util.Map;

import org.scapemod.bytecode.InstructionReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.Handle;
import org.scapemod.bytecode.asm.Label;
import org.scapemod.bytecode.asm.MethodVisitor;
import org.scapemod.bytecode.asm.Opcodes;

/**
 * Represents an insert instructions adapter. This adapter inserts instructions
 * at specified positions in a specified method.
 * 
 * @author Martin Tuskevicius
 */
public class InsertInstructionsAdapter extends ClassVisitor {

    private final String methodName;
    private final String methodDescriptor;
    private final Map<Integer, InstructionReader> instructionMap;
    
    /**
     * Creates a new insert instructions adapter.
     * 
     * @param cv
     *            the delegate <code>ClassVisitor</code>.
     * @param methodName
     *            the name of the method to insert instructions in.
     * @param methodDescriptor
     *            the descriptor of the method to insert instructions in.
     * @param instructionMap
     *            the instruction map.
     */
    public InsertInstructionsAdapter(ClassVisitor cv,
	    String methodName,
	    String methodDescriptor,
	    Map<Integer, InstructionReader> instructionMap) {
	super(Opcodes.ASM4, cv);
	this.methodName = methodName;
	this.methodDescriptor = methodDescriptor;
	this.instructionMap = instructionMap;
    }

    @Override
    public MethodVisitor visitMethod(int access,
	    String name,
	    String desc,
	    String signature,
	    String[] exceptions) {
	MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
	if (name.equals(methodName) && desc.equals(methodDescriptor)) {
	    mv = new InstructionInserter(mv);
	}
	return mv;
    }

    private class InstructionInserter extends MethodVisitor {

	private int instructionPosition = 0;
	
	public InstructionInserter(MethodVisitor mv) {
	    super(Opcodes.ASM4, mv);
	}

	@Override
	public void visitInsn(int opcode) {
	    checkInsertInstructions();
	    super.visitInsn(opcode);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
	    checkInsertInstructions();
	    super.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
	    checkInsertInstructions();
	    super.visitVarInsn(opcode, var);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
	    checkInsertInstructions();
	    super.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
	    checkInsertInstructions();
	    super.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
	    checkInsertInstructions();
	    super.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
	    checkInsertInstructions();
	    super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
	    checkInsertInstructions();
	    super.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitLdcInsn(Object cst) {
	    checkInsertInstructions();
	    super.visitLdcInsn(cst);
	}

	@Override
	public void visitIincInsn(int var, int increment) {
	    checkInsertInstructions();
	    super.visitIincInsn(var, increment);
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
	    checkInsertInstructions();
	    super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
	    checkInsertInstructions();
	    super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
	    checkInsertInstructions();
	    super.visitMultiANewArrayInsn(desc, dims);
	}
	
	private void checkInsertInstructions() {
	    InstructionReader instructions = instructionMap.get(instructionPosition++);
	    if (instructions != null) {
		instructions.insertInstructions(this);
	    }
	}
    }
}