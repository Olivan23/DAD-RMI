package VotacionServidorUrna;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Urna extends Remote {
    void registrarVoto(int dni, int candidato) throws RemoteException; // Cambiado a int
    boolean verificarEstadoVotante(int dni) throws RemoteException;
    List<String> obtenerCandidatos() throws RemoteException;
}
