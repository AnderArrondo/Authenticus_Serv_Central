package es.deusto.sd.authenticus_serv_central.external;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import es.deusto.sd.authenticus_serv_central.dto.ArchImagenDTO;
import es.deusto.sd.authenticus_serv_central.dto.RequestProcesaDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.RequestProcesa;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

public class SocketProcesaClient {
    private String serverIP;
	private int serverPort;
	Socket socket;

	ObjectInputStream in;
	ObjectOutputStream out;
    
	public SocketProcesaClient(String servIP, int servPort) {
		serverIP = servIP;
		serverPort = servPort;
		try {
			socket = new Socket(serverIP, serverPort);

			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
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
            out.writeObject(requestDTO);
			out.flush();

			ArchImagenDTO resultDTO = (ArchImagenDTO) in.readObject();
			return resultDTO;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("# SocketProcesaClient - enviarRequestProcesa IO/ClassNotFound error: " + e.getMessage());
        }
        return null;
    }
}
