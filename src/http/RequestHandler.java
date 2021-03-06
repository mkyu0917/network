package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler extends Thread {
	private static final String DOCUMENT_ROOT = "./webapp";

	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress(); //연결이 되었다고 로그를 뿌림
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":"	   //
					+ inetSocketAddress.getPort());

			// get IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			//소켓의 바이트 데이터를 문자열 데이터로 그리고 라인단위로 받음
			OutputStream os = socket.getOutputStream();
			//while문을 통해서 요청정보를 모두 출력한다.
			
			String request= null;
			
			
			while(true) {
				String line = br.readLine();
				String[] tokens = line.split(" ");
				String url = tokens[1];
				String protocol = tokens[2];
				responseStaticResource(os,url,protocol);
				
				if (line == null ||"".equals(line)) {
					break;
				}
				if( request == null) {
					request = line;
					break;
				}
				
			}
			consoleLog(request);
			
								
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
//			os.write( "\r\n".getBytes() );
//			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch ( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try {
				if ( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch ( IOException ex)  {
				consoleLog( "error:" + ex );
			}
		}
	}

	
	private void consoleLog(String message) {
		
		System.out.println("[RequestHandler#" + getId() + "] " + message);
		
		
	}
	private void responseStaticResource(OutputStream outputStream, String url, String protocol )
				throws IOException {

		File file = new File( "./webapp" + url );
		Path path = file.toPath();
		byte[] body = Files.readAllBytes( path );
		
		outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ));
		outputStream.write( "Content-Type:text/html\r\n".getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write( body );
  
			  }  

	
	

	}
