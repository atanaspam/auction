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
            long startTime = 0;
            long elapsedTime = 0;
            switch (args[0]){
                case "1":{
                    startTime = System.currentTimeMillis();
                    int n = 1;
                    for (int i = 0; i<10000; i++){
                        client.createNewAuction("auction "+i, d, 100);
                    }
                    break;
                }
                case "2":{
                    startTime = System.currentTimeMillis();
                    int n = client.createNewAuction("auction",d, 100);
                    startTime = System.currentTimeMillis();
                    for (int i = 0; i<10000; i++){
                        client.bid(n,i);
                    }
                    break;
                }
                case "3":{
                    startTime = System.currentTimeMillis();
                    Thread t1 = testCreate(d, 5000);
                    Thread t2 = testCreate(d, 5000);
                    t1.start();
                    t2.start();
                    t2.join();
                    break;
                }
                case "4":{
                    startTime = System.currentTimeMillis();
                    Thread t1 = testBid(d, 5000);
                    Thread t2 = testBid(d, 5000);
                    t1.start();
                    t2.start();
                    t2.join();
                    break;
                }

                case "5":{
                    startTime = System.currentTimeMillis();
                    Thread t1 = testCreate(d, 5000);
                    Thread t2 = testBid(d, 5000);
                    t1.start();
                    t2.start();
                    t2.join();
                    break;
                }

            }

            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.format("10000 calls in %d ms - %d.%03d ms/call\n",
                    elapsedTime, elapsedTime/10000, (elapsedTime%10000)/10);
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Cannot create acution...");
        }


    }
    public static Thread testCreate(Date d, int iterations) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                AuctionSystemClientImpl client;
                try {
                    client = new AuctionSystemClientImpl();
                    for (int i = 0; i<iterations; i++){
                        client.createNewAuction("auction "+i, d, 100);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }

    public static Thread testBid(Date d, int iterations) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                AuctionSystemClientImpl client;
                try {
                    client = new AuctionSystemClientImpl();
                    int n = client.createNewAuction("auction",d, 100);
                    for (int i = 0; i<iterations; i++){
                        client.bid(n,i);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return t;
    }
}
