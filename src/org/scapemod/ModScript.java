package org.scapemod;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.scapemod.bytecode.adapter.AddGetterAdapter;
import org.scapemod.bytecode.adapter.ChangeSuperclassAdapter;
import org.scapemod.bytecode.adapter.ImplementInterfaceAdapter;
import org.scapemod.bytecode.adapter.MaskInjectedMethodsAdapter;
import org.scapemod.bytecode.asm.ClassReader;
import org.scapemod.bytecode.asm.ClassVisitor;
import org.scapemod.bytecode.asm.ClassWriter;
import org.scapemod.util.BufferUtilities;

/**
 * Represents a mod script.
 * 
 * @author Martin Tuskevicius
 */
public class ModScript {

    protected final ByteBuffer data;

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
	boolean canvasModified = false;
	while (modifiedClassData.size() < readers.size()) {
	    for (Entry<String, ClassReader> readerEntry : readers.entrySet()) {
		if (modifiedClassData.containsKey(readerEntry.getKey())) {
		    continue;
		}
		String className = readerEntry.getKey();
		ClassWriter classWriter = new ClassWriter(0);
		ClassVisitor lastVisitor = new MaskInjectedMethodsAdapter(classWriter);
		if (remainingClassCount-- > 0) {
		    className = BufferUtilities.getString(data);
		    if (canvasModified) {
			byte getterCount = data.get();
			for (int i = 0; i < getterCount; i++) {
			    String fieldName = BufferUtilities.getString(data);
			    String fieldDescriptor = BufferUtilities.getString(data);
			    String getterName = BufferUtilities.getString(data);
			    String getterDescriptor = BufferUtilities.getString(data);
			    String ownerName = BufferUtilities.getString(data);
			    int multiplier = data.getInt();
			    boolean isStatic = (data.get() == 1);
			    lastVisitor = new AddGetterAdapter(lastVisitor, fieldName, fieldDescriptor, getterName, getterDescriptor, ownerName, multiplier, isStatic);
			}
			lastVisitor = new ImplementInterfaceAdapter(lastVisitor, BufferUtilities.getString(data));
		    } else {
			lastVisitor = new ChangeSuperclassAdapter(lastVisitor, ModScriptConfiguration.getCustomCanvasClassName());
			canvasModified = true;
		    }
		}
		ClassReader classReader = (readerEntry.getKey() == className) ? readerEntry.getValue() : readers.get(className);
		classReader.accept(lastVisitor, ClassReader.SKIP_FRAMES);
		modifiedClassData.put(className, classWriter.toByteArray());
	    }
	}
	return modifiedClassData;
    }
}