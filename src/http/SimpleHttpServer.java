package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {
	
	private static final int PORT = 8088;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			// 1. Create Server Socket
			serverSocket = new ServerSocket();
			   
			// 2. Bind
			String localhost = InetAddress.getLocalHost().getHostAddress();//서버소켓을 만듬
			serverSocket.bind( new InetSocketAddress( localhost, PORT ) ); //인터넷 소켓어드레스를 바인딩 
			consoleLog("bind " + localhost + ":" + PORT);

			while (true) {
				// 3. Wait for connecting ( accept ), accept로 받으면 누가 요청할때까지 잠수 blocking
				Socket socket = serverSocket.accept();

				// 4. Delegate Processing Request 
				new RequestHandler(socket).start();
			}

		} catch (IOException ex) {
			consoleLog("error:" + ex);
		} finally {
			// 5. clean-up
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException ex) {
				consoleLog("error:" + ex);
			}
		}
	}

	public static void consoleLog(String message) {
		System.out.println("[HttpServer#" + Thread.currentThread().getId()  + "] " + message);
	}
}