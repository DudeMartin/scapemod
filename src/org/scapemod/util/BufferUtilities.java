package org.scapemod.util;

import java.nio.ByteBuffer;

/**
 * A collection of buffer-related utility methods.
 * 
 * @author Martin Tuskevicius
 */
public final class BufferUtilities {

    /**
     * Prevents external instantiation.
     */
    private BufferUtilities() {

    }

    /**
     * Reads the specified number of bytes from the provided byte buffer and
     * returns a new <code>ByteBuffer</code> containing the read bytes.
     * 
     * @param buf
     *            the buffer to read the bytes from.
     * @param number
     *            the number of bytes to read.
     * @return a new buffer containing the bytes.
     */
    public static ByteBuffer getBytes(ByteBuffer buf, int number) {
	byte[] bytes = new byte[number];
	buf.get(bytes);
	return ByteBuffer.wrap(bytes);
    }

    /**
     * Reads a null-terminated (<code>'\0'</code>) string from the provided byte
     * buffer.
     * 
     * @param buf
     *            the buffer to read the string from.
     * @return the string.
     */
    public static String getString(ByteBuffer buf) {
	StringBuilder builder = new StringBuilder();
	for (char c; (c = (char) buf.get()) != '\0';) {
	    builder.append(c);
	}
	return builder.toString();
    }

    /**
     * Reads a <code>byte</code> from the buffer and returns its unsigned value.
     * 
     * @param buf
     *            the buffer to read from.
     * @return the unsigned value.
     */
    public static int getUnsignedByte(ByteBuffer buf) {
	return buf.get() & 0xFF;
    }

    /**
     * Reads a <code>short</code> from the buffer and returns its unsigned
     * value.
     * 
     * @param buf
     *            the buffer to read from.
     * @return the unsigned value.
     */
    public static int getUnsignedShort(ByteBuffer buf) {
	return buf.getShort() & 0xFFFF;
    }
}