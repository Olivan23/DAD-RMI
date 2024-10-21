package VotacionAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class AdminGUI {
    private Admin admin;

    public AdminGUI() {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.237.215", 5099);
            admin = (Admin) registry.lookup("admin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Admin - Gestión de Votaciones");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Botones
        JButton abrirBtn = new JButton("Abrir Votación");
        JButton cerrarBtn = new JButton("Cerrar Votación");
        JButton agregarCandidatoBtn = new JButton("Agregar Candidato");
        JButton eliminarCandidatoBtn = new JButton("Eliminar Candidato");
        JButton listarCandidatosBtn = new JButton("Listar Candidatos");

        // Panel de botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(abrirBtn);
        panel.add(cerrarBtn);
        panel.add(agregarCandidatoBtn);
        panel.add(eliminarCandidatoBtn);
        panel.add(listarCandidatosBtn);

        frame.add(panel, BorderLayout.CENTER);

        // Acción de abrir votación
        abrirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    admin.abrirVotacion();
                    JOptionPane.showMessageDialog(frame, "Votación abierta.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Acción de cerrar votación
        cerrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    admin.cerrarVotacion();
                    JOptionPane.showMessageDialog(frame, "Votación cerrada.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Agregar candidato
        agregarCandidatoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre del candidato:");
                String partido = JOptionPane.showInputDialog(frame, "Ingrese el partido del candidato:");
                try {
                    admin.agregarCandidato(nombre, partido);
                    JOptionPane.showMessageDialog(frame, "Candidato agregado: " + nombre);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Eliminar candidato
        eliminarCandidatoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(frame, "Ingrese el ID del candidato a eliminar:");
                int id = Integer.parseInt(idStr);
                try {
                    admin.eliminarCandidato(id);
                    JOptionPane.showMessageDialog(frame, "Candidato eliminado con ID: " + id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listar candidatos
        listarCandidatosBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> candidatos = admin.listarCandidatos();
                    JOptionPane.showMessageDialog(frame, String.join("\n", candidatos));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminGUI();
    }
}
