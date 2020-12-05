package iCook.Model;

/**
 * User class that stores a user's id, username, and password
 * Uses the Singleton Pattern - only 1 user should exists in runtime
 *
 * @author Team 2
 * @version 11/29/2020
 */
public class User {

    // the SINGLETON object of this class
    private static User userObject = null;


    // instance variables
    private int id;             // id that is stored in the database
    private String userName;    // the username of this user
    private String password;    // the password of this user


    /**
     * Constructor - 4 variables must be passed in
     */
    private User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        //this.userIngredients = userIngredients;
    }


    /**
     * Returns the singleton user object
     *
     * Determines if a user object is already created.
     * If the singleton exists, return the object
     * else, create a new user object and return it
     */
    public static User getUser(int id, String userName, String password)
    {
        if (userObject == null)
        {
            userObject = new User(id, userName, password);
            System.out.println("User created\n");
        }

        return userObject;
    }


    /**
     * Returns the singleton user object - no parameters needed here
     *
     * Determines if a user object is already created.
     * If the singleton exists, return the object
     * else, return null
     */
    public static User getUser()
    {
        if (userObject != null)
            return userObject;
        else
            return null;
    }


    /**
     * Sets the User Singleton to Null (A new User Singleton can be created after this call)
     */
    public void deleteUserObject()
    {
        userObject = null ;
    }


    /**
     * Getter for the user's username
     */
    public String getUserName()
    {
        return userName;
    }


    /**
     * Getter for the user's password
     */
    public String getPassword()
    {
        return password;
    }


    /**
     * Getter for the user's id
     */
    public int getId()
    {
        return id;
    }


} // end of User class
