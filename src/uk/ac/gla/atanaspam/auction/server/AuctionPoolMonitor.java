package uk.ac.gla.atanaspam.auction.server;

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
                    //System.out.println(pool.getAuctions());
                    pool.terminateAuction(a.getId());
                    i--; // if we remove an auction we go one index back as the whole arraylist gets shifted and we may miss an ended auction.
                    try {
                        if (a.getWinner() == null){
                            a.getOwner().client.sendNotification("Your auction "+ a.getId() + " finished without winners.");
                        }else {
                            a.getOwner().client.sendNotification(a.getWinner().getId() + " has won Auction " + a.getId()
                                    + " for " + a.getCurrentPrice());
                            a.getWinner().client.sendNotification("You have won Auction " + a.getId() + " for "+ a.getCurrentPrice());
                            for (Client c : a.getBidders())
                                c.client.sendNotification("You have lost Auction " + a.getId() + ". Winning price: "+ a.getCurrentPrice());
                        }
                    }catch (java.rmi.ConnectException e){
                        //System.out.println("Client has disconnected or cannot be reached");
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }catch (NullPointerException e) {
                        System.out.println("Client does not exist (auction is manually imported into the server.)");
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
