// ManejadorArchivos.java
package com.dam.accesodata;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivos {
    private static final String ARCHIVO = "notas_estudiantes.txt";

    // Añadir un estudiante al archivo
    public void añadirEstudiante(Estudiante estudiante) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            writer.write(estudiante.toString());
            writer.newLine();
            System.out.println("Estudiante añadido correctamente.");
        } catch (IOException e) {
            System.out.println("Error al añadir el estudiante: " + e.getMessage());
        }
    }

    // Mostrar todos los estudiantes del archivo
    public void mostrarEstudiantes() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            System.out.println("--- Lista de Estudiantes ---");
            for (Estudiante estudiante : estudiantes) {
                System.out.println(estudiante);
            }
        }
    }

    // Buscar un estudiante por su nombre
    public void buscarEstudiante(String nombre) {
        List<Estudiante> estudiantes = leerEstudiantes();
        boolean encontrado = false;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Estudiante encontrado: " + estudiante);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("Estudiante no encontrado.");
        }
    }

    // Calcular la nota media de todos los estudiantes
    public void calcularMedia() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados para calcular la media.");
        } else {
            double suma = 0;
            for (Estudiante estudiante : estudiantes) {
                suma += estudiante.getNota();
            }
            double media = suma / estudiantes.size();
            System.out.println("La nota media de los estudiantes es: " + media);
        }
    }

    // Leer todos los estudiantes del archivo
    private List<Estudiante> leerEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String nombre = partes[0];
                    double nota = Double.parseDouble(partes[1]);
                    estudiantes.add(new Estudiante(nombre, nota));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado. Se creará uno nuevo al añadir estudiantes.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return estudiantes;
    }
}
