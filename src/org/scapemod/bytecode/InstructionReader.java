package org.scapemod.bytecode;

import static org.scapemod.util.BufferUtilities.getString;
import static org.scapemod.util.BufferUtilities.getUnsignedByte;
import static org.scapemod.util.BufferUtilities.getUnsignedShort;

import java.nio.ByteBuffer;

import org.scapemod.bytecode.asm.Label;
import org.scapemod.bytecode.asm.MethodVisitor;

/**
 * Represents an instruction reader.
 * 
 * @author Martin Tuskevicius
 */
public class InstructionReader {

    private static final int LABEL = 0;
    private static final int INSTRUCTION = 1;
    private static final int FIELD_INSTRUCTION = 2;
    private static final int METHOD_INSTRUCTION = 3;
    private static final int TYPE_INSTRUCTION = 4;
    private static final int JUMP_INSTRUCTION = 5;
    private static final int INT_INSTRUCTION = 6;
    private static final int INT_INCREMENT_INSTRUCTION = 7;
    private static final int VARIABLE_INSTRUCTION = 8;
    private static final int LDC_INSTRUCTION = 9;
    private static final int MULTI_A_NEW_ARRAY_INSTRUCTION = 10;

    private static final int LDC_INT = 0;
    private static final int LDC_LONG = 1;
    private static final int LDC_FLOAT = 2;
    private static final int LDC_DOUBLE = 3;
    private static final int LDC_STRING = 4;

    private final ByteBuffer data;

    /**
     * Creates a new instruction reader.
     * 
     * @param data
     *            the instruction data.
     */
    public InstructionReader(ByteBuffer data) {
	this.data = data;
    }

    /**
     * Reads and inserts instructions.
     * 
     * @param mv
     *            the method visitor.
     */
    public void insertInstructions(MethodVisitor mv) {
	int instructionCount = getUnsignedShort(data);
	Label[] labels = new Label[getUnsignedByte(data)];
	for (int i = 0; i < labels.length; i++) {
	    labels[i] = new Label();
	}
	while (instructionCount-- > 0) {
	    switch (data.get()) {
	    case LABEL: {
		mv.visitLabel(labels[getUnsignedByte(data)]);
		break;
	    }
	    case INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		mv.visitInsn(opcode);
		break;
	    }
	    case FIELD_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		String owner = getString(data);
		String name = getString(data);
		String descriptor = getString(data);
		mv.visitFieldInsn(opcode, owner, name, descriptor);
		break;
	    }
	    case METHOD_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		String owner = getString(data);
		String name = getString(data);
		String descriptor = getString(data);
		mv.visitMethodInsn(opcode, owner, name, descriptor);
		break;
	    }
	    case TYPE_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		String type = getString(data);
		mv.visitTypeInsn(opcode, type);
		break;
	    }
	    case JUMP_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		Label label = labels[getUnsignedByte(data)];
		mv.visitJumpInsn(opcode, label);
		break;
	    }
	    case INT_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		int operand = data.getShort();
		mv.visitIntInsn(opcode, operand);
		break;
	    }
	    case INT_INCREMENT_INSTRUCTION: {
		int variable = getUnsignedByte(data);
		int increment = data.get();
		mv.visitIincInsn(variable, increment);
		break;
	    }
	    case VARIABLE_INSTRUCTION: {
		int opcode = getUnsignedByte(data);
		int variable = getUnsignedByte(data);
		mv.visitVarInsn(opcode, variable);
		break;
	    }
	    case LDC_INSTRUCTION: {
		Object constant;
		switch (data.get()) {
		case LDC_INT:
		    constant = data.getInt();
		    break;
		case LDC_LONG:
		    constant = data.getLong();
		    break;
		case LDC_FLOAT:
		    constant = data.getFloat();
		    break;
		case LDC_DOUBLE:
		    constant = data.getDouble();
		    break;
		case LDC_STRING:
		    constant = getString(data);
		    break;
		default:
		    throw new RuntimeException();
		}
		mv.visitLdcInsn(constant);
		break;
	    }
	    case MULTI_A_NEW_ARRAY_INSTRUCTION: {
		String descriptor = getString(data);
		int dimensions = getUnsignedByte(data);
		mv.visitMultiANewArrayInsn(descriptor, dimensions);
		break;
	    }
	    default:
		throw new RuntimeException();
	    }
	}
    }
}