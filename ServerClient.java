import java.io.*;
import java.net.*;

// Kelas utama yang menggabungkan server dan client
public class ServerClient {

    // Kelas untuk menjalankan Server
    static class ServerThread extends Thread {
        public void run() {
            try {
                // Membuat server socket di port 12345
                ServerSocket serverSocket = new ServerSocket(12345);
                System.out.println("Server siap dan menunggu koneksi dari client...");

                // Menerima koneksi dari client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client terhubung!");

                // Membuat input dan output stream untuk komunikasi dengan client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Percakapan antara server dan client
                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Client: " + clientMessage);

                    if (clientMessage.equalsIgnoreCase("keluar")) {
                        System.out.println("Client telah keluar.");
                        break;
                    }

                    // Server mengirim balasan ke client
                    out.println("Server menerima: " + clientMessage);
                }

                // Tutup koneksi
                clientSocket.close();
                serverSocket.close();
                System.out.println("Server ditutup.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Kelas untuk menjalankan Client
    static class ClientThread extends Thread {
        public void run() {
            try {
                // Tunggu sebentar sebelum client terhubung
                Thread.sleep(2000); // Tunggu 2 detik agar server siap

                // Terhubung ke server di localhost dan port 12345
                Socket socket = new Socket("localhost", 12345);
                System.out.println("Terhubung ke server!");

                // Membuat input dan output stream untuk komunikasi dengan server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                // Percakapan dengan server
                String message;
                while (true) {
                    // Input dari pengguna
                    System.out.print("Anda: ");
                    message = userInput.readLine();

                    // Kirim pesan ke server
                    out.println(message);

                    if (message.equalsIgnoreCase("keluar")) {
                        break;  // Keluar dari loop jika pengguna mengetik 'keluar'
                    }

                    // Menerima respons dari server
                    String response = in.readLine();
                    System.out.println(response);
                }

                // Tutup koneksi
                socket.close();
                System.out.println("Koneksi client ditutup.");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Menjalankan server di thread terpisah
        ServerThread serverThread = new ServerThread();
        serverThread.start();

        // Menjalankan client di thread terpisah
        ClientThread clientThread = new ClientThread();
        clientThread.start();
    }
}
