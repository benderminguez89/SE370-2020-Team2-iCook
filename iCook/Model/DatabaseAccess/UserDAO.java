package iCook.Model.DatabaseAccess;

import iCook.Model.User;
import iCook.Model.UserIngredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DAO class for the users && user_ingredients table in iCook's database.
 * Every method requires a try and catch for a SQLException.
 *
 * @author Team 2
 * @version 4/29/2021
 */
public class UserDAO extends BaseDAO {

    /**
     * Constructor
     */
    public UserDAO() {
    }


    /**
     * Performs a SQL statement to add a user ingredient to the user_ingredient table
     *
     * @param userID the user's id
     * @param ingredientID the ingredient's id
     * @param quantity the quantity of the ingredient that the user has
     */
    private void addUserIngredient(int userID, int ingredientID, int quantity) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            statement.execute("INSERT INTO user_ingredients (user_id, ingredient_id, quantity) VALUE (" + userID + ", " + ingredientID + ", " + quantity + ") ");
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * Performs a SQL statement to update an existing user ingredient's quantity within the user_ingredients table
     *
     * @param ingredientID the id of the ingredient entry
     * @param quantity the quantity of the user ingredient entry
     */
    private void updateUserIngredient(int ingredientID, int quantity) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            statement.execute("UPDATE user_ingredients SET quantity = '" + quantity + "' WHERE ingredient_id = '" + ingredientID + "' ");
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * Performs a SQL statement to remove a user ingredient from the user_ingredients table
     *
     * @param userID the user's id
     * @param ingredientID the id of the ingredient
     */
    private void deleteUserIngredient(int userID, int ingredientID) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            statement.execute("DELETE FROM user_ingredients WHERE user_id = '" + userID + "' AND ingredient_id =  '"+ ingredientID + "' ");
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * Performs a SQL statement to determine if a user exists within the users table
     *
     * @param username the username field within the users table
     * @param password the password field within the users table
     * @return a boolean value to indicate if the login credentials are valid
     */
    public boolean validUserLogin(String username, String password) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // perform the query
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_name = '" + username + "' AND password = '" + password + "' LIMIT 1");

            // returns true if a there is a result
            // returns false otherwise
            return rs.next();
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return false;
        }
    }


    /**
     * Performs SQL statement to determine if a username is already taken within the users table
     *
     * @param username the username field within the users table
     * @return a boolean value to indicate if the username is taken
     */
    public boolean usernameIsTaken(String username) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // perform the query
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_name = '" + username + "' LIMIT 1");

            // returns true if a there is a result (username already taken)
            // returns false otherwise (user name is free)
            return rs.next();
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return false;
        }
    }


    /**
     * Performs a SQL statement to add a new entry to the users table
     *
     * @param username the username field within the users table
     * @param password the password field within the users table
     */
    public void addUser(String username, String password) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // if an account does not already exists with the given username
            if ( !usernameIsTaken(username) ) {
                // create a new account with the given username and password
                statement.execute("INSERT INTO users (user_name, password) VALUE ('" + username + "', '" + password + "') ");
                System.out.println("Account successfully made!");
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * Performs a SQL statement to get the user id from the users table
     * and creates the User Singleton object
     *
     * @param username the username field within the users table
     * @param password the password field within the users table
     */
    public void getUser(String username, String password) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // perform the query
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_name = '" + username + "' AND password = '" + password + "' LIMIT 1");

            while (rs.next()) {
                int id = rs.getInt("id");
                boolean is_admin = rs.getBoolean("is_admin");

                // create the user singleton (SHOULD BE THE ONLY PLACE THIS HAPPENS)
                User user = User.getUser(id, username, password, is_admin);
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }


    /**
     * Performs a SQL statement to return an Arraylist of UserIngredient objects.
     * Pulls data from the user_ingredients table
     *
     * @param userID the user_id field within the user_ingredients table
     * @return an ArrayList containing UserIngredient objects
     */
    public ArrayList<UserIngredient> getUserIngredients(int userID) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // perform the query
            ResultSet rs = statement.executeQuery("SELECT UI.id AS thisID, UI.quantity, I.id as ingID, I.name, I.unit_of_measure " +
                                                        "FROM user_ingredients UI, ingredients I WHERE UI.user_id = '" + userID + "' AND UI.ingredient_id = I.id");
            ArrayList<UserIngredient> userIngredients = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("thisID");
                int ingredientID = rs.getInt("ingID");
                int quantity = rs.getInt("quantity");
                String name = rs.getString("name");
                String unit_of_measure = rs.getString("unit_of_measure");

                // add the user ingredient to the array list
                userIngredients.add(new UserIngredient(id, userID, ingredientID, quantity, name, unit_of_measure));
            }

            return userIngredients;
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }


    /**
     * Performs a SQL statement to update the user_ingredients table for a specified user
     *
     * @param userID the user_id field within the user_ingredients table
     * @param userIngredients an ArrayList that contains each UserIngredient to be inserted
     */
    public void updateUserIngredientTable(int userID, ArrayList<UserIngredient> userIngredients) {
        try {
            //create a new statement
            Statement statement = this.createStatement();

            // need to access the IngredientDAO for validity check
            IngredientDAO ingredientDAO = new IngredientDAO();

            // simply remove all entries for this user
            statement.executeUpdate("DElETE FROM user_ingredients WHERE user_id = '"+userID+"' ");

            // insert the user's ingredients
            for (UserIngredient ingredient : userIngredients) {
                // make sure the ingredient is valid (safety measure --> this should always be valid)
                if (ingredientDAO.validIngredient(ingredient.getIngredientID()) && ingredient.getQuantity() != 0) {
                    statement.executeUpdate("INSERT INTO user_ingredients " +
                            "VALUES(NULL, '"+userID+"', '"+ingredient.getIngredientID()+"', '"+ingredient.getQuantity()+"') ");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


} // end of UserDAO class
