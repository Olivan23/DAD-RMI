package VotacionEscrutador;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorEscrutador {
    public static void main(String[] args) {
        try {
            Escrutador escrutador = new Implementar_Escrutador();
            Registry registry = LocateRegistry.createRegistry(5100); // Puerto para el servidor de escrutador
            registry.rebind("escrutador", escrutador);
            System.out.println("Servidor de Escrutador listo en el puerto 5100.");
        } catch (Exception e) {
            System.err.println("Error en el servidor de Escrutador: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
