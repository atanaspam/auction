package uk.ac.gla.atanaspam.auction.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author atanaspam
 * @version 0.1
 * @created 02/11/2015
 */
public class Auction implements Serializable{

    //private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.ENGLISH);

    private int id;
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

    public Auction(int id, Date begintime, Date endtime, int currentPrice, Client winner, Client owner, boolean finished) {
        this.id = id;
        this.endtime = endtime;
        this.begintime = begintime;
        this.currentPrice = currentPrice;
        this.winner = winner;
        this.owner = owner;
        this.finished = finished;
        this.bidders = new ArrayList<>();
    }

    public Auction(int id, Date begintime, Date endtime, int currentPrice, Client owner) {
        this.id = id;
        this.begintime = begintime;
        this.endtime = endtime;
        this.currentPrice = currentPrice;
        this.owner = owner;
        this.finished = false;
        this.bidders = new ArrayList<>();
    }

    public synchronized int bid(int newPrice, Client bidder){
        if (this.getEndtime().compareTo(new Date()) > 0) {
            if (this.getCurrentPrice() < newPrice) {
                this.setCurrentPrice(newPrice);

                this.setWinner(bidder);
                System.out.println("Auction " + this.getId() +
                        " has new winnig price: " + newPrice +
                        " from " + this.getWinner());

                return 0;
            }else{
                return -2;
            }
        }else {
            return -1;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Client getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "id: " + id +
                ", begintime: " + begintime +
                ", endtime: " + endtime +
                ", currentPrice: " + currentPrice +
                ", winner: " + winner +
                ", owner: " + owner;
    }
}
