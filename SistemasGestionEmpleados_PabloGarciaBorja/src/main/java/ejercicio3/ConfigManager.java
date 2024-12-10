package ejercicio3;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private Properties properties;

    public ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error cargando archivo de configuración: " + e.getMessage());
            // Si el archivo no existe, se puede crear uno nuevo con valores por defecto.
            setProperty("fichero_binario", "empleados.dat");
            setProperty("fichero_xml", "empleados.xml");
            setProperty("fichero_json", "empleados.json");
            setProperty("id_empleado", "0");
            saveProperties(); // Guardamos la nueva configuración
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Archivo de configuración actualizado");
        } catch (IOException e) {
            System.err.println("Error guardando archivo de configuración: " + e.getMessage());
        }
    }

    public int getIdEmpleado() {
        return Integer.parseInt(properties.getProperty("id_empleado", "0"));
    }

    public void incrementIdEmpleado() {
        int currentId = getIdEmpleado();
        setProperty("id_empleado", String.valueOf(currentId + 1));
    }
}
