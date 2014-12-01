package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;



public class SearchServiceDHT {

 
   
   private static List<Number160> myCandidates = new ArrayList<Number160>();
   private static Number160 domainKey = Number160.createHash("ITEMS");
   
   public static List<Number160> findReference(Peer peer,String index) throws ClassNotFoundException, IOException{
       
       int counterRef = 0;
       Number160 keyKeyword = Number160.createHash(index);
       Number160 myHash;
       FutureDHT futureGet = peer.get(keyKeyword).setDomainKey(domainKey).setAll().start();
       futureGet.awaitUninterruptibly();
       List<Number160> myReferences = new ArrayList<Number160>();
       List<Number160> myReferencesDev = new ArrayList<Number160>();
       Object o;
       
       if(futureGet.isSuccess()){
        
         Iterator<Data> iteratorRef = futureGet.getDataMap().values().iterator();
        
         while(counterRef < futureGet.getDataMap().size()){
                  o = iteratorRef.next().getObject();
                
                  if(o.getClass().equals(keyKeyword.getClass())){
                    
                        myHash = (Number160)o;
                        myReferences.add(myHash);
                       
                       
                } else {
                                        
                    myReferences.add(keyKeyword);
                 
                }
             counterRef++;
             
         }
       }
     
       for(Number160 n : myReferences){
          if( myReferencesDev.contains(n))
              continue;
          else
              myReferencesDev.add(n);
          
       }   
       
      return myReferencesDev;
   }
   
   public static List<ItemSimple> search(Peer myPeer, List<Number160> references) throws ClassNotFoundException, IOException{ 
       
       int counterItem = 0;
       List<Number160> search = new ArrayList<Number160>();
       search = references;
       
       List<ItemSimple> items = new ArrayList<ItemSimple>();
       Object o;
       
        for(Number160 number: search){
            
            FutureDHT futureGet = myPeer.get(number).setDomainKey(domainKey).setAll().start().awaitUninterruptibly();
      
            if(futureGet.isSuccess()){         
               
               Iterator<Data> iteratorItem = futureGet.getDataMap().values().iterator();
              
               while(counterItem < futureGet.getDataMap().size()){
                 
                   o = iteratorItem.next().getObject();
                   
                   if(!(o.getClass().equals(number.getClass()))){
                       
                       
                       items.add((ItemSimple)o);
                   
                   }
                   counterItem++;
               }
              counterItem = 0;
            }
        } 
        return items;
    }  
           
   
     public static List<Item> searchItem(Peer myPeer, List<ItemSimple> itemSimples) throws ClassNotFoundException, IOException{
         
        List<Item> items = new ArrayList<Item>();
        Item item;
        
        for(ItemSimple i:itemSimples){
            
            Number160 keyWord = Number160.createHash(i.getIdItem());
             
            FutureDHT futureGet = myPeer.get(keyWord).setDomainKey(domainKey).start().awaitUninterruptibly(); 
             
            if(futureGet.isSuccess()){
               
               item = (Item)futureGet.getData().getObject();
               items.add(item);
            }
        } 
        return items;
         
     }
   
   
   
   
    public static List<ItemSimple> booleanSearch(Peer myPeer,List<String> myOperators,List<String> myOperands,List<String> myQuery) throws ClassNotFoundException, IOException{
                  
             int numOperators = myOperators.size();
             int numOperands = myOperands.size();
             List<ItemSimple> theOnes = new ArrayList<ItemSimple>();
           
             String myOperator = "";
             List<Number160> mySearch;
             List<List<Number160>> mySearches = new ArrayList<List<Number160>>();
             List<List<Number160>> myNotOperands = new ArrayList<List<Number160>>();
             List<String> notOperands = new ArrayList<String>();
            
             
             if(myOperators.get(0).equals("not"))
                 System.out.println("Failure!!!");
             
             for(int k=0;k<myQuery.size();k++){
                 
                 if(myQuery.get(k).equals("not")){
                     notOperands.add(myQuery.get(k+1));
                     myOperators.remove("not");
                 }
                 
             }
             
             if(myOperands.size() - myOperators.size() != 1)
                 System.out.println("Query Rejected!!!");
                 
             for(int j=0;j<numOperands;j++){
                 
                 mySearch = new ArrayList<Number160>();
                 mySearch = findReference(myPeer,myOperands.get(j));
                 
                 if(notOperands.contains(myOperands.get(j))){
                     myNotOperands.add(mySearch);
                     notOperands.remove(myOperands.get(j));
                 }
                 
                 mySearches.add(mySearch);
                 
             }
             
             
          myCandidates = mySearches.get(0);
             
            for(int i=myOperators.size() -1, m=1 ;i>=0;i-- , m++){
          
                 myOperator = myOperators.get(i);
                 
                 if(numOperands > 1)
                    theChosenOnes(myOperator,mySearches.get(m),myNotOperands);
                 else
                     System.out.println("Not enough arguments - Exception");
                 
            } 
             
            if(myCandidates.isEmpty())
                System.out.println("No results found - Exception");
               
           theOnes = search(myPeer,myCandidates);   
           
           return theOnes;
            
     }
        
    
    public static void theChosenOnes(String myOperator, List<Number160> secondSearch, List<List<Number160>> myNotOperands){
                
        
        if(myOperator.equals("and")){
           
            for(List<Number160> list : myNotOperands){
                
                if(list.containsAll(secondSearch)){
                    
                   myCandidates.removeAll(secondSearch);
                   myNotOperands.remove(list);
                return;
                }
            }
           myCandidates.retainAll(secondSearch);
            
        }
        
        if(myOperator.equals("or")){
                
               for(List<Number160> list : myNotOperands)
                   if(list.equals(secondSearch)){
                       System.out.println("throw exception!!!! - or");
                       return;   
                   }
            
                for(Number160 hash : secondSearch){
                  
                    if(!(myCandidates.contains(hash)))
                        myCandidates.add(hash);
                    
                }
            }
        
        
      }
    
   public static void clearMySearch(){
       
       myCandidates.clear();
   }
   
   public static void listItems(List<Item> items) {
		int i = 1;
      System.out.println("****************** Results *********************");
      System.out.println("************************************************");
      
      for (Item item : items){
        
          if(!item.getSold()){  
      	   System.out.println(i+" - Name: " + item.getName() + " Dealer: " + item.getDealer());
            i++;
        } 
      }
      System.out.println("************************************************");
      System.out.println("Press 0 to go back or press the item number to see details and bid");
   }
 }
        
    
    
    

