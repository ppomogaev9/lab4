package factory.configReader;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;


import static factory.params.Params.*;

public class ConfigReader {
    private String logField = "LogSale";
    private InputStream configStream;
    static Map<String, Integer> values = new HashMap<>();
    public ConfigReader() {
        values.put("BodyStorageSize", 0);
        values.put("MotorStorageSize", 0);
        values.put("AccessoryStorageSize", 0);
        values.put("AutoStorageSize", 0);
        values.put("Workers", 0);
        values.put("Dealers", 0);
        values.put("Suppliers", 0);

        try {
            Class<?> configClass = Class.forName("factory.configReader.ConfigReader");
            configStream = configClass.getClassLoader().getResourceAsStream("config.txt");
        }
        catch (Exception e) {
            log.info("some exception in configReader constructor");
        }
    }
    public void readConfig() {
        Scanner scan = null;
        scan = new Scanner(configStream);
        String key;

        while (scan.hasNextLine()) {
            scan.useDelimiter("=");
            key = scan.next();
            scan.skip("=");
            scan.useDelimiter("\r\n");

            if (key.equals(logField)){
                if (scan.hasNextBoolean()){
                    logSale = scan.nextBoolean();
                }
                else {
                    throw new IllegalArgumentException("wrong value: " + scan.next());
                }
            }
            else {
                if (values.containsKey(key)) {
                    if (scan.hasNextInt()) {
                        values.put(key, scan.nextInt());
                    }
                    else {
                        throw new IllegalArgumentException("wrong value: " + scan.next());
                    }
                }
                else {
                    throw new IllegalArgumentException("wrong parameter: " + scan.next());
                }

                scan.skip("\r\n");
            }
        }

        bodyStorageSize = values.get("BodyStorageSize");
        motorStorageSize = values.get("MotorStorageSize");
        accessoryStorageSize = values.get("AccessoryStorageSize");
        autoStorageSize = values.get("AutoStorageSize");
        workersNum = values.get("Workers");
        dealersNum = values.get("Dealers");
        suppliersNum = values.get("Suppliers");
    }
}
