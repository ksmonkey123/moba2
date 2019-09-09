package ch.awae.moba2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Collection of utility methods
 *
 * @author Andreas Wälchli
 */
public final class Utils {

    /**
     * Reads a properties file
     *
     * @param file the file to read
     * @return the properties
     * @throws IllegalArgumentException if the {@code file} could not be located
     * @throws IOException              if an error occurred while reading the file
     */
    public static Properties getProperties(String file) throws IOException {
        try (InputStream in = Utils.class.getClassLoader()
                .getResourceAsStream("resources/" + file)) {
            if (in == null)
                throw new IllegalArgumentException("file not found: resources/" + file);

            Properties p = new Properties();
            p.load(in);
            return p;
        }
    }

    /**
     * Parses a String into a signed integer.
     * <p>
     * The following formats are supported:
     * <ul>
     * <li>{@code 0b00011011} represents a binary number</li>
     * <li>{@code 0x0123af6b} represents a hexadecimal number</li>
     * <li>any decimal format supported by {@link Integer#parseInt(String)}</li>
     * </ul>
     *
     * @param number the number to parse. may not be null
     * @return the signed integer value of the given String
     * @throws NullPointerException  the {@code number} is null
     * @throws NumberFormatException the {@code number} cannot be parsed into an integer. See
     *                               {@link Integer#parseInt(String, int)} for more information
     * @see Integer#parseInt(String, int)
     */
    public static int parseInt(String number) {
        Objects.requireNonNull(number, "the number may not be null");
        if (number.startsWith("0b"))
            return Integer.parseInt(number.substring(2), 2);
        else if (number.startsWith("0x"))
            return Integer.parseInt(number.substring(2), 16);
        else
            return Integer.parseInt(number, 10);
    }

}
