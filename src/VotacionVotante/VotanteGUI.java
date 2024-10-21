package VotacionVotante;

import VotacionServidorUrna.Urna;
import VotacionServidorValidacion.Validacion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class VotanteGUI {

    private Validacion validacion;
    private Urna urna;

    public VotanteGUI() {
        try {
            // Conectarse al servidor de validación en el puerto 5101
            Registry registryValidacion = LocateRegistry.getRegistry("localhost", 5102);
            validacion = (Validacion) registryValidacion.lookup("validacion");

            // Conectarse al servidor de urna en el puerto 5100
            Registry registryUrna = LocateRegistry.getRegistry("localhost", 5101);
            urna = (Urna) registryUrna.lookup("urna");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor RMI.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Salir si hay error en la conexión.
        }

        JFrame frame = new JFrame("Votante - Sistema de Votación");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton votarBtn = new JButton("Votar");
        JButton verificarEstadoBtn = new JButton("Verificar Estado de Votación");
        JButton salirBtn = new JButton("Salir");

        panel.add(votarBtn);
        panel.add(verificarEstadoBtn);
        panel.add(salirBtn);

        frame.add(panel, BorderLayout.CENTER);

        // Acción para votar
        votarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isVotingOpen()) {
                    JOptionPane.showMessageDialog(frame, "La votación no está abierta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Si la votación no está abierta, salir del método
                }

                JTextField dniField = new JTextField();
                JPanel candidatosPanel = new JPanel();
                List<String> candidatos = obtenerCandidatos(); // Método para obtener candidatos

                // Crear los JRadioButtons para los candidatos
                ButtonGroup buttonGroup = new ButtonGroup();
                for (String candidato : candidatos) {
                    JRadioButton radioButton = new JRadioButton(candidato);
                    radioButton.setActionCommand(candidato.split(":")[0]); // Solo obtener el ID del candidato
                    buttonGroup.add(radioButton);
                    candidatosPanel.add(radioButton);
                }

                Object[] inputs = {
                    "DNI:", dniField,
                    "Seleccione un candidato:", candidatosPanel
                };

                int option = JOptionPane.showConfirmDialog(frame, inputs, "Votar", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int dni = Integer.parseInt(dniField.getText());
                        String candidatoSeleccionado = buttonGroup.getSelection() != null ? buttonGroup.getSelection().getActionCommand() : null;

                        // Validar el votante
                        if (validacion.validarVotante(dni)) {
                            // Verificar si el votante ya ha votado
                            if (validacion.yaVoto(dni)) {
                                JOptionPane.showMessageDialog(frame, "El votante ya ha votado.", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Intentar votar
                                if (candidatoSeleccionado != null) {
                                    urna.registrarVoto(dni, Integer.parseInt(candidatoSeleccionado)); // Pasar ID como int
                                    JOptionPane.showMessageDialog(frame, "Voto registrado exitosamente.");
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Por favor, seleccione un candidato.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "El votante no está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: El DNI debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al votar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Acción para verificar estado de votación
        verificarEstadoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isVotingOpen()) {
                    JOptionPane.showMessageDialog(frame, "La votación no está abierta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Si la votación no está abierta, salir del método
                }

                JTextField dniField = new JTextField();

                Object[] inputs = {
                    "DNI:", dniField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputs, "Verificar Estado", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int dni = Integer.parseInt(dniField.getText());
                        boolean haVotado = urna.verificarEstadoVotante(dni);
                        String mensaje = haVotado ? "El votante con DNI " + dni + " ya ha votado." : "El votante con DNI " + dni + " no ha votado.";
                        JOptionPane.showMessageDialog(frame, mensaje);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: El DNI debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al verificar estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Acción para salir
        salirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private boolean isVotingOpen() {
        try {
            return validacion.votacionAbierta(); // Verifica si la votación está abierta
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar el estado de la votación.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Retorna false si hay un error
        }
    }

    private List<String> obtenerCandidatos() {
        // Método para obtener los candidatos desde la base de datos
        try {
            return urna.obtenerCandidatos();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener candidatos.", "Error", JOptionPane.ERROR_MESSAGE);
            return List.of(); // Retorna una lista vacía si hay un error
        }
    }

    public static void main(String[] args) {
        new VotanteGUI();
    }
}
