package main.java.pt.ist.p2p;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSimple implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description;
    private String name;
    private ArrayList<Bid> allBidders = new ArrayList<Bid>();
    private String dealer;
    private boolean sold;

    public ItemSimple(){

    }


    public boolean getSold(){
        return this.sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Bid> getAllBidders() {
        return allBidders;
    }

    public void setBidder(Bid bid){
        allBidders.add(bid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}