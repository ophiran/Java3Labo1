/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

/**
 *
 * @author mike
 */
public class LoginRequest implements Request{
    public String login;
    public String password;
    
    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
