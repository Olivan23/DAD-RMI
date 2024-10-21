package VotacionAdmin;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorAdmin {
    public static void main(String[] args) {
        try {
            Admin admin = new Implementar_Admin();
            Registry registry = LocateRegistry.createRegistry(5099); // Puerto para el servidor de admin
            registry.rebind("admin", admin);
            System.out.println("Servidor de Admin listo en el puerto 5099.");
        } catch (Exception e) {
            System.err.println("Error en el servidor de Admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
