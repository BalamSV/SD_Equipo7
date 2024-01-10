/*
    Antonio Segura David
    Bravo Amescua Emiliano
    Sanchez Valdivia Balam
*/

//Creacion de un Servidro RMI y utiliza Hilos para realizar las tareas de segundo plano

import java.net.InetAddress; //Proporciona métodos para obtener información sobre direcciones IP y nombres de host
import java.net.NetworkInterface; //Permite acceder a información sobre interfaces de red, como direcciones MAC y direcciones IP asociadas
import java.net.SocketException; //Una excepción que indica un error en las operaciones de socket
import java.rmi.*; //RMI (Remote Method Invocation) es un mecanismo en Java que permite la invocación de métodos en objetos que residen en otros procesos Java, incluso en máquinas remotas. Aquí se están importando las clases y excepciones necesarias para trabajar con RMI.
import java.rmi.registry.LocateRegistry; //Proporciona métodos estáticos para obtener o crear registros RMI en el host local
import java.rmi.registry.Registry; //Interfaz que define los métodos para realizar operaciones en un registro RMI, como enlazar y buscar objetos remotos.
import java.util.Enumeration; //Interfaz que se utiliza para enumerar elementos en una colección, como una lista o un conjunto
import java.util.Scanner; //Utilizada para analizar datos de entrada, como la entrada del usuario a través del teclado
import java.util.logging.Level; //Define un conjunto estándar de niveles de registro (por ejemplo, INFO, WARNING, SEVERE) que se utilizan en la infraestructura de registro de Java
import java.util.logging.Logger; //Proporciona un sistema de registro flexible y avanzado para las aplicaciones Java

class p2pServer { //Se define la clase
		//Variables estaticas
        static int port; //Puerto
        static String host; //Host
        static Registry registry = null; //Registro RMI
                
	static public void main(String args[]) throws Exception //Verifica que se proporciona los argumentos de linea de comandos adecuados (host y puerto)
	{
                
                if(args.length!=2){
                    System.out.println("Usage: p2pServer <host> <port>");
                    System.exit(0);
                }
                host=args[0];
                port = Integer.parseInt(args[1]);      
		//Establecimiento del administrador de seguridad
		if(System.getSecurityManager() == null) //Si no existe un administrador
		{
			System.setSecurityManager(new SecurityManager()); //Se crea un administrador nuevo
		}

		//Se crea la instancia del servidor
		ServerImplementacion ServerImplementacion = new ServerImplementacion();
		//Enlace en el registro de RMI
		try { //Intento de crear un registro RMI en el puerto especificado y se enlaza la instancia del servidor con un nombre en el registro.        
                        
                        registry = LocateRegistry.createRegistry(port);
			registry.bind("rmi://"+host+":" + port + "/server", ServerImplementacion);
		 } catch (AlreadyBoundException e) { //Si ya esta enlazado, muestra un mensaje de error y sale del programa
			 System.out.println("\nError - Server is already bound.\n");
			 System.exit(0);
		 }
		
		
		System.out.println("Server \""+"rmi://"+host+":" + port + "/server"+"\" running...");
		
		//Bucle principal del programa
		int num=1;
                HiloS h = new HiloS(ServerImplementacion); //Se crea un objeto de hilos
				h.start(); //Se inicia
		System.out.println("Enter 0 to exit: "); //Se imprime la instruccion
		while(num!=0) //Inicio de bucles que mientras no sea 0, sigue en ejecucion
		{
			Scanner scan = new Scanner(System.in);
			num=scan.nextInt();
		}
		h.stop(); //Detiene la tarea del hilo
		//Desenlace y salida
		registry.unbind("rmi://"+host+":" + port + "/server");
		System.exit(0);	//Desvinculacion de la instancia del servidor del registro RMI y se sale del programa
	}
	
        

        

	
}
