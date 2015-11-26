package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystem;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * @author atanaspam
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
        System.out.println("Available commands: help, load <filename>, save ");
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
                            System.out.println("Unable to find file...");
                        else
                            System.out.println("Success.");
                        break;
                    } catch (IOException e) {
                        System.out.println("An error has occurred.");
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
                case "help": {
                    System.out.println("load <filename>             -To load auctions from a file");
                    System.out.println("save                        -To save auctions to a file.");
                    System.out.println("help                        -To see a list of available commands.");
                }
                default:{
                    System.out.println("Type help to see a list of available commands.");
                }
            }
        }
    }
}
