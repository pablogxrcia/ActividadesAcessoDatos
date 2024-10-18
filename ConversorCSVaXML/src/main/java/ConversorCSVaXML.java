import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ConversorCSVaXML {
    private static final Logger logger = LoggerFactory.getLogger(ConversorCSVaXML.class);

    public void convertirCSVaXML(String csvFilePath, String xmlFilePath) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> registros = reader.readAll();

            // Crear un documento XML vacío
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Crear el nodo raíz
            Element rootElement = doc.createElement("Estudiantes");
            doc.appendChild(rootElement);

            // Recorrer los registros CSV y añadirlos al XML
            for (String[] registro : registros) {
                Element estudiante = doc.createElement("Estudiante");
                rootElement.appendChild(estudiante);

                // Añadir elementos al nodo estudiante
                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(registro[0]));
                estudiante.appendChild(id);

                Element nombre = doc.createElement("Nombre");
                nombre.appendChild(doc.createTextNode(registro[1]));
                estudiante.appendChild(nombre);

                Element apellido = doc.createElement("Apellido");
                apellido.appendChild(doc.createTextNode(registro[2]));
                estudiante.appendChild(apellido);

                Element edad = doc.createElement("Edad");
                edad.appendChild(doc.createTextNode(registro[3]));
                estudiante.appendChild(edad);

                Element curso = doc.createElement("Curso");
                curso.appendChild(doc.createTextNode(registro[4]));
                estudiante.appendChild(curso);
            }

            // Escribir el contenido en un archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFilePath);
            transformer.transform(source, result);

            logger.info("Conversión completada. Archivo XML generado en " + xmlFilePath);

        } catch (IOException | CsvException | ParserConfigurationException | javax.xml.transform.TransformerException e) {
            logger.error("Error al convertir el CSV a XML", e);
        }
    }

    public static void main(String[] args) {
        ConversorCSVaXML conversor = new ConversorCSVaXML();
        conversor.convertirCSVaXML("estudiantes.csv", "estudiantes.xml");
    }
}
