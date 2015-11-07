package uk.ac.gla.atanaspam.auction.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * @author atanaspam
 * @version 0.1
 * @created 02/11/2015
 */
public class AuctionSystemServer {

    public AuctionSystemServer(){
        try {
            LocateRegistry.createRegistry(1099);
            AuctionSystem a = new AuctionSystemImpl("auctions.txt", "clients.txt");
            Naming.rebind("rmi://localhost/AuctionSystemService", a);
            System.out.println("Server Listening for connections...");
        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String args[]) {
        new AuctionSystemServer();
    }

}
