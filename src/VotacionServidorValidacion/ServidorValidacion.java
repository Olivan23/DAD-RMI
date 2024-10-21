package VotacionServidorValidacion;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorValidacion {

    public static void main(String[] args) {
        try {
            // Crear el registro RMI en el puerto 5100
            Registry registry = LocateRegistry.createRegistry(5102);

            // Crear las implementaciones
            ValidacionImpl validacion = new ValidacionImpl();

            // Registrar las implementaciones en el registro
            registry.rebind("validacion", validacion);

            System.out.println("Servidor RMI está corriendo en el puerto 5100.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
