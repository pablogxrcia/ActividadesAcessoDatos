package ejercicio3;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class controlador implements Initializable{

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnExportarJSON;

    @FXML
    private Button btnExportarXML;

    @FXML
    private Button btnImportarJSON;

    @FXML
    private Button btnImportarXML;

    @FXML
    private Button btnInsertar;
    
    @FXML
    private TableColumn<Empleado, Integer> columnaID;

    @FXML
    private TableColumn<Empleado, String> idApellidos;

    @FXML
    private TableColumn<Empleado, String> idDepart;

    @FXML
    private TableColumn<Empleado, String> idNombre;

    @FXML
    private TableColumn<Empleado, Double> idSueldo;

    @FXML
    private TableView<Empleado> tabla;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtDepartamento;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtSueldo;
    
    @FXML
    private Label info;
    
    private ConfigManager config;
    private ObservableList<Empleado> listaEmpleados;
    
    public controlador(){
        config = new ConfigManager();
    }
    
    @FXML
    void actualizar(ActionEvent event) {
        if (!validarCampos()) return;

        Empleado empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado == null) {
            mostrarError("Seleccione un empleado para actualizar.");
            return;
        }

        empleadoSeleccionado.setNombre(txtNombre.getText().trim());
        empleadoSeleccionado.setApellidos(txtApellidos.getText().trim());
        empleadoSeleccionado.setDepartamento(txtDepartamento.getText().trim());
        empleadoSeleccionado.setSueldo(Double.parseDouble(txtSueldo.getText().trim()));

        tabla.refresh();
        info.setText("Empleado actualizado correctamente.");
        guardarDatosEnBinario();
    }

    @FXML
    void borrar(ActionEvent event) {
        Empleado empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado == null) {
            mostrarError("Seleccione un empleado para eliminar.");
            return;
        }

        listaEmpleados.remove(empleadoSeleccionado);
        info.setText("Empleado eliminado correctamente.");
        guardarDatosEnBinario();
    }
    

    @FXML
    void exportarJSON(ActionEvent event) {
         String jsonFile = config.getProperty("fichero_json");
        File file = new File(jsonFile);

        // Convertir la ObservableList a un ArrayList para que Jackson pueda manejarla
        ArrayList<Empleado> empleadosList = new ArrayList<>(listaEmpleados);

        try {
            // Crear un objeto ObjectMapper de Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Serializar la lista de empleados al archivo JSON
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, empleadosList);

            info.setText("Datos exportados correctamente a JSON.");
        } catch (IOException e) {
            mostrarError("Error al exportar los datos a JSON: " + e.getMessage());
            e.printStackTrace();  // Esto imprimirá más detalles del error en la consola
        }
    }

    @FXML
    void exportarXML(ActionEvent event) {
        String xmlFile = config.getProperty("fichero_xml");
        File file = new File(xmlFile);

        // Crear el contenedor de empleados
        Empleados empleadosContainer = new Empleados();
        empleadosContainer.setEmpleados(listaEmpleados);  // Establecer la lista de empleados en el contenedor

        try {
            // Crear contexto JAXB para la clase Empleados (contenedor de empleados)
            JAXBContext context = JAXBContext.newInstance(Empleados.class);

            // Crear el marshaller (para convertir el objeto en XML)
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Para formato bonito (indentar)

            // Serializar el contenedor de empleados al archivo XML
            marshaller.marshal(empleadosContainer, file);

            info.setText("Datos exportados correctamente a XML.");
        } catch (JAXBException e) {
            mostrarError("Error al exportar los datos a XML: " + e.getMessage());
            e.printStackTrace();  // Esto imprimirá más detalles del error en la consola
        }
    }

    @FXML
    void importarJSON(ActionEvent event) {
        String jsonFile = config.getProperty("fichero_json");
        File file = new File(jsonFile);

        try {
            // Crear un objeto ObjectMapper de Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Leer los datos del archivo JSON y convertirlos en una lista de Empleados
            List<Empleado> empleadosList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Empleado.class));

            // Actualizar la lista ObservableList con los datos importados
            listaEmpleados.setAll(empleadosList);

            info.setText("Datos importados correctamente desde JSON.");
        } catch (IOException e) {
            mostrarError("Error al importar los datos desde JSON: " + e.getMessage());
            e.printStackTrace();  // Esto imprimirá más detalles del error en la consola
        }
    }

    @FXML
    void importarXML(ActionEvent event) {
        String xmlFile = config.getProperty("fichero_xml");
        File file = new File(xmlFile);

        try {
            // Crear contexto JAXB para la clase Empleados, que contiene la lista de Empleados
            JAXBContext context = JAXBContext.newInstance(Empleados.class);

            // Crear el unmarshaller (para convertir XML en objetos Java)
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Deserializar el archivo XML en una instancia de Empleados
            Empleados empleadosContainer = (Empleados) unmarshaller.unmarshal(file);

            // Obtener la lista de empleados del contenedor
            List<Empleado> empleadosList = empleadosContainer.getEmpleados();

            // Actualizar la lista ObservableList con los datos importados
            listaEmpleados.setAll(empleadosList);

            info.setText("Datos importados correctamente desde XML.");
        } catch (JAXBException e) {
            mostrarError("Error al importar los datos desde XML: " + e.getMessage());
            e.printStackTrace();  // Esto imprimirá más detalles del error en la consola
        }
    }

    @FXML
    void insertar(ActionEvent event) {
        if (!validarCampos()) return;

        int nuevoId = config.getIdEmpleado() + 1;
        config.incrementIdEmpleado();

        Empleado empleado = new Empleado(
                nuevoId,
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtDepartamento.getText().trim(),
                Double.parseDouble(txtSueldo.getText().trim())
        );

        listaEmpleados.add(empleado);
        info.setText("Empleado añadido correctamente.");
        guardarDatosEnBinario();
    }
    
    private File obtenerArchivo(String nombreArchivo) {
        try {
            return new File(getClass().getClassLoader().getResource("resources/" + nombreArchivo).toURI());
        } catch (Exception e) {
            info.setText("Error accediendo al archivo: " + nombreArchivo);
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaEmpleados = FXCollections.observableArrayList(); // Inicializa la lista de empleados
        tabla.setItems(listaEmpleados);

        // Configurar las columnas de la tabla
        columnaID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        idNombre.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNombre()));
        idApellidos.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getApellidos()));
        idDepart.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartamento()));
        idSueldo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getSueldo()));


        // Cargar datos desde el archivo binario al iniciar la aplicación
        cargarDatosDesdeBinario();

        // Vincular la selección de la tabla con los campos de texto
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarFormulario(newSelection);
            }
        });
    }
    
    private void cargarFormulario(Empleado empleado) {
        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtDepartamento.setText(empleado.getDepartamento());
        txtSueldo.setText(String.valueOf(empleado.getSueldo()));
    }
    
    
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            errores.append("El campo 'Apellidos' no puede estar vacío.\n");
        }
        if (txtDepartamento.getText().trim().isEmpty()) {
            errores.append("El campo 'Departamento' no puede estar vacío.\n");
        }
        if (txtSueldo.getText().trim().isEmpty()) {
            errores.append("El campo 'Sueldo' no puede estar vacío.\n");
        } else {
            try {
                Double.parseDouble(txtSueldo.getText());
            } catch (NumberFormatException e) {
                errores.append("El campo 'Sueldo' debe ser un número válido.\n");
            }
        }

        if (errores.length() > 0) {
            // Mostrar los errores en el campo "info"
            info.setText(errores.toString());

            // Mostrar un diálogo de error
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Validación de Campos");
            alerta.setHeaderText("Errores encontrados:");
            alerta.setContentText(errores.toString());
            alerta.showAndWait();
            return false;
        }

    return true; // Todos los campos son válidos
}

    
    
    
    
    
    ////////////////////////////////////////////////////metodos auxiliares ///////////////////////////////////////////////////
    
    private void cargarDatosDesdeBinario() {
        String binFile = config.getProperty("fichero_binario");
        File file = new File(binFile);

        // Si el archivo no existe, crearlo vacío
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                mostrarError("Error al crear el archivo binario.");
                return;
            }
            cargarDatosDeEjemplo(); // Si el archivo está vacío, cargar datos de ejemplo
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binFile))) {
            // Leer la lista de empleados desde el archivo
            ArrayList<Empleado> empleadosList = (ArrayList<Empleado>) ois.readObject();
            listaEmpleados.setAll(empleadosList);  // Convertir ArrayList a ObservableList
        } catch (FileNotFoundException e) {
            cargarDatosDeEjemplo(); // Cargar datos de ejemplo si el archivo está vacío
        } catch (IOException | ClassNotFoundException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }
    }
    
    private void cargarDatosDeEjemplo() {
        // Crear algunos empleados de ejemplo
        listaEmpleados.add(new Empleado(config.getIdEmpleado(), "Juan", "Perez", "Ventas", 25000));
        config.incrementIdEmpleado();
        listaEmpleados.add(new Empleado(config.getIdEmpleado(), "Ana", "Gomez", "Marketing", 28000));
        config.incrementIdEmpleado();

        // Guardar los empleados en el archivo binario
        guardarDatosEnBinario();
    }

    private void guardarDatosEnBinario() {
        String binFile = config.getProperty("fichero_binario");

        // Convertir ObservableList a ArrayList para serialización
        ArrayList<Empleado> empleadosList = new ArrayList<>(listaEmpleados);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binFile))) {
            oos.writeObject(empleadosList);  // Serializar el ArrayList
            info.setText("Datos guardados correctamente.");
        } catch (IOException e) {
            mostrarError("Error al guardar los datos: " + e.getMessage());
        }
    }

    
    private void mostrarError(String mensaje) {
        System.err.println(mensaje);  // Esto imprimirá detalles adicionales en la consola
        info.setText(mensaje);

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
