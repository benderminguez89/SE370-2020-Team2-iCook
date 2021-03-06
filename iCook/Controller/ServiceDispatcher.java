package iCook.Controller;

import iCook.Model.*;
import iCook.View.AbstractUI;
import iCook.View.Operations.DisplayObjects.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Vector;

/**
 * The main controller class for iCook's MVC design pattern. Communicates between the View and Model packages.
 *
 * @author Team 2
 * @version 5/7/2021
 */
public class ServiceDispatcher {
    // user needs to be static (not unique for each ServiceDispatcher object)
    private static User user = null;

    private static String SECRET_KEY = "my_super_secret_key";
    private static String SALT = "ssshhhhhhhhhhh!!!!";

    // instance variables
    private Facade facade;
    private ArrayList<UserIngredient> userIngredients;

    // all UI elements needed
    private static JFrame frame;
    private AbstractUI current_state;


    /**
     * Constructor - initializes instance variables.
     * Calls getUserIngredients to populate userIngredients.
     */
    public ServiceDispatcher() {
        facade = new Facade();
        userIngredients = new ArrayList<>();
        getUserIngredients();
    }


    /**
     * Hashes the user's password using one-way encryption
     *
     * @param strToEncrypt the password to be encrypted
     * @return the hashed password
     */
    private String encrypt(String strToEncrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }


    /**
     * Requests the facade to log the user in.
     * If the facade successfully logs the user in, initialize the user singleton.
     *
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return true if the login was successful, false otherwise
     */
    public boolean login(String username, String password) {
        // get the hashed password
        String hashedPassword = encrypt(password);

        if ( facade.login(username, hashedPassword) ) {
            // initialize the user SINGLETON here
            // initialize the user's list of ingredient here (1st time)
            user = User.getUser();
            assert user != null;
            userIngredients = facade.getUserIngredients(user.getId());
            return true;
        }
        else
            return false;
    }


    /**
     * Requests the facade to create a new user with the given credentials.
     * Initializes the user singleton with the newly created account and sets the
     * user's list of ingredients to null.
     *
     * @param username the username of the user trying to sign up
     * @param password the password of the user trying to sign up
     * @return the username if sign up was successful, error message if the username is taken
     */
    public String signUp(String username, String password) {
        try {
            // get the hashed password
            String hashedPassword = encrypt(password);

            facade.signUp(username, hashedPassword);
            user = User.getUser();
            userIngredients = null;
            return username;
        }

        catch (UsernameTakenException error)
        {
            return error.toString();
        }
    }


    /**
     * Gets the username of the user SINGLETON
     *
     * @return the username of the logged in user
     */
    public String getUserName() {
        return user.getUserName();
    }


    /**
     * Displays the user singleton in the console
     */
    public void displayUser() {
        System.out.println(user.getId() + " " + user.getUserName());
    }


    /**
     * Determines if the user is logged in by looking at the user SINGLETON object
     *
     * @return true if the singleton object is not null, false otherwise
     */
    public boolean isLoggedIn() {
        return user != null;
    }


    /**
     * Returns the boolean value indicating whether or not the current user
     * is an admin or not.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isUserAdmin() {
        return user.isAdmin();
    }


    /**
     * Return an ArrayList of IngredientDisplayObjects representing the system's ingredients
     *
     * @return List of IngredientDisplayObjects representing the system's ingredients
     */
    public ArrayList<IngredientDisplayObject> getSystemIngredientDisplayObjects() {
        // list we are going to return
        ArrayList<IngredientDisplayObject> ingredientDisplayObjects = new ArrayList<>();

        // get the list of system ingredients from the system
        ArrayList<Ingredient> ingredients = facade.getSystemIngredients();

        // for every system ingredient, convert it into an IngredientDisplayObject and add it
        // to the array list
        for (Ingredient ing : ingredients) {
            ingredientDisplayObjects.add(new IngredientDisplayObject(ing.getIngredientID(),
                    ing.getIngredientName(), ing.getUnitOfMeasure(), 0));
        }

        return ingredientDisplayObjects;
    }


    /**
     * Returns an ArrayList of IngredientDisplayObjects representing the user's inventory
     *
     * @return an ArrayList of IngredientDisplayObjects representing all user ingredients
     */
    public ArrayList<IngredientDisplayObject> getUserInventory() {
        // initialize the user's ingredients
        // ** this will change so need call here **
        getUserIngredients();

        // the user's inventory will be stored in an ArrayList
        ArrayList<IngredientDisplayObject> inventory = new ArrayList<>();

        // for every user ingredient in the list of user ingredients
        for (UserIngredient userIngredient : userIngredients)
        {
            // store the Ingredient's id
            int ingredientID = userIngredient.getIngredientID();

            // store the Ingredient's name
            String name = userIngredient.getUserIngredientName();

            // store the Ingredient's unit of measure
            String unitOfMeasure = userIngredient.getUserIngredientUnitOfMeasure();

            // store the Ingredient's quantity
            int quantity = userIngredient.getQuantity();

            // add a new IngredientDisplayObject to the ArrayList
            inventory.add(new IngredientDisplayObject(ingredientID, name, unitOfMeasure, quantity));
        }


        Collections.sort(inventory);

        // return the ArrayList
        return inventory;
    }


    /**
     * Initializes userIngredients with an ArrayList containing UserIngredient objects
     */
    private void getUserIngredients() {
        // only call the facade if the user singleton has been initialized
        if (user != null)
            userIngredients = facade.getUserIngredients(user.getId());
    }


    /**
     * Requests the facade to update the user's inventory with a given ArrayList
     *
     * @param updatedIngredientList an ArrayList of IngredientDisplayObjects that contains the user's pending inventory information (to be updated)
     */
    public void updateUserInventory(ArrayList<IngredientDisplayObject> updatedIngredientList) {
        // store the needed ingredient information in a HashMap
        ArrayList<UserIngredient> userIngredients = new ArrayList<>();

        // Convert every display object into an ingredient object, then
        // create a new UserIngredient object and append it to userIngredients
        for (IngredientDisplayObject ingredient : updatedIngredientList) {
            UserIngredient ui = new UserIngredient(user, new Ingredient(ingredient.getIngredientID(),
                    ingredient.getName(), ingredient.getUnitOfMeasure()));  // new UserIngredient object

            ui.setQuantity(ingredient.getQuantity());   // set the quantity

            userIngredients.add(ui);    // add to list
        }

        // send to the facade to be processed
        facade.updateUserInventory(user.getId(), userIngredients);
    }


    /**
     * Requests the facade to return an ArrayList of recipes available to the user
     *
     * @return an ArrayList of RecipeDisplayObjects that represent recipes satisfiable to the user, based on their inventory
     */
    public ArrayList<RecipeDisplayObjectIF> getSatisfiedRecipes() {
        // send the user's inventory to the facade to be processed
        ArrayList<Recipe> recipes = facade.getSatisfiedRecipes(userIngredients, user.getId());

        // make sure the user has recipes available to them
        if (recipes != null && !recipes.isEmpty())
        {
            // create new ArrayList of RecipeDisplayObjects to be sent to the View
            ArrayList<RecipeDisplayObjectIF> display_recipes = new ArrayList<>();

            // for every Recipe object, create a RecipeDisplayObjectIF and add it to
            // the ArrayList to be returned
            for (Recipe recipe : recipes) {
                RecipeDisplayObjectIF recipeDO = new RecipeDisplayObject(recipe.getRecipeID(),
                                                        recipe.getRecipeName(), recipe.getInstructions(),
                                                        getIngredientDisplayObjects(recipe), recipe.isPublished());

                // get the current recipe's image URL
                String img_url = facade.getRecipeImageURL(recipe.getRecipeID());

                // if the image has a supported protocol, decorate the recipeDO with an image & add it to the arraylist.
                if (img_url != null && img_url.startsWith("http")) {
                    try {
                        URL url = new URL(img_url);
                        BufferedImage image = ImageIO.read(url);
                        RecipeDisplayObjectIF decorated_recipeDO = new Recipe_With_Image(recipeDO, image);

                        display_recipes.add(decorated_recipeDO);
                    }
                    catch (IOException e) {
                        System.out.println(e);
                        display_recipes.add(recipeDO);
                    }
                }

                // otherwise add the concrete recipeDO to the arraylist.
                else {
                    display_recipes.add(recipeDO);
                }

            } // end of for-loop


            // Sorts the recipe display objects alphabetically by recipe name
            Collections.sort(display_recipes);

            // return the list of available recipes
            return display_recipes;
        }

        // return null to indicate that the user cannot make any recipes with their current inventory
        return null;
    }


    /**
     * Logs the user out of their account. Deletes the User Singleton.
     */
    public void logUserOut() {
        user.deleteUserObject();
    }


    /**
     * Sends a Request to the Facade to return a Vector containing iCook's recipes.
     *
     * @return a Vector containing vectors (each inner vector contains a recipe's info).
     */
    public Vector<Vector> getRecipes() {
        return facade.getRecipes();
    }


    /**
     * Sends a Request to the Facade to return a Recipe object which is then converted
     * to a RecipeDisplayObject for the View to access.
     *
     * @param id the id of the recipe we want access to
     * @return a RecipeDisplayObject corresponding to the passed in recipe id
     */
    public RecipeDisplayObject getRecipeDisplayObject(int id) {
        Recipe recipe = facade.getRecipe(id);

        return new RecipeDisplayObject(id, recipe.getRecipeName(), recipe.getInstructions(), getIngredientDisplayObjects(recipe), recipe.isPublished());
    }


    /**
     * Converts a Recipe object's ingredient list to an ArrayList of IngredientDisplayObjects.
     * Used in conversions between Recipe objects and RecipeDisplayObjects
     *
     * @param recipe the Recipe whose list of ingredients we want to convert
     * @return an ArrayList of IngredientDisplayObjects
     */
    private ArrayList<IngredientDisplayObject> getIngredientDisplayObjects(Recipe recipe) {
        // list to be returned
        ArrayList<IngredientDisplayObject> ingredientDisplayObjects = new ArrayList<>();

        // convert the recipe's list RecipeIngredients to a list of IngredientDisplayObjects
        for (RecipeIngredient ri : recipe.getIngredients()) {
            ingredientDisplayObjects.add(new IngredientDisplayObject(ri.getIngredient().getIngredientID(),
                    ri.getIngredient().getIngredientName(), ri.getIngredient().getUnitOfMeasure(), ri.getQuantity()));
        }

        return ingredientDisplayObjects;
    }


    /**
     * Converts a RecipeDisplayObject's ingredient list to an ArrayList of RecipeIngredient Objects.
     * Used in conversions between RecipeDisplayObjects and Recipe objects.
     *
     * @param recipeDO the RecipeDisplayObject whose list of ingredients we want to convert
     * @return an ArrayList of RecipeIngredient Objects
     */
    private ArrayList<RecipeIngredient> getIngredientList(RecipeDisplayObject recipeDO) {
        // list to be returned
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        // convert the recipe's list of IngredientDisplayObjects to a list of RecipeIngredients
        for (IngredientDisplayObject ingDO : recipeDO.getIngredients()) {
            // ingredient to be passed into the new RecipeIngredient object
            Ingredient i = new Ingredient(ingDO.getIngredientID(), ingDO.getName(), ingDO.getUnitOfMeasure());

            // add a new RecipeIngredient to the ArrayList
            ingredients.add(new RecipeIngredient(0, i, ingDO.getQuantity()));
        }

        return ingredients;
    }


    /**
     * Sends a request to the Facade to build a new RecipeIF object
     * which should then be added to the database.
     * (Uses the Builder design pattern)
     *
     * @param recipeDO the RecipeDisplayObject to be added to the database
     */
    public void addNewRecipe(RecipeDisplayObject recipeDO) {
        facade.buildNewRecipe(recipeDO, getIngredientList(recipeDO));
    }


    /**
     * Sends a request to the Facade to build a new RecipeIF object
     * from an existing recipe which should then be updated in the database.
     * (Uses the Builder design pattern)
     *
     * @param recipeDO the RecipeDisplayObject to be updated in the database
     */
    public void updateRecipe(RecipeDisplayObject recipeDO) {
        facade.buildUpdateRecipe(recipeDO, getIngredientList(recipeDO));
    }


    /**
     * Sends a request to the facade to clone and insert a recipe into iCook's database.
     * (USES THE PROTOTYPE DESIGN PATTERN)
     *
     * @param id the id of the recipe we want to clone
     * @param newRecipeName the name of this new cloned recipe
     * @param newRecipeInstructions the instructions of this new cloned recipe
     */
    public void cloneRecipe(int id, String newRecipeName, String newRecipeInstructions) {
        facade.cloneRecipe(id, newRecipeName, newRecipeInstructions, user.getId());
    }


    /**
     * Initializes the program's frame and begins iCook. This is the only entry point for iCook.
     * (Starts the application)
     */
    public void startProgram() {
        // set the look and feel to be uniform on all operating systems
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }

        // The java frame is initialized here
        frame = new JFrame();
        frame.setTitle("iCook");
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // update the current state to the starting state of iCook
        current_state = AbstractUI.start();
        updateState(current_state);
    }


    /**
     * Ends the program
     */
    public void quitProgram() {
        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }


    /**
     * Update the current state (UI) of the program.
     *
     * @param next_state to be the current state
     */
    public void updateState(AbstractUI next_state) {
        current_state = next_state;
        frame.getContentPane().removeAll();
        frame.getContentPane().add(current_state);
        frame.setVisible(true);
    }


} // end of ServiceDispatcher class