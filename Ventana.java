import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class Ventana {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox cmbTipo;
    private JTextField tsxtCosto;
    private JComboBox cmbPrioridad;
    private JButton agregarButton;
    private JTextArea textArea1;
    private JButton ajustarPresupuestoButton;
    private JButton totalesButton;
    private JPanel Ventana;

    private ColaProyectos cola = new ColaProyectos();

    public Ventana() {
        if (textArea1 == null) {
            crearComponentes();
        }
        textArea1.setEditable(false);

        // Al iniciar se muestran los 5 proyectos predefinidos
        textArea1.setText(cola.mostrar());

        // R1: ingreso de proyectos
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String textoCodigo = txtCodigo.getText().trim();
                    String nombre = txtNombre.getText().trim();
                    String textoCosto = tsxtCosto.getText().trim();

                    // Validar campos vacíos
                    if (textoCodigo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Ingrese el código del proyecto");
                        return;
                    }
                    if (nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Ingrese el nombre del proyecto");
                        return;
                    }
                    if (textoCosto.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Ingrese el costo estimado");
                        return;
                    }

                    // Validar código numérico, positivo y no repetido
                    if (!soloNumeros(textoCodigo)) {
                        JOptionPane.showMessageDialog(null, "El código debe ser un número entero positivo (sin letras ni símbolos)");
                        return;
                    }
                    int codigo = Integer.parseInt(textoCodigo);
                    if (codigo <= 0) {
                        JOptionPane.showMessageDialog(null, "El código debe ser mayor a 0");
                        return;
                    }
                    if (cola.existeCodigo(codigo)) {
                        JOptionPane.showMessageDialog(null, "El código " + codigo + " ya existe. No se puede registrar.");
                        return;
                    }

                    // Validar costo numérico y positivo
                    float costo;
                    try {
                        costo = Float.parseFloat(textoCosto.replace(",", "."));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "El costo estimado debe ser un número válido");
                        return;
                    }
                    if (Float.isNaN(costo) || Float.isInfinite(costo)) {
                        JOptionPane.showMessageDialog(null, "El costo estimado debe ser un número válido");
                        return;
                    }
                    if (costo <= 0) {
                        JOptionPane.showMessageDialog(null, "El costo estimado debe ser mayor a 0");
                        return;
                    }

                    String tipo = cmbTipo.getSelectedItem().toString();
                    int prioridad = Integer.parseInt(cmbPrioridad.getSelectedItem().toString());

                    // Incorporar el proyecto a la cola de prioridad
                    Proyecto p = new Proyecto(codigo, nombre, tipo, costo, prioridad);
                    cola.agregar(p);

                    textArea1.setText(cola.mostrar());
                    refrescarVentana();
                    limpiarCampos();
                    JOptionPane.showMessageDialog(null, "Proyecto registrado correctamente");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El código no es un número válido");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        // R2: mostrar todos los proyectos con su costo ajustado
        ajustarPresupuestoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText(cola.mostrarConCostoAjustado());
                refrescarVentana();
            }
        });

        // R3: totales por tipo de proyecto en una ventana emergente
        totalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] tipos = {Proyecto.TECNOLOGICO, Proyecto.SOCIAL, Proyecto.AMBIENTAL};
                Object seleccion = JOptionPane.showInputDialog(null,
                        "Seleccione el tipo de proyecto:", "Totales por tipo",
                        JOptionPane.QUESTION_MESSAGE, null, tipos, cmbTipo.getSelectedItem());
                if (seleccion == null) {
                    return;     // el usuario canceló
                }
                String tipo = seleccion.toString();
                JOptionPane.showMessageDialog(null, mensajeTotales(tipo));
            }
        });
    }

    // Crea los componentes si IntelliJ no los generó desde el .form
    private void crearComponentes() {
        Ventana = new JPanel(new GridBagLayout());
        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        cmbTipo = new JComboBox(new String[]{"Tecnológico", "Ambiental", "Social"});
        tsxtCosto = new JTextField(15);
        cmbPrioridad = new JComboBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        agregarButton = new JButton("Agregar");
        ajustarPresupuestoButton = new JButton("Ajustar presupuesto");
        totalesButton = new JButton("Totales");
        textArea1 = new JTextArea(12, 80);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        String[] etiquetas = {"Código", "Nombre", "Tipo de proyecto", "Costo estimado", "Prioridad"};
        JComponent[] campos = {txtCodigo, txtNombre, cmbTipo, tsxtCosto, cmbPrioridad};
        JButton[] botones = {agregarButton, ajustarPresupuestoButton, totalesButton};
        for (int i = 0; i < 5; i++) {
            g.gridy = i;
            g.gridx = 0;
            Ventana.add(new JLabel(etiquetas[i]), g);
            g.gridx = 1;
            Ventana.add(campos[i], g);
            if (i < 3) {
                g.gridx = 2;
                Ventana.add(botones[i], g);
            }
        }
        g.gridy = 5;
        g.gridx = 0;
        g.gridwidth = 3;
        g.fill = GridBagConstraints.BOTH;
        g.weightx = 1;
        g.weighty = 1;
        Ventana.add(new JScrollPane(textArea1), g);
    }

    // Arma el mensaje de totales de un tipo de proyecto
    public String mensajeTotales(String tipo) {
        float totalEstimado = cola.totalCostoEstimado(tipo);
        float totalAjustado = cola.totalCostoAjustado(tipo);
        return "Tipo de proyecto: " + tipo + "\n"
                + "Costo estimado total: $" + String.format("%.2f", totalEstimado) + "\n"
                + "Costo ajustado total: $" + String.format("%.2f", totalAjustado);
    }

    // Verifica que el texto tenga solo dígitos
    public boolean soloNumeros(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        tsxtCosto.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbPrioridad.setSelectedIndex(0);
    }

    // Ajusta el tamaño de la ventana para que se vea todo el texto
    public void refrescarVentana() {
        Window ventana = SwingUtilities.getWindowAncestor(Ventana);
        if (ventana != null) {
            ventana.pack();
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);   // decimales con punto: 0.00
        JFrame frame = new JFrame("Gestión de proyectos con prioridad");
        frame.setContentPane(new Ventana().Ventana);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
