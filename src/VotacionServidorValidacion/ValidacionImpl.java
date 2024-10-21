package VotacionServidorValidacion;

import ConexionBD.ConexionMySQL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValidacionImpl extends UnicastRemoteObject implements Validacion {

    public ValidacionImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean validarVotante(int dni) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT COUNT(*) FROM votantes WHERE dni = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el votante está registrado
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Retorna false si hay un error
    }

    @Override
    public boolean yaVoto(int dni) throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT haVotado FROM votantes WHERE dni = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("haVotado"); // Retorna true si el votante ya ha votado
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Retorna false si hay un error
    }

    @Override
    public boolean votacionAbierta() throws RemoteException {
        try (Connection conn = ConexionMySQL.getConnection()) {
            String query = "SELECT votacion_abierta FROM configuracion_votacion WHERE id = 1"; // Consulta el único registro
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("votacion_abierta"); // Retorna el estado de la votación
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Retorna false si hay un error
    }

}
