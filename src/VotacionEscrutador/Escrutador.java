package VotacionEscrutador;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Escrutador extends Remote {

    List<String> verConteoVotos() throws RemoteException;

    boolean consultarEstadoVotacion() throws RemoteException;

    void registrarVotante(int dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException;

}
