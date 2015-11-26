package uk.ac.gla.atanaspam.auction.interfaces;

/**
 * This interface defines the methods available to the Auction System Server for invocation.
 * @author atanaspam
 * @created 04/11/2015
 */
public interface AuctionSystemClient extends java.rmi.Remote{

    public boolean sendNotification(String s) throws java.rmi.RemoteException;
    public boolean ping() throws java.rmi.RemoteException;
}
