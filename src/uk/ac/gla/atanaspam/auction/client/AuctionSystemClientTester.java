package uk.ac.gla.atanaspam.auction.client;

import java.rmi.RemoteException;
import java.util.Date;

/**
 * @author atanaspam
 * @version 0.1
 * @created 12/11/2015
 */
public class AuctionSystemClientTester {

    public static void main (String[] args){
        AuctionSystemClientImpl client = null;
        try {
            client = new AuctionSystemClientImpl();

        } catch (RemoteException e) {
            System.out.println("Unable to connect to server...");
        }
        Date d = new Date();
        try {
            client.createNewAuction(d, 100);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i<10000; i++){
                client.createNewAuction(d, 100);
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.format("10000 calls in %d ms - %d.%03d ms/call\n",
                    elapsedTime, elapsedTime/10000, (elapsedTime%10000)/10);
        } catch (RemoteException e) {
            System.out.println("Cannot create acution...");
        }
    }
}
