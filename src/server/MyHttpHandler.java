package server;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JSpinner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import segheria.Segheria;


class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;
        String route = null;

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
            route = getRequestRoute(httpExchange);
        }
        
        handleResponse(route, httpExchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
    }
    
    private String getRequestRoute(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().split("/")[1].split("\\?")[0];
    }

    private void handleResponse(String route, HttpExchange httpExchange, String requestParamValue) throws IOException {
    	try {
    		OutputStream outputStream = httpExchange.getResponseBody();
    		StringBuilder htmlBuilder = new StringBuilder();
    		
    		if(route.equals("taglio")) {
    			int durataSecondi = Integer.valueOf(requestParamValue);
    			Segheria.tagliManager.newTaglio(durataSecondi);        	
    			Segheria.refreshDock();
    			Segheria.resetDock();
    			Segheria.checkWarnings();
    		}
    		
    		if(route.equals("temperatura")) {
    			Double gradi = Double.valueOf(requestParamValue);
    			Segheria.lblSessionTemperatura.setText(String.valueOf(gradi) + " °C");
    		}
    		
    		String htmlResponse = htmlBuilder.toString();
    		httpExchange.sendResponseHeaders(200, htmlResponse.length());
    		outputStream.write(htmlResponse.getBytes());
    		outputStream.flush();
    		outputStream.close();
    		
    	} catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
}