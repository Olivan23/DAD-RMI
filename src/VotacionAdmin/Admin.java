package VotacionAdmin;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Admin extends Remote {
    void abrirVotacion() throws RemoteException;
    void cerrarVotacion() throws RemoteException;
    void agregarCandidato(String nombre, String partido) throws RemoteException;
    void eliminarCandidato(int idCandidato) throws RemoteException;
    List<String> listarCandidatos() throws RemoteException;
}
