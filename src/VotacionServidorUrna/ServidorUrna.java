package VotacionServidorUrna;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorUrna {
  
    public static void main(String[] args) {
        try {
            // Crear el registro RMI en el puerto 5100
            Registry registry = LocateRegistry.createRegistry(5101);

            // Crear las implementaciones
            UrnaImpl urna = new UrnaImpl();

            // Registrar las implementaciones en el registro
            registry.rebind("urna", urna);

            System.out.println("Servidor RMI está corriendo en el puerto 5100.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
