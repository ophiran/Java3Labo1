/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bddDataObjects;

/**
 *
 * @author mike
 */
public class Client {
    private String name;
    private int id;
    private String password;
    private static int clientNumber = 0;

    
    public int getId(){
        return this.id;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Client(String name, String password){
        clientNumber++;
        this.name = name;
        this.password = password;
        this.id = clientNumber;
    }
}
