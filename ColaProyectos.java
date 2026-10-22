import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

// Clase que administra la cola de prioridad de proyectos
public class ColaProyectos {

    private PriorityQueue<Proyecto> colaProyectos;

    public ColaProyectos() {
        colaProyectos = new PriorityQueue<Proyecto>();

        // 5 proyectos predefinidos que se muestran al ejecutar el programa
        colaProyectos.add(new Proyecto(101, "Sistema de inventario", Proyecto.TECNOLOGICO, 15000.00f, 8));
        colaProyectos.add(new Proyecto(102, "Comedor comunitario", Proyecto.SOCIAL, 8000.00f, 10));
        colaProyectos.add(new Proyecto(103, "Reforestación del parque", Proyecto.AMBIENTAL, 5000.00f, 6));
        colaProyectos.add(new Proyecto(104, "App de citas médicas", Proyecto.TECNOLOGICO, 12000.00f, 4));
        colaProyectos.add(new Proyecto(105, "Reciclaje en escuelas", Proyecto.AMBIENTAL, 3000.00f, 2));
    }

    // R1: agrega un proyecto a la cola. Rechaza códigos repetidos.
    public void agregar(Proyecto p) throws Exception {
        if (existeCodigo(p.getCodigo())) {
            throw new Exception("El código " + p.getCodigo() + " ya existe. No se puede registrar.");
        }
        colaProyectos.add(p);
    }

    // Verifica si un código ya está registrado en la cola
    public boolean existeCodigo(int codigo) {
        for (Proyecto p : colaProyectos) {
            if (p.getCodigo() == codigo) {
                return true;
            }
        }
        return false;
    }

    // R1: muestra los proyectos ordenados de mayor a menor prioridad
    public String mostrar() {
        List<Proyecto> lista = new ArrayList<>(colaProyectos);
        Collections.sort(lista);

        StringBuilder sb = new StringBuilder();
        sb.append("PROYECTOS (de mayor a menor prioridad)\n");
        sb.append("------------------------------------------------------------\n");
        int posicion = 1;
        for (Proyecto p : lista) {
            sb.append(posicion + ". " + p.toString() + "\n");
            posicion++;
        }
        return sb.toString();
    }

    // R2: muestra todos los proyectos con sus datos y al final el costo ajustado
    public String mostrarConCostoAjustado() {
        List<Proyecto> lista = new ArrayList<>(colaProyectos);
        Collections.sort(lista);

        StringBuilder sb = new StringBuilder();
        sb.append("PROYECTOS CON COSTO AJUSTADO (de mayor a menor prioridad)\n");
        sb.append("------------------------------------------------------------\n");
        int posicion = 1;
        for (Proyecto p : lista) {
            sb.append(posicion + ". " + p.toString()
                    + " | Costo ajustado: $" + String.format("%.2f", p.calcularCostoAjustado()) + "\n");
            posicion++;
        }
        return sb.toString();
    }

    // R3: suma el costo estimado de los proyectos de un tipo
    public float totalCostoEstimado(String tipo) {
        float total = 0;
        for (Proyecto p : colaProyectos) {
            if (p.getTipo().equalsIgnoreCase(tipo)) {
                total = total + p.getCostoEstimado();
            }
        }
        return total;
    }

    // R3: suma el costo ajustado de los proyectos de un tipo
    public float totalCostoAjustado(String tipo) {
        float total = 0;
        for (Proyecto p : colaProyectos) {
            if (p.getTipo().equalsIgnoreCase(tipo)) {
                total = total + p.calcularCostoAjustado();
            }
        }
        return total;
    }

    public int getCantidad() {
        return colaProyectos.size();
    }
}
