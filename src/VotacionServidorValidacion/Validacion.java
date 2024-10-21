package VotacionServidorValidacion;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Validacion extends Remote {
    boolean validarVotante(int dni) throws RemoteException;
    boolean yaVoto(int dni) throws RemoteException;
    boolean votacionAbierta() throws RemoteException; // Nuevo método
}
