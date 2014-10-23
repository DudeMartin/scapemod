package org.scapemod.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of web-related utility methods.
 * 
 * @author Martin Tuskevicius
 */
public final class WebUtilities {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("<param name=\"(.*?)\" value=\"(.*?)\">");

    /**
     * Prevents external instantiation.
     */
    private WebUtilities() {

    }

    /**
     * Reads the contents of the resource at the other end of the connection.
     * 
     * @param connection
     *            the connection.
     * @return the contents.
     * @throws IOException
     *             if a communication error occurred.
     */
    public static byte[] readContents(URLConnection connection) throws IOException {
	final int length = connection.getContentLength();
	final InputStream in = connection.getInputStream();
	try {
	    if (length == -1) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int bytesRead = 0; (bytesRead = in.read(buffer)) >= 0;) {
		    if (bytesRead > 0) {
			data.write(buffer, 0, bytesRead);
		    }
		}
		return data.toByteArray();
	    } else {
		byte[] data = new byte[length];
		for (int position = 0, remaining = length; remaining > 0;) {
		    int bytesRead = in.read(data, position, remaining);
		    position += bytesRead;
		    remaining -= bytesRead;
		}
		return data;
	    }
	} finally {
	    in.close();
	}
    }

    /**
     * Reads the contents of the resource at the specified address.
     * 
     * @param url
     *            the address.
     * @return the contents.
     * @throws IOException
     *             if a communication error occurred.
     */
    public static byte[] readContents(URL url) throws IOException {
	return readContents(url.openConnection());
    }

    /**
     * Reads the contents of the resource at the specified textual address.
     * 
     * @param address
     *            the address.
     * @return the contents.
     * @throws IOException
     *             if a communication error occurred.
     */
    public static byte[] readContents(String address) throws IOException {
	return readContents(new URL(address));
    }

    /**
     * Parses parameters from a given page source and returns them in a map.
     * Parameters are matched only if they are in the format:
     * 
     * <p>
     * {@code <param name="parameter name" value="value">}
     * 
     * @param source
     *            the page source from which to parse the parameters.
     * @param ignore
     *            the parameters to ignore. If a parameter name equals one of
     *            these strings, then it will be ignored (i.e., it will not be
     *            included in the resulting map).
     * @return a <code>HashMap</code>, where the key in the map is the parameter
     *         name and the corresponding value is the value of the parameter.
     */
    public static HashMap<String, String> parseParameters(String source, String... ignore) {
	List<String> ignoreList = Arrays.asList(ignore);
	HashMap<String, String> parameters = new HashMap<String, String>();
	Matcher matcher = PARAMETER_PATTERN.matcher(source);
	while (matcher.find()) {
	    String parameter = matcher.group(1);
	    String value = matcher.group(2);
	    if (!ignoreList.contains(parameter)) {
		parameters.put(parameter, value);
	    }
	}
	return parameters;
    }
}