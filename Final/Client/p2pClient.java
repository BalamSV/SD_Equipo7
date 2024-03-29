/*
    Antonio Segura David
    Bravo Amescua Emiliano
    Sanchez Valdivia Balam
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
// La clase principal que representa al cliente en la red peer-to-peer
public class p2pClient {
// El método principal que se ejecuta al iniciar el programa
	public static void main(String args[]) throws Exception {
                PeerData p;
                String serverHost;
                int serverPort;
                Registry registry = null;
                String instanceName;
                ServerInterface IndexServer = null;
		// Verifica si se proporcionan los argumentos necesarios
		if (args.length != 5) {
			System.out.println("usage: p2pClient <serverHost> <serverPort> <ClientHost> <clientPort> <idPeer>\n\n");
			return;
		}
		try {
             // Lee los argumentos proporcionados y crea un objeto PeerData
			serverHost = args[0];
			serverPort = Integer.parseInt(args[1]);
                        p=new PeerData(Integer.parseInt(args[4]),args[2],Integer.parseInt(args[3]));
                        
		} catch (NumberFormatException e) {
			System.err.println("Argument must be and Integer");
			return;
		}
        // Verifica que el puerto del servidor no sea igual al puerto del cliente
                if(serverPort==p.port){
                        System.out.println("Server's port can't be equal to Client's port \n\n");
                        return;
                }
// Crea un nombre único para la instancia del cliente
		instanceName = "Peer" + p.Id;

		// Establece un administrador de seguridad si no está configurado
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
                
                try{
                    // Obtiene el registro del servidor
                        registry = LocateRegistry.getRegistry(serverHost,serverPort);//busca el registro del servidor
                        IndexServer = (ServerInterface) registry.lookup("rmi://"+serverHost+":"+serverPort+"/server");
                        // Verifica si el identificador del par ya existe en el servidor
                        if(IndexServer.getPeer(p.Id)!=null){
                            System.out.println("peerId already exists. Please select another peerId");
                            System.exit(0);
                        }
		}catch (RemoteException e){
			System.out.println("\nError - Server \""+"rmi://"+serverHost+":"+serverPort+"/server"+"\" not found.");
			System.exit(0);
		}
        // Crea una implementación del cliente y lo registra en el registro RMI
		ClientImplementacion ClientImplementacion = new ClientImplementacion(IndexServer,p);//crea un objeto tipo clienteImpl y le pasa la referencia a el servidor, y los datos del peer
                registry = getRegistry(p.port);//crea un registro en la maquina actual con el puerto dado
                try{
                    registry.bind("rmi://"+p.host+":" + p.port + "/"+instanceName, ClientImplementacion);//liga una instancia del clienteImpl en el registro
                }catch (RemoteException e) {//en caso de que ya haya sido ligado una instancia con el mismo nombre salta esta excepcion
			System.out.println("\nError binding Peer "+ p.Id +" Please select another peerId");
			System.exit(0);
                }
		System.out.println("ClientServer running... \""+"rmi://"+p.host+":" + p.port + "/"+instanceName+"\"");//muestra la direccion de la instancia
                // Ejecuta el bucle principal del cliente hasta que se complete la operación
                while(ClientImplementacion.run());//bucle hasta recibir falso
                
                System.out.println("Adios");
                try{
                    registry = getRegistry(p.port);//aqui busca el registro segun el puerto dado
                    registry.unbind("rmi://"+p.host+":" + p.port + "/"+instanceName);//quita la instancia
                }catch(NotBoundException e){
                    System.out.println("Error - NotBoundException "+p);
                }
                catch(AccessException e){
                    System.out.println("Error - AccessException "+p);
                }
                catch(NullPointerException e){
                    System.out.println("Error - NullPointerException "+p);
                }
                catch(RemoteException e){
                    System.out.println("Error - RemoteException "+p);
                }
                // Desvincula la instancia del cliente del registro RMI
                try{
                    IndexServer.unregistryPeer(p);
                }catch(RemoteException em){
                    System.out.println("\nError - No se pudo conectar con el servidor.\n");
                }finally{
                    System.exit(0);
                }
	}

// Método para obtener o crear el registro RMI en un puerto específico   
private static Registry getRegistry(int port){
    Registry registry=null;
    try {
        registry = LocateRegistry.createRegistry(port);//crea un registro con el puerto dado
        //System.out.println("rmiregistry iniciado en el puerto:"+port);
    }catch(ExportException e){//en caso de que el registro ya haya sido creado anteriormente salta esta excepcion
        try {
            registry = LocateRegistry.getRegistry(port);//aqui busca el registro segun el puerto dado
            //System.out.println("rmiregistry obtenido en el puerto:"+port);
        }catch(RemoteException em){//cualquier otro error causa estra excepcion
            System.out.println("\nError - No se pudo obtener el registro.\n");
            System.exit(0);
        }
    }catch (RemoteException ex) {
        System.out.println("\nError - No se pudo crear el registro.\n");
        System.exit(0);
    }
    return registry;
}
// Método para obtener la dirección IP del host	
private static String getHost() {
            String host_ = "";
            String prefijo="192";
            boolean buscando=true;
            try {
                Enumeration e = NetworkInterface.getNetworkInterfaces();
                while(e.hasMoreElements() && buscando)
                {
                    NetworkInterface n = (NetworkInterface) e.nextElement();
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements() && buscando)
                    {
                        InetAddress i = (InetAddress) ee.nextElement();
                        host_=i.getHostAddress();
                        if(host_.startsWith(prefijo))
                            buscando=false;
                    }
                }
                
            } catch (SocketException ex) {
                Logger.getLogger(p2pClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            return host_;
   }

}
