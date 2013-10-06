package dbDataObjects;

import java.util.Set;

import containerDbAccess.ContainerAccess;

public class Client {
    private int idClients;
    private String lastName;
    private String firstName;
    private String login;
    private String password;
    private String address;
    private String phoneNumber;
    private String email;

    public int getId() {
    	return idClients;
    }
    
    public Client(String login) {
    	this("", "", login, "", "", "", "");
    }
    public Client(String login, String password) {
    	this("", "", login, password, "", "", "");
    }

    public Client(String lastName, String firstName, String login, String password,
                String address, String phoneNumber, String email){
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public boolean isAuthorized() {
    	ContainerAccess db = ContainerAccess.getInstance();
    	Set<String> clientLists = db.getClients();
    	if (clientLists.contains(this.login)) {
    		return true;
    	} else {
    		return false;
    	}
    }

}
