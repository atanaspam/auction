package uk.ac.gla.atanaspam.auction.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * This class manages all the auctions in the system. The AuctionSystem could have been implemented without a separate
 * class for this but I find it better to split this functionality in a different class.
 * @author atanaspam
 * @created 03/11/2015
 */
public class AuctionPool{

    private ArrayList<Auction> auctions;
    private ArrayList<Auction> finished;
    /** @see AuctionPoolMonitor  */
    private AuctionPoolMonitor monitor;
    private int nextId;

    public AuctionPool(){
        this.finished = new ArrayList<>();
        this.auctions = new ArrayList<>();
        this.nextId=1;
        this.monitor = new AuctionPoolMonitor(this);
        monitor.start();
    }

    public ArrayList<Auction> getAuctions(){
        return this.auctions;
    }

    /**
     * This method when Auctions are imported from a file.
     * It also has to take care that no auction is imported with an already existing Auction ID.
     * @param a the Auction object to be added.
     */
    public void addAuction(Auction a){
        auctions.add(a);
        if (a.getId() > this.nextId)
            nextId = a.getId()+1;
        else{
            a.setId(nextId++);
        }
    }

    public ArrayList<Integer> getAuctionIDs(){
        ArrayList<Integer> ids = new ArrayList<>();
        for (Auction a : auctions) {
            ids.add(a.getId());
        }
        return ids;
    }

    /**
     *@deprecated
     */
    public void addAuction(String name, Date beginTime, Date endTime, int price, Client winner, Client owner, boolean finished){
        auctions.add(new Auction(nextId++, name, beginTime, endTime, price, winner, owner, finished));
    }

    /**
     *@deprecated
     */
    public void addAuction(int id, String name, Date beginTime, Date endTime, int price, Client winner, Client owner, boolean finished){
        auctions.add(new Auction(id, name, beginTime, endTime, price, winner, owner, finished));
        nextId = id;
    }

    /**
     * This method is called by the Auction System Implementation
     */
    public synchronized int addAuction(String name, Date endDate, int price, Client owner){
        Auction a = new Auction(nextId++, name, new Date(), endDate, price, null, owner, false);
        auctions.add(a);
        return a.getId();
    }


    /**
     * This method searches the currently active auctions for a specific auction.
     * @param id The ID of the auction we need.
     * @return The Auction or null if it doesnt exist.
     */
    public Auction getAuction(int id){
        ArrayList<Auction> auctions =new ArrayList<>();
        for(int i = 0; i< auctions.size(); i++){
            Auction a = auctions.get(i);
            if (a.getId() == id){
                return a;
            }
        }
        return null;
    }

    /**
     * This method traverses all the auctions (Finished and Not-finished)
     * @param id The ID of the auction we need.
     * @return The Auction or null if it doesnt exist.
     */
    public Auction getFromAllAuctions(int id){
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
        for(Auction a : this.getAuctions()){
            if (a.getId() == id){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean terminateAuction(int id){
        Iterator<Auction> iter = this.getAuctions().iterator();

        while (iter.hasNext()) {
            Auction a = iter.next();
            if (a.getId() == id) {
                finished.add(a);
                iter.remove();
                return true;
            }
//
        }
        return false;
    }

    /**
     * This method is called by the Auction System Implementation to write the currently active auctions to a file
     * @param file The name/path of the file
     * @return True if the write succeed, False otherwise.
     */
    public boolean writeToFile(String file){
        try {
            FileOutputStream fileOut = new FileOutputStream("auctions.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.reset();
            out.writeObject(auctions);
            out.close();
            fileOut.close();
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Add an auction to the list of finished auctions.
     * @param a The auction
     */
    public void addFinished(Auction a){
        this.finished.add(a);
    }


}
