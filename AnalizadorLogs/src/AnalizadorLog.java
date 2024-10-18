import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AnalizadorLog {

    private Map<String, Integer> contadorNiveles;
    private Map<String, Integer> contadorErrores;

    public AnalizadorLog() {
        contadorNiveles = new HashMap<>();
        contadorNiveles.put("INFO", 0);
        contadorNiveles.put("WARNING", 0);
        contadorNiveles.put("ERROR", 0);

        contadorErrores = new HashMap<>();
    }

    public void analizarLog(String logFilePath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(logFilePath))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                procesarLinea(linea);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de log: " + e.getMessage());
        }
    }

    private void procesarLinea(String linea) {
        if (linea.contains("INFO")) {
            contadorNiveles.put("INFO", contadorNiveles.get("INFO") + 1);
        } else if (linea.contains("WARNING")) {
            contadorNiveles.put("WARNING", contadorNiveles.get("WARNING") + 1);
        } else if (linea.contains("ERROR")) {
            contadorNiveles.put("ERROR", contadorNiveles.get("ERROR") + 1);
            // Extraer el mensaje de error y contar su ocurrencia
            String mensajeError = linea.split("ERROR")[1].trim();
            contadorErrores.put(mensajeError, contadorErrores.getOrDefault(mensajeError, 0) + 1);
        }
    }

    public void generarInforme(String outputPath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            writer.write("Informe de Logs\n");
            writer.write("================\n\n");

            // Escribir el conteo de los niveles de log
            writer.write("Conteo de niveles de log:\n");
            for (Map.Entry<String, Integer> entry : contadorNiveles.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }

            writer.write("\nErrores más comunes:\n");

            // Obtener los 5 errores más comunes
            contadorErrores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .forEach(entry -> {
                        try {
                            writer.write(entry.getKey() + " - Ocurrencias: " + entry.getValue() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        AnalizadorLog analizador = new AnalizadorLog();
        analizador.analizarLog("log.txt");
        analizador.generarInforme("informe_log.txt");
        System.out.println("Análisis completado. Informe generado en 'informe_log.txt'.");
    }
}
