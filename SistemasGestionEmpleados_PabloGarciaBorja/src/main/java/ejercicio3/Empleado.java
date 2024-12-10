package ejercicio3;
import javax.xml.bind.annotation.*;

import java.io.Serializable;

@XmlRootElement(name = "empleado")  // Definimos un nombre para el elemento raíz
@XmlAccessorType(XmlAccessType.FIELD)  // Indica que JAXB debe acceder a los campos directamente
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;  // serialVersionUID para la compatibilidad de serialización

    @XmlElement  // Anotamos cada campo que queremos que se mapee al XML
    private int id;

    @XmlElement
    private String nombre;

    @XmlElement
    private String apellidos;

    @XmlElement
    private String departamento;

    @XmlElement
    private double sueldo;
    
    public Empleado(){}

    // Constructor
    public Empleado(int id, String nombre, String apellidos, String departamento, double sueldo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
        this.sueldo = sueldo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDepartamento() { return departamento; }
    public double getSueldo() { return sueldo; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }
}