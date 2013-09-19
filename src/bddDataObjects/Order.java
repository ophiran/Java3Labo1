/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bddDataObjects;

import java.util.Date;

/**
 *
 * @author mike
 */
public class Order {
    private int id;
    private Date date;
    private int refClient;
    private PartsType pieceType;
    private int numberPieces;
    
    public Order(int id, Date date, int refClient, PartsType pieceType,
            int numberPieces){
        this.id = id;
        this.date = date;
        this.refClient = refClient;
        this.pieceType = pieceType;
        this.numberPieces = numberPieces;
    }
}
