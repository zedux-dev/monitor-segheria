package server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server extends Thread {
	
	public void run() {
		try {
    		HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
    		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);

    		server.createContext("/taglio", new MyHttpHandler());
    		server.createContext("/temperatura", new MyHttpHandler());
    		server.setExecutor(threadPoolExecutor);
    		server.start();
    		
    		System.out.println("Sega server started on port: 3000");
    		
    	} catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
	}
}