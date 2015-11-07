package uk.ac.gla.atanaspam.auction.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author atanaspam
 * @version 0.1
 * @created 03/11/2015
 */
public class AuctionPool{

    private ArrayList<Auction> auctions;
    private ArrayList<Auction> finished;
    private AuctionPoolMonitor monitor;
    private int nextId;

    public AuctionPool(){
        this.finished = new ArrayList<>();
        this.auctions = new ArrayList<>();
        this.nextId=1;
        this.monitor = new AuctionPoolMonitor(this);
        monitor.start();
    }

    public void addAuction(Auction a){
        auctions.add(a);
        if (a.getId() > this.nextId)
            nextId = a.getId()+1;
    }

    public void addAuction(Date beginTime, Date endTime, int price, Client winner, Client owner, boolean finished){
        auctions.add(new Auction(nextId++,beginTime, endTime, price, winner, owner, finished));
    }

    public void addAuction(int id, Date beginTime, Date endTime, int price, Client winner, Client owner, boolean finished){
        auctions.add(new Auction(id,beginTime, endTime, price, winner, owner, finished));
        nextId = id;
    }

    public int addAuction(Date endDate, int price, Client owner){
        Auction a = new Auction(nextId++, new Date(), endDate, price, null, owner, false);
        auctions.add(a);
        return a.getId();
    }



    public Auction getAuction(int id){
        for(Auction a : auctions){
            if (a.getId() == id){
                return a;
            }
        }
        return null;
    }

    public Auction getAllAuctions(int id){
        ArrayList<Auction> all = new ArrayList<>();
        all.addAll(auctions);
        all.addAll(finished);
        for(Auction a : all){
            if (a.getId() == id){
                return a;
            }
        }
        return null;
    }

    public boolean exists(int id){
        for(Auction a : auctions){
            if (a.getId() == id){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Auction> getAuctions(){
        return this.auctions;
    }

    public synchronized boolean terminateAuction(int id){
        for(Auction a : auctions){
            if (a.getId() == id){
                a.setFinished(true);
                finished.add(a);
                auctions.remove(a);
                return true;
            }
        }
        return false;
    }

    public void writeToFile(String file){
        try {
            FileOutputStream fileOut = new FileOutputStream("auctions.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.reset();
            out.writeObject(auctions);
            out.close();
            fileOut.close();
        }catch (IOException e) {
            System.out.println("Error when writing to file");
        }
    }

    public void addFinished(Auction a){
        this.finished.add(a);
    }


}
