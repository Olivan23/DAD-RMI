package VotacionEscrutador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class EscrutadorGUI {

    private Escrutador escrutador;

    public EscrutadorGUI() {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.237.215", 5100);
            escrutador = (Escrutador) registry.lookup("escrutador");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor RMI.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Si no se puede conectar, salir del constructor.
        }

        JFrame frame = new JFrame("Escrutador - Sistema de Votación");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Botones
        JButton verVotosBtn = new JButton("Ver Conteo de Votos");
        JButton estadoVotacionBtn = new JButton("Consultar Estado de Votación");
        JButton registrarVotanteBtn = new JButton("Registrar Votante");

        // Panel de botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(verVotosBtn);
        panel.add(estadoVotacionBtn);
        panel.add(registrarVotanteBtn);

        frame.add(panel, BorderLayout.CENTER);

        // Acción de ver votos
        verVotosBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> conteoVotos = escrutador.verConteoVotos();
                    JOptionPane.showMessageDialog(frame, String.join("\n", conteoVotos));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al obtener el conteo de votos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción de consultar estado de votación
        estadoVotacionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean estado = escrutador.consultarEstadoVotacion();
                    String mensaje = estado ? "La votación está ABIERTA." : "La votación está CERRADA.";
                    JOptionPane.showMessageDialog(frame, mensaje);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al consultar el estado de la votación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción de registrar votante
        registrarVotanteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField dniField = new JTextField();
                JTextField nombreField = new JTextField();
                JTextField apellidoPaternoField = new JTextField();
                JTextField apellidoMaternoField = new JTextField();
                JTextField direccionField = new JTextField();

                Object[] inputs = {
                    "DNI:", dniField,
                    "Nombre:", nombreField,
                    "Apellido Paterno:", apellidoPaternoField,
                    "Apellido Materno:", apellidoMaternoField,
                    "Dirección:", direccionField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputs, "Registrar Votante", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int dni = Integer.parseInt(dniField.getText());
                        String nombre = nombreField.getText();
                        String apellidoPaterno = apellidoPaternoField.getText();
                        String apellidoMaterno = apellidoMaternoField.getText();
                        String direccion = direccionField.getText();

                        // Intentar registrar al votante
                        escrutador.registrarVotante(dni, nombre, apellidoPaterno, apellidoMaterno, direccion);
                        JOptionPane.showMessageDialog(frame, "Votante registrado exitosamente.");
                    }catch (NumberFormatException ex) {
                        // Manejo de excepción para el caso de formato incorrecto del DNI
                        JOptionPane.showMessageDialog(frame, "Error: El DNI debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    // Manejo de la excepción SQLException y mostrar el mensaje
                     catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new EscrutadorGUI();
    }
}