/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;


import com.fazecast.jSerialComm.SerialPort;
import com.karaikacho.proyectofinal.MarcarController;
import java.io.IOException;
import java.util.Scanner;

import java.io.PrintWriter;

/**
 *
 * @author thotstin
 */
public class ArduinoLectorHuellas {
    public static SerialPort port;
    public static void marcar(int id){
        
    }
    
    public static void init() {
        // List available ports
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available ports:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println((i + 1) + ": " + ports[i].getSystemPortName());
        }

        // Choose the port (for example, COM3 on Windows, /dev/ttyUSB0 on Linux)
        Scanner input = new Scanner(System.in);
        System.out.print("Enter port number: ");
        int chosenPort = input.nextInt();

        port = ports[chosenPort - 1];

        port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 1000, 0);

        if (!port.openPort()) {
            System.out.println("❌ Could not open port.");
            return;
        }

        System.out.println("✅ Connected to Arduino Uno. Listening... (type 'q' to quit)");

        // Algo asi es para escribir al arduino
        PrintWriter writer = new PrintWriter(port.getOutputStream());

        Thread reader = new Thread(() -> {
            try (Scanner scanner = new Scanner(port.getInputStream())) {
                String c;
                int id;
                while (!Thread.currentThread().isInterrupted()) {
                    if (scanner.hasNextLine()) {
                        c = scanner.nextLine();
                        if (0 == c.length()) {
                            continue;
                        }
                        /* Algoritmo: Leer y escribir (como echo) hasta que se lea 'x', hacer parseint y marcar con ese id, loop*/
                        if ('x' != c.charAt(0)) {
                            System.out.println(c);
                        } else {
                            id = scanner.nextInt();
                            MarcarController.marcarId(id);
                        }
                    }
                }
            }
        });
        reader.start();
        
        /*
        port.getOutputStream().write((cmd + "\n").getBytes());
        port.getOutputStream().flush();
        */
    }
}
