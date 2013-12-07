/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;


/**
 *
 * @author mike
 */
public class LoginRequest{
    public String login;
    public String password;
    
    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public LoginRequest(String data) {
        String vectStr[] = data.split("#");
        this.login = vectStr[1];
        this.password = vectStr[2];
    }
    
    public String networkString() {
        String toRet;
        toRet = "logina#" + this.login + "#" + this.password + "\r\n";
        return toRet;
    }
    
}
