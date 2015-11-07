package uk.ac.gla.atanaspam.auction.server;

/**
 * @author atanaspam
 * @version 0.1
 * @created 03/11/2015
 */
public class NoSuchClientException extends Exception {
    public NoSuchClientException(int id) {
        System.out.println("No client exists with id: " + id);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
