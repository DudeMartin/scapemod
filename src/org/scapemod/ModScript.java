package org.scapemod;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.scapemod.bytecode.InstructionReader;
import org.scapemod.bytecode.adapter.AddCallerAdapter;
import org.scapemod.bytecode.adapter.AddGetterAdapter;
import org.scapemod.bytecode.adapter.AddMethodAdapter;
import org.scapemod.bytecode.adapter.ChangeSuperclassAdapter;
import org.scapemod.bytecode.adapter.ImplementInterfaceAdapter;
import org.scapemod.bytecode.adapter.InsertInstructionsAdapter;
import org.scapemod.bytecode.asm.ClassReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.ClassWriter;
import org.scapemod.util.BufferUtilities;
import org.scapemod.util.ClassUtilities;

/**
 * Represents a mod script.
 * 
 * @author Martin Tuskevicius
 */
public final class ModScript {

    public static final int ADD_GETTER = 0;
    public static final int ADD_CALLER = 1;
    public static final int ADD_METHOD = 2;
    public static final int INSERT_INSTRUCTIONS = 3;
    public static final int IMPLEMENT_INTERFACE = 4;
    public static final int CHANGE_SUPERCLASS = 5;

    private final ByteBuffer data;

    /**
     * Creates a new mod script.
     * 
     * @param data
     *            the script data.
     */
    public ModScript(ByteBuffer data) {
	this.data = data;
    }

    /**
     * Executes this mod script.
     * 
     * @param readers
     *            a mapping of class names to <code>ClassReader</code> objects.
     * @return a mapping of class names to modified class data (in the form of
     *         <code>byte</code> arrays).
     */
    public synchronized Map<String, byte[]> mod(Map<String, ClassReader> readers) {
	data.rewind();
	Map<String, byte[]> modifiedClassData = new HashMap<String, byte[]>();
	short remainingClassCount = data.getShort();
	while (modifiedClassData.size() < readers.size()) {
	    for (Entry<String, ClassReader> readerEntry : readers.entrySet()) {
		if (modifiedClassData.containsKey(readerEntry.getKey())) {
		    continue;
		}
		String className = readerEntry.getKey();
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassVisitor lastVisitor = classWriter;
		if (remainingClassCount-- > 0) {
		    className = BufferUtilities.getString(data);
		    byte modificationCount = data.get();
		    while (modificationCount-- > 0) {
			switch (data.get()) {
			case ADD_GETTER: {
			    String fieldName = BufferUtilities.getString(data);
			    String fieldDescriptor = BufferUtilities.getString(data);
			    String getterName = BufferUtilities.getString(data);
			    String getterDescriptor = BufferUtilities.getString(data);
			    String ownerName = BufferUtilities.getString(data);
			    int multiplier = data.getInt();
			    boolean isStatic = ClassUtilities.isStatic(fieldName, ownerName, readers);
			    lastVisitor = new AddGetterAdapter(lastVisitor, fieldName, fieldDescriptor, getterName, getterDescriptor, ownerName, multiplier, isStatic);
			    break;
			}
			case ADD_CALLER: {
			    String callerMethodName = BufferUtilities.getString(data);
			    String methodName = BufferUtilities.getString(data);
			    String methodDescriptor = BufferUtilities.getString(data);
			    String ownerName = BufferUtilities.getString(data);
			    int dummyArgument = data.getInt();
			    boolean isStatic = ClassUtilities.isStatic(methodName, methodDescriptor, ownerName, readers);
			    lastVisitor = new AddCallerAdapter(lastVisitor, callerMethodName, methodName, methodDescriptor, ownerName, dummyArgument, isStatic);
			    break;
			}
			case ADD_METHOD: {
			    int methodAccess = data.getInt();
			    String methodName = BufferUtilities.getString(data);
			    String methodDescriptor = BufferUtilities.getString(data);
			    InstructionReader instructions = new InstructionReader(BufferUtilities.getBytes(data, data.getInt()));
			    lastVisitor = new AddMethodAdapter(lastVisitor, methodAccess, methodName, methodDescriptor, instructions);
			    break;
			}
			case INSERT_INSTRUCTIONS: {
			    String methodName = BufferUtilities.getString(data);
			    String methodDescriptor = BufferUtilities.getString(data);
			    Map<Integer, InstructionReader> instructionsMap = createInstructionMap();
			    lastVisitor = new InsertInstructionsAdapter(lastVisitor, methodName, methodDescriptor, instructionsMap);
			    break;
			}
			case IMPLEMENT_INTERFACE: {
			    lastVisitor = new ImplementInterfaceAdapter(lastVisitor, BufferUtilities.getString(data));
			    break;
			}
			case CHANGE_SUPERCLASS: {
			    lastVisitor = new ChangeSuperclassAdapter(lastVisitor, ModScriptConfiguration.getSuperclass(BufferUtilities.getString(data)));
			    break;
			}
			}
		    }
		}
		ClassReader classReader = (readerEntry.getKey() == className) ? readerEntry.getValue() : readers.get(className);
		classReader.accept(lastVisitor, ClassReader.SKIP_FRAMES);
		modifiedClassData.put(className, classWriter.toByteArray());
	    }
	}
	return modifiedClassData;
    }
    
    /**
     * Reads instruction positions and instruction data and constructs a sorted
     * and offsetted instruction map.
     * 
     * @return the instruction map.
     */
    private Map<Integer, InstructionReader> createInstructionMap() {
	SortedMap<Integer, InstructionReader> sortedInstructionMap = new TreeMap<Integer, InstructionReader>();
	for (int count = data.get(); count > 0; count--) {
	    int position = BufferUtilities.getUnsignedShort(data);
	    InstructionReader instructions = new InstructionReader(BufferUtilities.getBytes(data, data.getInt()));
	    sortedInstructionMap.put(position, instructions);
	}
	Map<Integer, InstructionReader> offsettedMap = new HashMap<Integer, InstructionReader>();
	int offset = 0;
	for (Entry<Integer, InstructionReader> entry : sortedInstructionMap.entrySet()) {
	    InstructionReader instructions = entry.getValue();
	    offsettedMap.put(entry.getKey() + offset, instructions);
	    offset += instructions.getInstructionCount();
	}
	return offsettedMap;
    }
}