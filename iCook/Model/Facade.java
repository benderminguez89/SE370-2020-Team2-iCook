package iCook.Model;

import iCook.Model.DatabaseAccess.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Central class for all DAO classes. Has access to all DAO classes.
 *
 * @author Team 2
 * @version 4/26/2021
 */
public class Facade {

    // instance variables
    private UserDAO userDAO;
    private IngredientDAO ingredientDAO;
    private RecipeDAO recipeDAO;


    /**
     * Constructor - initializes instance variables
     */
    public Facade() {
        userDAO = new UserDAO();
        ingredientDAO = new IngredientDAO();
        recipeDAO = new RecipeDAO();
    }


    /**
     * Calls the UserDAO to determine if the user's credentials are valid.
     * If true, we request the userDAO to create the user singleton.
     *
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return true if the user can login, false otherwise
     */
    public boolean login(String username, String password)
    {
        // determine if the user's login info is valid. If so return true, false otherwise
        if ( userDAO.validUserLogin(username, password) )
        {
            userDAO.getUser(username, password);
            return true;
        }
        else
            return false;
    }


    /**
     * Calls the UserDAO to create a new user with the given credentials.
     * If the username isn't taken, we request the userDAO to create the user singleton.
     *
     * @param username the username of the user trying to sign up
     * @param password the password of the user trying to sign up
     * @throws UsernameTakenException if the username is already taken
     */
    public void signUp(String username, String password) throws UsernameTakenException
    {
        // make sure the username isn't taken
        // NEED TO THROW AN EXCEPTION HERE
        if ( userDAO.usernameIsTaken(username) ) {
            System.out.println("UsernameTakenException thrown");
            throw new UsernameTakenException("\"" + username + "\"" + " is already in use!");
        }
        else {
            // create a new User with the given username and password
            userDAO.addUser(username, password);
            userDAO.getUser(username, password);
        }
    }


    /**
     * Calls the IngredientDAO to return all system ingredients
     *
     * @return an ArrayList of Ingredient objects (all system ingredients)
     */
    public ArrayList<Ingredient> getSystemIngredients()
    {
        return ingredientDAO.getAllIngredients();
    }


    /**
     * Calls the UserDAO to return the user's ingredients
     *
     * @param userID the user's id whose ingredients we want to get
     * @return an ArrayList of UserIngredient objects (user's inventory)
     */
    public ArrayList<UserIngredient> getUserIngredients(int userID)
    {
        return userDAO.getUserIngredients(userID);
    }


    /**
     * Requests the UserDAO to update the user's inventory
     *
     * @param userID the user's id whose ingredients we want to update
     * @param userIngredients an ArrayList of UserIngredient objects to be updated
     */
    public void updateUserInventory(int userID, ArrayList<UserIngredient> userIngredients)
    {
        userDAO.updateUserIngredientTable(userID, userIngredients);
    }


    /**
     * Sends a Request to the RecipeDAO to get a list of recipes available to the user based on their inventory
     *
     * @param userIngredients the ArrayList containing UserIngredient objects (the user's inventory)
     * @param owner_id the user's id so we can include their modified recipes
     * @return an ArrayList of Recipe objects satisfiable to the user, based on their inventory
     */
    public ArrayList<Recipe> getSatisfiedRecipes(ArrayList<UserIngredient> userIngredients, int owner_id)
    {
        return recipeDAO.getSatisfiedRecipes(userIngredients, owner_id);
    }


    /**
     * Sends a Request to the RecipeDAO to get a list of iCook's recipes.
     *
     * @return a Vector containing vectors (each inner vector contains a recipe's info).
     */
    public Vector<Vector> getRecipes()
    {
        return recipeDAO.getRecipes();
    }


    /**
     * Sends a Request to the RecipeDAO to get the desired Recipe object
     *
     * @param id the id of the recipe we want to retrieve
     * @return a Recipe object corresponding to its id
     */
    public Recipe getRecipe(int id)
    {
        return recipeDAO.getRecipe(id);
    }


    /**
     * Sends a Request to the RecipeDAO to insert a new Recipe
     *
     * @param recipe to be inserted into the database
     */
    public void addNewRecipe(Recipe recipe) {
        recipeDAO.addNewRecipe(recipe);
    }


    /**
     * Sends a Request to the RecipeDAO to update the passed in recipe
     *
     * @param recipe to be updated in the database
     */
    public void updateRecipe(Recipe recipe) {
        recipeDAO.updateRecipe(recipe);
    }


    /**
     * Sends a Request to the RecipeDAO to get an ArrayList of all recipes
     *
     * @return ArrayList containing RecipeIF objects
     */
    public ArrayList<RecipeIF> getAllSystemRecipes() {
        return recipeDAO.getAllSystemRecipes();
    }


    /**
     * Sends a Request to the RecipeDAO to insert a cloned recipe
     *
     * @param cloned_recipe the cloned recipe to be inserted into the database
     * @param owner_id owner of this cloned recipe
     */
    public void insertClonedRecipe(RecipeIF cloned_recipe, int owner_id) {
        recipeDAO.insertClonedRecipe(cloned_recipe, owner_id);
    }


} // end of Facade class