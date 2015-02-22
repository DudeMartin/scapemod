package org.scapemod.bytecode;

import static org.scapemod.util.BufferUtilities.getString;
import static org.scapemod.util.BufferUtilities.getUnsignedByte;
import static org.scapemod.util.BufferUtilities.getUnsignedShort;

import java.nio.ByteBuffer;

import org.scapemod.bytecode.asm.MethodVisitor;

/**
 * Represents an instruction reader.
 * 
 * @author Martin Tuskevicius
 */
public final class InstructionReader {

    public static final int INSTRUCTION = 0;
    public static final int FIELD_INSTRUCTION = 1;
    public static final int METHOD_INSTRUCTION = 2;
    public static final int TYPE_INSTRUCTION = 3;
    public static final int INT_INSTRUCTION = 4;
    public static final int INT_INCREMENT_INSTRUCTION = 5;
    public static final int VARIABLE_INSTRUCTION = 6;
    public static final int LDC_INSTRUCTION = 7;
    public static final int MULTI_A_NEW_ARRAY_INSTRUCTION = 8;

    public static final int LDC_INT = 0;
    public static final int LDC_LONG = 1;
    public static final int LDC_FLOAT = 2;
    public static final int LDC_DOUBLE = 3;
    public static final int LDC_STRING = 4;

    private final ByteBuffer data;
    private final int instructionCount;
    
    /**
     * Creates a new instruction reader.
     * 
     * @param data
     *            the instruction data.
     */
    public InstructionReader(ByteBuffer data) {
	this.data = data;
	this.instructionCount = getUnsignedShort(data);
	data.mark();
    }

    /**
     * Returns the number of instructions that will be inserted.
     * 
     * @return the instruction count.
     */
    public int getInstructionCount() {
	return instructionCount;
    }

    /**
     * Reads and inserts instructions.
     * 
     * @param mv
     *            the method visitor.
     */
    public void insertInstructions(MethodVisitor mv) {
	data.reset();
	int instructionCount = this.instructionCount;
	while (instructionCount-- > 0) {
	    switch (data.get()) {
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