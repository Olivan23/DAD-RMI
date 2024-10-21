package VotacionAdmin;

import ConexionBD.ConexionMySQL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Implementar_Admin extends UnicastRemoteObject implements Admin {

    public Implementar_Admin() throws RemoteException {
        super();
    }

    @Override
    public void abrirVotacion() throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "UPDATE configuracion_votacion SET votacion_abierta = TRUE WHERE id = 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
            System.out.println("Votación abierta.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cerrarVotacion() throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "UPDATE configuracion_votacion SET votacion_abierta = FALSE WHERE id = 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
            System.out.println("Votación cerrada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void agregarCandidato(String nombre, String partido) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "INSERT INTO candidatos (nombre, partido) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, partido);
            stmt.executeUpdate();
            System.out.println("Candidato agregado: " + nombre);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarCandidato(int idCandidato) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "DELETE FROM candidatos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idCandidato);
            stmt.executeUpdate();
            System.out.println("Candidato eliminado con ID: " + idCandidato);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> listarCandidatos() throws RemoteException {
        List<String> candidatos = new ArrayList<>();
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT id, nombre, partido FROM candidatos";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String partido = rs.getString("partido");
                candidatos.add("ID: " + id + " - " + nombre + " (" + partido + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidatos;
    }
}
