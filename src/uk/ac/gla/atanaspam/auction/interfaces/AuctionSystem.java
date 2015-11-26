package uk.ac.gla.atanaspam.auction.interfaces;

import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystemClient;

import java.util.ArrayList;
import java.util.Date;

/**
 * This interface defines the methods exported by the AuctionSystemServer to remote clients
 * @author atanaspam
 * @created 02/11/2015
 */
public interface AuctionSystem extends java.rmi.Remote {

    public int bid (int auctionId, int bidAmount, int clientId) throws java.rmi.RemoteException;
    public int createNewAuction(String name, Date endTime, int startingPrice, int clientId) throws java.rmi.RemoteException;
    public ArrayList<Integer> getAuctionList(int clientId) throws java.rmi.RemoteException;
    public int register(AuctionSystemClient client) throws java.rmi.RemoteException;
    public boolean login(int clientId, AuctionSystemClient client) throws java.rmi.RemoteException;
    public String getAuctionInfo(int auctionId) throws java.rmi.RemoteException;
}
