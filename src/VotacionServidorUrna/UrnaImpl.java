package VotacionServidorUrna;

import ConexionBD.ConexionMySQL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UrnaImpl extends UnicastRemoteObject implements Urna {

    public UrnaImpl() throws RemoteException {
        super();
    }

    @Override
    public void registrarVoto(int dni, int candidato) throws RemoteException { // Cambiado a int
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "INSERT INTO votos (id_candidato) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, candidato); // Se usa candidato como int
            stmt.executeUpdate();

            // Actualizar el estado de que el votante ha votado
            String updateQuery = "UPDATE votantes SET haVotado = TRUE WHERE dni = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, dni);
            updateStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al registrar el voto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verificarEstadoVotante(int dni) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT haVotado FROM votantes WHERE dni = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("haVotado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Retorna false si hay un error
    }

    @Override
    public List<String> obtenerCandidatos() throws RemoteException {
        List<String> candidatos = new ArrayList<>();
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT id, nombre FROM candidatos"; // Suponiendo que hay una columna id
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Añadir el nombre del candidato y el ID a una lista de alguna manera
                candidatos.add(rs.getInt("id") + ": " + rs.getString("nombre")); // Asegúrate de ajustar esto según sea necesario
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener candidatos: " + e.getMessage(), e);
        }
        return candidatos;
    }
}
