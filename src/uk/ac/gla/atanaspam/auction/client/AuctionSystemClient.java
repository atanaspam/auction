package uk.ac.gla.atanaspam.auction.client;

/**
 * @author atanaspam
 * @version 0.1
 * @created 04/11/2015
 */
public interface AuctionSystemClient extends java.rmi.Remote{

    public boolean sendNotification(String s) throws java.rmi.RemoteException;
    public boolean ping() throws java.rmi.RemoteException;
}
