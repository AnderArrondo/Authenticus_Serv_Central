package es.deusto.sd.authenticus_serv_central.external;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.authenticus_serv_central.dto.ArchImagenDTO;
import es.deusto.sd.authenticus_serv_central.dto.RequestProcesaDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.RequestProcesa;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

public class SocketProcesaClient {
    private String serverIP;
	private int serverPort;
	private Socket socket;
	private ObjectMapper mapper = new ObjectMapper();

    private BufferedReader in;
    private BufferedWriter out;
    
	public SocketProcesaClient(String servIP, int servPort) {
		serverIP = servIP;
		serverPort = servPort;
		try {
			socket = new Socket(serverIP, serverPort);

			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(" - TranslationClient: sever connection accepted.");
		} catch (UnknownHostException e) {
			System.err.println(" # Trans. SocketClient: Socket error: " + e.getMessage());
		} catch (EOFException e) {
			System.err.println(" # Trans. SocketClient: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println(" # Trans. SocketClient: IO error: " + e.getMessage());
		}
	}

    public ArchImagenDTO enviarRequestProcesa(ArchImagen img, TipoExp tipoExp) {
        RequestProcesa request = new RequestProcesa(img, tipoExp);
        RequestProcesaDTO requestDTO = new RequestProcesaDTO(
            new ArchImagenDTO(request.getArchImagen().getNombre(), request.getArchImagen().getPath()),
            request.getTipoExp().name()
        );

        try {
            String jsonRequest = mapper.writeValueAsString(requestDTO);

			out.write(jsonRequest);
			out.newLine();
			out.flush();

			String jsonResponse = in.readLine();
			
			if(jsonResponse != null) {
				return mapper.readValue(jsonResponse, ArchImagenDTO.class);
			}
        } catch (IOException  e) {
            System.err.println("# SocketProcesaClient - enviarRequestProcesa IO/ClassNotFound error: " + e.getMessage());
        }
        return null;
    }

	public void closeConnection() {
		try {
			if (socket != null) {
				socket.close();
			}
			if( in != null) {
				in.close();
			}
			if( out != null) {
				out.close();
			}
		} catch (IOException e) {
			System.err.println(" # SocketProcesaClient: closeConnection IO error: " + e.getMessage());
		}
	}
}
