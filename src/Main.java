import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        var port = 8081;// порт может быть любой в доступном диапазоне 0-65536.
        // Чтобы не взять уже занятый - лучше использовать около 8080
        ServerSocket serverSocket = new ServerSocket(port);
        var executor = Executors.newFixedThreadPool(500);

        System.out.println("Server started");

        try {
            while (true) { //ждет подключения
                Socket socket = serverSocket.accept();
                executor.submit(() -> handle(socket));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handle(Socket socket) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                //ответ
                out.println("Response: " + line + " serverTime = " + System.currentTimeMillis()
                        + " clientHost = " + socket.getInetAddress().toString() + " clientPort = " + socket.getPort());

                //выход
                if (line.equals("end")) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

