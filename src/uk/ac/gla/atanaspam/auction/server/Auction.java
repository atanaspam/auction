package uk.ac.gla.atanaspam.auction.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents a single auction in the AuctionSystem.
 * @author atanaspam
 * @created 02/11/2015
 */
public class Auction implements Serializable{

    private int id;
    private String name;
    private Date begintime;
    private Date endtime;
    private int currentPrice;
    private Client winner;
    private Client owner;
    private boolean finished;
    private ArrayList<Client> bidders;

    public int getId() {
        return id;
    }

    public Date getBegintime() {
        return begintime;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public Date getEndtime() {
        return endtime;
    }

    public Client getWinner() {
        return winner;
    }

    public ArrayList<Client> getBidders() {
        return bidders;
    }
    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setWinner(Client winner) {
        if (!bidders.contains(winner)){
           bidders.add(winner);
        }
        this.winner = winner;

    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Client getOwner() {
        return owner;
    }

    /**
     * A raw constructor
     */
    public Auction(int id,String name, Date begintime, Date endtime, int currentPrice, Client winner, Client owner, boolean finished) {
        this.id = id;
        this.name = name;
        this.endtime = endtime;
        this.begintime = begintime;
        this.currentPrice = currentPrice;
        this.winner = winner;
        this.owner = owner;
        this.finished = finished;
        this.bidders = new ArrayList<>();
    }

    /**
     * A simpler constructor used by import methods
     * @deprecated
     */
    public Auction(int id, Date begintime, Date endtime, int currentPrice, Client owner) {
        this.id = id;
        this.begintime = begintime;
        this.endtime = endtime;
        this.currentPrice = currentPrice;
        this.owner = owner;
        this.finished = false;
        this.bidders = new ArrayList<>();
    }

    /**
     * This method is called by the AuctionPool when a client wants to bid on this auction.
     * @param newPrice The new price to be set
     * @param bidder The Client instance for that bidder.
     * @return 0 if bid is successfull, -1 if auction has already ended, -2 if the new price is lower than the current.
     */
    public synchronized int bid(int newPrice, Client bidder){
        if (this.getEndtime().compareTo(new Date()) > 0) {
            if (this.getCurrentPrice() < newPrice) {
                this.setCurrentPrice(newPrice);
                this.setWinner(bidder);
                //TODO log instead of print
                System.out.println("Auction " + this.getId() +
                        " has new winnig price: " + newPrice +
                        " from " + this.getWinner());
                return 0;
            }else{ return -2; }
        }else { return -1; }
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Standard String representation used for debugging.
     */
    @Override
    public String toString() {
        return "id: " + id +
                ", name: " + name +
                ", begintime: " + begintime +
                ", endtime: " + endtime +
                ", currentPrice: " + currentPrice +
                ", winner: " + winner +
                ", owner: " + owner;
    }
}
