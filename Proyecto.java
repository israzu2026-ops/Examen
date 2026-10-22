// Clase que representa un proyecto que debe ser ejecutado por una organización
public class Proyecto implements Comparable<Proyecto> {

    // Tipos de proyecto permitidos
    public static final String TECNOLOGICO = "Tecnológico";
    public static final String SOCIAL = "Social";
    public static final String AMBIENTAL = "Ambiental";

    private int codigo;
    private String nombre;
    private String tipo;
    private float costoEstimado;
    private int prioridad;      // 1 (más baja) a 10 (más alta)

    // Constructor por defecto con valores predefinidos
    public Proyecto() {
        this.codigo = 0;
        this.nombre = "Proyecto sin nombre";
        this.tipo = TECNOLOGICO;
        this.costoEstimado = 1000.0f;
        this.prioridad = 1;
    }

    // Constructor con parámetros para inicializar todos los atributos
    public Proyecto(int codigo, String nombre, String tipo, float costoEstimado, int prioridad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.costoEstimado = costoEstimado;
        this.prioridad = prioridad;
    }

    // R2: calcula el costo ajustado según el tipo de proyecto.
    // No modifica el costo estimado, por eso no se acumulan recargos ni descuentos.
    public float calcularCostoAjustado() {
        if (tipo.equalsIgnoreCase(TECNOLOGICO)) {
            return costoEstimado * 1.15f;     // +15%
        } else if (tipo.equalsIgnoreCase(SOCIAL)) {
            return costoEstimado * 0.90f;     // -10%
        } else if (tipo.equalsIgnoreCase(AMBIENTAL)) {
            return costoEstimado * 0.95f;     // -5%
        }
        return costoEstimado;
    }

    // Ordena de mayor a menor prioridad (10 primero).
    // Si tienen la misma prioridad, se ordena por código.
    @Override
    public int compareTo(Proyecto otro) {
        int resultado = Integer.compare(otro.prioridad, this.prioridad);
        if (resultado == 0) {
            resultado = Integer.compare(this.codigo, otro.codigo);
        }
        return resultado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(float costoEstimado) {
        this.costoEstimado = costoEstimado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Nombre: " + nombre + " | Tipo: " + tipo
                + " | Costo estimado: $" + String.format("%.2f", costoEstimado)
                + " | Prioridad: " + prioridad;
    }
}
