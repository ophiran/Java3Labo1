package dbDataObjects;

import java.util.Set;

import containerDbAccess.ContainerAccess;

public class Client {
    private int idClients;
    private String lastName;
    private String firstName;
    public String login;
    private String password;
    private String address;
    private String phoneNumber;
    private String email;

    public int getId() {
    	return idClients;
    }
    
    public Client(String login) {
    	this(0,"", "", login, "", "", "", "");
    }
    public Client(String login, String password) {
    	this(0,"", "", login, password, "", "", "");
    }

    public Client(int id, String lastName, String firstName, String login, 
            String password, String address, String phoneNumber, String email) {
        this.idClients = id;
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
        /*
    	Set<String> clientLists = db.getClientsLogin();
    	if (clientLists.contains(this.login)) {
    		return true;
    	} else {
    		return false;
    	}
        */
        return db.clientAuthorized(login, password);
    }
    
    

}
