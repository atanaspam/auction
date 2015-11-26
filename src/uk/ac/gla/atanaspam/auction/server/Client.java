package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystemClient;

/**
 * This class represents a single client's details within the Auction Server.
 * @author atanaspam
 * @created 02/11/2015
 */
public class Client implements java.io.Serializable{


    private int id;
    AuctionSystemClient client;

    public Client(int id, AuctionSystemClient client) {
        this.client = client;
        this.id = id;
    }

    public Client(int id) {
        this.client = null;
        this.id = id;
    }

    public void setClient(AuctionSystemClient client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Client: " +
                id + " ";
    }

}
