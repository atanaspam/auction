package uk.ac.gla.atanaspam.auction.client;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date d = null;
        try {
            d = sdf.parse("18-Nov-2016 18:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            int n = 1;// client.createNewAuction(d, 100);
            long startTime = System.currentTimeMillis();
            for (int i = 202; i<10202; i++){
                client.bid(n,i);
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.format("10000 calls in %d ms - %d.%03d ms/call\n",
                    elapsedTime, elapsedTime/10000, (elapsedTime%10000)/10);
        } catch (Exception e) {
            System.out.println("Cannot create acution...");
        }
    }
}
