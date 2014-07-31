

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class SocketManager extends Thread{

	private static Logger logger = Logger.getLogger(SocketManager.class);
	
	private static ServerSocket serverSocket = null;
	private Map<String, SocketThread> socketMap = new HashMap<String, SocketThread>();
	
	public SocketManager(){
		if(serverSocket == null){
			try {
				serverSocket = new ServerSocket(7071);
				System.out.println("7071端口监听成功");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Couldn't find 7071 port");
				System.out.println("System close");
				System.exit(1);
			}
		}
	}
	
	public static void main(String[] args) {
		new SocketManager().run();
	}
	
	public void run(){
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				String ip = socket.getInetAddress().getHostAddress();
				if(socketMap.containsKey(ip)){
					socketMap.remove(ip);
				}else{
					SocketThread thread = new SocketThread(socket);
					socketMap.put(ip, thread);
					thread.start();
				}
				
			} catch (IOException e) {
				logger.error("Socket Accept failed", e);
			}
		}
	}
	
}
