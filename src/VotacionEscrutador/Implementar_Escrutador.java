package VotacionEscrutador;

import ConexionBD.ConexionMySQL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Implementar_Escrutador extends UnicastRemoteObject implements Escrutador {

    public Implementar_Escrutador() throws RemoteException {
        super();
    }

    @Override
    public List<String> verConteoVotos() throws RemoteException {
        List<String> resultados = new ArrayList<>();
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT c.nombre, c.partido, COUNT(v.id_candidato) AS votos "
                    + "FROM candidatos c LEFT JOIN votos v ON c.id = v.id_candidato "
                    + "GROUP BY c.id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String partido = rs.getString("partido");
                int votos = rs.getInt("votos");
                resultados.add(nombre + " (" + partido + ") - Votos: " + votos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    @Override
    public boolean consultarEstadoVotacion() throws RemoteException {
        boolean votacionAbierta = false;
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT votacion_abierta FROM configuracion_votacion WHERE id = 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                votacionAbierta = rs.getBoolean("votacion_abierta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return votacionAbierta;
    }

    @Override
    public void registrarVotante(int dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            // Verificar si el votante ya está registrado
            String checkQuery = "SELECT COUNT(*) FROM votantes WHERE dni = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, dni);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si ya existe el DNI, lanzar excepción con mensaje personalizado
                throw new SQLException("El votante con DNI " + dni + " ya está registrado.");
            }

            // Si no existe, proceder con la inserción del nuevo votante
            String query = "INSERT INTO votantes (dni, nombre, apellidoPaterno, apellidoMaterno, direccion, haVotado) VALUES (?, ?, ?, ?, ?, FALSE)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidoPaterno);
            stmt.setString(4, apellidoMaterno);
            stmt.setString(5, direccion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            try {
                // Manejo de excepciones de SQL
                throw new SQLException("Error en el registro: " + e.getMessage(), e);
            } catch (SQLException ex) {
                Logger.getLogger(Implementar_Escrutador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
