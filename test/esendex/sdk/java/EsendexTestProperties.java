package esendex.sdk.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EsendexTestProperties {

    public enum Key {
        USERNAME("tests.username"),
        PASSWORD("tests.password"),
        ACCOUNT("tests.account"),
        DESTINATION_NUMBER("tests.destination_number");

        String value;
        Key(String v) {
            value = v;
        }
    }

    private static final EsendexTestProperties instance = new EsendexTestProperties();
    private static final String PROPERTY_FILE_NAME = "esendex_test.properties";

    private Properties properties;

    public EsendexTestProperties() {
        properties = new Properties();
        try {
            InputStream is = getClass().getResourceAsStream("/" + PROPERTY_FILE_NAME);
            if (is == null) throw new FileNotFoundException();
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not load '" + PROPERTY_FILE_NAME + "' is it at the root of the classpath?");
        }
    }

    public static EsendexTestProperties instance() {
        return instance;
    }

    public String getProperty(Key key) {
        String prop = properties.getProperty(key.value);
        if (prop == null) throw new NullPointerException("Key: '" + key
                + "' could not be found, this key is mandatory");
        return prop;
    }

}
