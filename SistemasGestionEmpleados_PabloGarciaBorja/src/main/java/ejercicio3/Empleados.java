package ejercicio3;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "empleados")  // El nombre del elemento raíz será "empleados"
@XmlAccessorType(XmlAccessType.FIELD)
public class Empleados {

    @XmlElement(name = "empleado")  // Indica que cada "empleado" es un elemento dentro de la lista
    private List<Empleado> empleados;

    // Getter y setter
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }
}
