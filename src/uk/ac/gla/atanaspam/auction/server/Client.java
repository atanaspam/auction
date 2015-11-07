package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.client.AuctionSystemClient;

/**
 * @author atanaspam
 * @version 0.1
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
