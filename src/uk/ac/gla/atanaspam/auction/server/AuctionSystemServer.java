package uk.ac.gla.atanaspam.auction.server;

import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * @author atanaspam
 * @version 0.1
 * @created 02/11/2015
 */
public class AuctionSystemServer {

    AuctionSystem a;
    public AuctionSystemServer(){
        try {
            LocateRegistry.createRegistry(1099);
            a = new AuctionSystemImpl();
            Naming.rebind("rmi://localhost/AuctionSystemService", a);
            //Naming.rebind("rmi://130.209.245.90/AuctionSystemService", a);
            System.out.println("Server Listening for connections...");
        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String args[]) {
        AuctionSystemServer as = new AuctionSystemServer();
        AuctionSystemImpl asi = (AuctionSystemImpl) as.a;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String[] command = scanner.nextLine().split(" ");
            if (command[0].equals("quit") || command[0].equals("exit")) {
                scanner.close();
                System.exit(0);
            }
            switch (command[0]) {
                case "load": {
                    try {
                        String filename = command[1];
                        if (!asi.readAuctionsFromFile(filename))
                            System.out.println("An error has occurred.");
                        break;
                    } catch (FileNotFoundException e) {
                        System.out.println("Unable to find file...");
                    } catch (Exception e) {
                        System.out.println("Usage: load <filename>");
                    }
                }
                case "save": {
                    if (asi.writeAuctionsToFile()) {
                        System.out.println("Saved current auctions to auctions.txt");
                        break;
                    }
                    else
                        System.out.println("An error has occured while trying to save to file.");
                }
            }
        }
    }
}
