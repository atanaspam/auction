package uk.ac.gla.atanaspam.auction.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author atanaspam
 * @version 0.1
 * @created 03/11/2015
 */
public class AuctionPoolMonitor extends Thread{

    AuctionPool pool;
    public AuctionPoolMonitor(AuctionPool p){
        this.pool = p;
    }
    private Thread t;

    public void run(){
        while (true) {
            ArrayList<Auction> auctions = pool.getAuctions();
            for (int i = 0; i<auctions.size(); i++){
                Auction a = auctions.get(i);
                if(a.getEndtime().compareTo(new Date())<0){
                    pool.terminateAuction(a.getId());
                    try {
                        a.getWinner().client.sendNotification("You have won Auction " + a.getId() + " for "+ a.getCurrentPrice());
                        a.getOwner().client.sendNotification(a.getWinner().getId() + " has won Auction " + a.getId()
                                + " for "+ a.getCurrentPrice());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }catch (NullPointerException e) {
                        System.out.println("Client cannot be reached");
                        //System.out.println("Auction " + a.getId()+ " is now finished:"+ a.isFinished());
                    }
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    public void start(){
        System.out.println("Starting Auction Monitor" );
        if (t == null)
        {
            t = new Thread (this, "Auction Monitor");
            t.start ();
        }

    }

}
