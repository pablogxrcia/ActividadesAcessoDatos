import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorContactos {
    private static final String FILE_NAME = "contactos.txt";
    private List<Contacto> contactos;

    public GestorContactos() {
        contactos = new ArrayList<>();
        cargarContactos();
    }

    public void añadirContacto(Contacto contacto) {
        contactos.add(contacto);
        guardarContactoEnArchivo(contacto);
    }

    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos guardados.");
        } else {
            for (Contacto contacto : contactos) {
                System.out.println(contacto);
            }
        }
    }

    public Contacto buscarContacto(String nombre) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)) {
                return contacto;
            }
        }
        return null;
    }

    public void eliminarContacto(String nombre) {
        Contacto contacto = buscarContacto(nombre);
        if (contacto != null) {
            contactos.remove(contacto);
            guardarTodosLosContactos();
            System.out.println("Contacto eliminado: " + nombre);
        } else {
            System.out.println("Contacto no encontrado.");
        }
    }

    private void guardarContactoEnArchivo(Contacto contacto) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(contacto.getNombre() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el contacto.");
        }
    }

    private void guardarTodosLosContactos() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Contacto contacto : contactos) {
                writer.write(contacto.getNombre() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los contactos.");
        }
    }

    private void cargarContactos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    Contacto contacto = new Contacto(datos[0], datos[1], datos[2]);
                    contactos.add(contacto);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de contactos.");
        }
    }

    public static void main(String[] args) {
        GestorContactos gestor = new GestorContactos();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú:");
            System.out.println("1. Añadir contacto");
            System.out.println("2. Listar contactos");
            System.out.println("3. Buscar contacto");
            System.out.println("4. Eliminar contacto");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    Contacto contacto = new Contacto(nombre, telefono, email);
                    gestor.añadirContacto(contacto);
                    System.out.println("Contacto añadido.");
                    break;
                case 2:
                    gestor.listarContactos();
                    break;
                case 3:
                    System.out.print("Nombre del contacto a buscar: ");
                    nombre = scanner.nextLine();
                    Contacto encontrado = gestor.buscarContacto(nombre);
                    if (encontrado != null) {
                        System.out.println("Contacto encontrado: " + encontrado);
                    } else {
                        System.out.println("Contacto no encontrado.");
                    }
                    break;
                case 4:
                    System.out.print("Nombre del contacto a eliminar: ");
                    nombre = scanner.nextLine();
                    gestor.eliminarContacto(nombre);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        scanner.close();
    }
}
