package iCook.View.Operations;

import iCook.Controller.ServiceDispatcher;
import iCook.View.DisplayObjects.IngredientDisplayObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Team 2
 * @version 12/4/2020
 */
public class InventoryUI extends JFrame{


    private JFrame frame;

    /*Left side of frame instance variables*/
    private JPanel mainleftPanel;
    private JButton[] incrementBtns;
    private JButton[] quantityBtns;
    private JButton[] decrementBtns;

    private JPanel[] btnContainer;
    private JPanel totalBtnContainer;

    JRadioButton[] ingredient_name_buttons; // = new JRadioButton[ingredientList.size()];
    JRadioButton [] ingredient_unit_buttons; //= new JRadioButton[ingredientList.size()];


    /*Right side of frame instance variables*/
    private JPanel mainrightPanel;
    private JButton[] increaseBtns;
    private JButton[] decreaseBtns;
    private JButton[] amountBtns;

    private JPanel[] btnContainerRight;
    private JPanel totalBtnContainerRight;

    /*Bottom panel with Buttons instance variables*/
    private JButton home;
    private JButton recipes;
    private JButton update;


    private ServiceDispatcher serviceDispatcher;

    private ArrayList<IngredientDisplayObject> ingredientList;    // stores the system ingredients

    private ArrayList<IngredientDisplayObject> userIngredientList;      // stores the user's ingredients
    private HashMap<String, IngredientDisplayObject> addedIngredients;        // stores newly added ingredients
    private ArrayList<IngredientDisplayObject> pendingIngredientList;   // this is the list of the entire inventory for the user (sent to controller when update button pressed)

    private JButton[] currInventoryQuantity;
    private JButton[] currInventoryIncrement;
    private JButton[] currInventoryDecrement;

    private ArrayList<JButton> userInventoryIncrementBtns; // list of newly created + buttons (whenever add button is pressed)
    private ArrayList<JButton> userInventoryDecrementBtns; // list of newly created - buttons (whenever add button is pressed)
    private ArrayList<JButton> userInventoryQuantityBtns;

    private ArrayList<JButton> availableInventoryIncrementBtns;
    private ArrayList<JButton> availableInventoryDecrementBtns;
    private ArrayList<JButton> availableInventoryAmountBtns;

    ButtonListener bl = new ButtonListener();
    RadioButtonListener rbl = new RadioButtonListener();

    public InventoryUI()
    {
        // initialize instance variables
        addedIngredients = new HashMap<>();
        userInventoryIncrementBtns = new ArrayList<>();
        userInventoryDecrementBtns = new ArrayList<>();
        userInventoryQuantityBtns = new ArrayList<>();

        availableInventoryIncrementBtns = new ArrayList<>();
        availableInventoryDecrementBtns = new ArrayList<>();
        availableInventoryAmountBtns = new ArrayList<>();

        serviceDispatcher = new ServiceDispatcher();
        ingredientList = serviceDispatcher.getAvailableIngredients();
        userIngredientList = serviceDispatcher.getUserInventory();

        DisplayFrame();
    }

    private void DisplayFrame()
    {
        frame = new JFrame();
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel container = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        container.setBackground(new Color(26, 27, 34));
        topPanel.setBackground(new Color(26, 27, 34));
        bottomPanel.setBackground(new Color(26, 27, 34));

        container.setLayout(new GridLayout(0,1));

        home = new JButton("Home");
        recipes = new JButton("Recipes");
        update = new JButton("Update");

        home.addActionListener(bl);
        recipes.addActionListener(bl);
        update.addActionListener(bl);

        bottomPanel.add(home);
        bottomPanel.add(recipes);
        bottomPanel.add(update);

        bottomPanel.setLayout(new GridLayout(0, 6));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(26, 27, 34));

        JPanel leftunitPanel = new JPanel();
        leftunitPanel.setBackground(new Color(26, 27, 34));

        JPanel rightunitPanel = new JPanel();
        rightunitPanel.setBackground(new Color(26, 27, 34));

        /* Array for ingredients that can be added (on left side of frame) */
        mainleftPanel = new JPanel();
        mainleftPanel.setBackground(new Color(26, 27, 34));


        //==========================================================================

        /* Arrays for user's current inventory (right side of frame) */
        JPanel mainrightPanel = new JPanel();
        mainrightPanel.setBackground(new Color(26, 27, 34));

        JPanel ingredientPortion = new JPanel();
        ingredientPortion.setBackground(new Color(26, 27, 34));

        JPanel unitPortion = new JPanel();
        unitPortion.setBackground(new Color(26, 27, 34));

        JPanel[] btnContainerRight;
        JPanel totalBtnContainerRight = new JPanel();
        totalBtnContainerRight.setBackground(new Color(26, 27, 34));

        JLabel [] currInventoryName;
        JRadioButton [] currInventoryUnits;

        JButton[] increaseBtns;
        JButton[] amountBtns;
        JButton[] decreaseBtns;


        if(userIngredientList.isEmpty()) {

            /*Left Panel Attributes*/

            ingredient_name_buttons = new JRadioButton[ingredientList.size()];
            ingredient_unit_buttons = new JRadioButton[ingredientList.size()];

            rightPanel.setLayout(new GridLayout(ingredientList.size(), 1));
            leftunitPanel.setLayout(new GridLayout(ingredientList.size(), 1));

            // this is for the left panel names/units
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredient_name_buttons[i] = new JRadioButton();
                ingredient_name_buttons[i].setText(ingredientList.get(i).getName());
                ingredient_name_buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
                ingredient_name_buttons[i].setBackground(new Color(26, 27, 34));
                ingredient_name_buttons[i].setForeground(Color.WHITE);

                ingredient_name_buttons[i].addActionListener(rbl);
                ingredient_name_buttons[i].setEnabled(true);
                rightPanel.add(ingredient_name_buttons[i]);

                ingredient_unit_buttons[i] = new JRadioButton();
                ingredient_unit_buttons[i].setText(ingredientList.get(i).getUnitOfMeasure());
                ingredient_unit_buttons[i].setBackground(new Color(26, 27, 34));
                ingredient_unit_buttons[i].setForeground(Color.WHITE);
                ingredient_unit_buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));

                leftunitPanel.add(ingredient_unit_buttons[i]);
            }

            //
            incrementBtns = new JButton[ingredientList.size()];
            quantityBtns = new JButton[ingredientList.size()];
            decrementBtns = new JButton[ingredientList.size()];

            // holds buttons -/+ for a row
            btnContainer = new JPanel[ingredientList.size()];

            // holds all buttons
            totalBtnContainer = new JPanel(new GridLayout(ingredientList.size(), 1));
            totalBtnContainer.setBackground(new Color(26, 27, 34));

            // this is for left panel -/+ buttons
            for(int i = 0; i < btnContainer.length; i++){
                btnContainer[i] = new JPanel(new GridLayout(1, 0));
                btnContainer[i].setBackground(new Color(26, 27, 34));

                decrementBtns[i] = new JButton("-");
                decrementBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                decrementBtns[i].setForeground(new Color(26, 27, 34));

                quantityBtns[i] = new JButton(" ");
                quantityBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                quantityBtns[i].setForeground(new Color(26, 27, 34));

                incrementBtns[i] = new JButton("+");
                incrementBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                incrementBtns[i].setForeground(new Color(26, 27, 34));

                btnContainer[i].add(decrementBtns[i]);
                btnContainer[i].add(quantityBtns[i]);
                btnContainer[i].add(incrementBtns[i]);


                totalBtnContainer.add(btnContainer[i]);

                decrementBtns[i].addActionListener(bl);
                incrementBtns[i].addActionListener(bl);
                quantityBtns[i].addActionListener(bl);

                availableInventoryIncrementBtns.add(incrementBtns[i]);
                availableInventoryAmountBtns.add(quantityBtns[i]);
                availableInventoryDecrementBtns.add(decrementBtns[i]);
            }

        }



        else{

            /*Pass usersInventory to iCookingredientinventory & extract values
            that user does not have and put into available ingredients array
             */

            /* Displays available ingredients on right side of jframe */
            ingredient_name_buttons = new JRadioButton[ingredientList.size()];
            ingredient_unit_buttons = new JRadioButton[ingredientList.size()];

            incrementBtns = new JButton[ingredientList.size()];
            quantityBtns = new JButton[ingredientList.size()];
            decrementBtns = new JButton[ingredientList.size()];

            // for a row of -/+ buttons
            btnContainer = new JPanel[ingredientList.size()];

            // for all buttons
            totalBtnContainer = new JPanel(new GridLayout(ingredientList.size(), 1));
            totalBtnContainer.setBackground(new Color(26, 27, 34));

            rightPanel.setLayout(new GridLayout(ingredientList.size(), 1));
            leftunitPanel.setLayout(new GridLayout(ingredientList.size(), 1));

            // this is for the name / units for each ingredient in the left panel
            for(int i = 0; i < ingredientList.size(); i++){
                ingredient_name_buttons[i] = new JRadioButton();
                ingredient_name_buttons[i].setText(ingredientList.get(i).getName());
                ingredient_name_buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
                ingredient_name_buttons[i].setBackground(new Color(26, 27, 34));
                ingredient_name_buttons[i].setForeground(Color.WHITE);
                ingredient_name_buttons[i].addActionListener(rbl);
                ingredient_name_buttons[i].setEnabled(true);

                rightPanel.add(ingredient_name_buttons[i]);

                ingredient_unit_buttons[i] = new JRadioButton();
                ingredient_unit_buttons[i].setText(ingredientList.get(i).getUnitOfMeasure());
                ingredient_unit_buttons[i].setBackground(new Color(26, 27, 34));
                ingredient_unit_buttons[i].setForeground(Color.WHITE);
                ingredient_unit_buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));

                leftunitPanel.add(ingredient_unit_buttons[i]);
            }

            // this is for each -/+ button in the left panel
            for(int i = 0; i < btnContainer.length; i++)
            {
                decrementBtns[i] = new JButton("-");
                decrementBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                decrementBtns[i].setForeground(new Color(26, 27, 34));

                quantityBtns[i] = new JButton(" ");
                quantityBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                quantityBtns[i].setForeground(new Color(26, 27, 34));

                incrementBtns[i] = new JButton("+");
                incrementBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                incrementBtns[i].setForeground(new Color(26, 27, 34));

                btnContainer[i] = new JPanel(new GridLayout(1, 0));
                btnContainer[i].setBackground(new Color(26, 27, 34));

                btnContainer[i].add(decrementBtns[i]);
                btnContainer[i].add(quantityBtns[i]);
                btnContainer[i].add(incrementBtns[i]);

                totalBtnContainer.add(btnContainer[i]);

                decrementBtns[i].addActionListener(bl);
                quantityBtns[i].addActionListener(bl);
                incrementBtns[i].addActionListener(bl);

                availableInventoryIncrementBtns.add(incrementBtns[i]);
                availableInventoryAmountBtns.add(quantityBtns[i]);
                availableInventoryDecrementBtns.add(decrementBtns[i]);
            }



            /*Right Panel Attributes*/
            currInventoryName = new JLabel[userIngredientList.size()];
            currInventoryUnits = new JRadioButton[userIngredientList.size()];
            decreaseBtns = new JButton[userIngredientList.size()];
            amountBtns = new JButton[userIngredientList.size()];
            increaseBtns = new JButton[userIngredientList.size()];

            rightunitPanel.setLayout(new GridLayout(userIngredientList.size(), 1));

            ingredientPortion.setLayout(new GridLayout(userIngredientList.size(), 1));

            btnContainerRight = new JPanel[userIngredientList.size()];
            totalBtnContainerRight.setLayout(new GridLayout(userIngredientList.size(), 1));

            // this is for every name / unit of user possessed ingredient
            for(int i = 0; i < userIngredientList.size(); i++){
                currInventoryName[i] = new JLabel();
                currInventoryName[i].setText(userIngredientList.get(i).getName());
                currInventoryName[i].setFont(new Font("Arial", Font.PLAIN, 20));
                currInventoryName[i].setBackground(new Color(26, 27, 34));
                currInventoryName[i].setForeground(Color.WHITE);

                ingredientPortion.add(currInventoryName[i]);
                ingredientPortion.setBackground(new Color(26, 27, 34));

                currInventoryUnits[i] = new JRadioButton();
                currInventoryUnits[i].setText(userIngredientList.get(i).getUnitOfMeasure());
                currInventoryUnits[i].setBackground(new Color(26, 27, 34));
                currInventoryUnits[i].setForeground(Color.WHITE);
                currInventoryUnits[i].setFont(new Font("Arial", Font.PLAIN, 20));

                rightunitPanel.add(currInventoryUnits[i]);
            }


            // this is for every -/+ button for the ingredients the user possesses
            for(int i = 0; i < userIngredientList.size(); i++){
                decreaseBtns[i] = new JButton("-");
                decreaseBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                decreaseBtns[i].setForeground(new Color(26, 27, 34));

                amountBtns[i] = new JButton(String.valueOf(userIngredientList.get(i).getQuantity()));
                amountBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                amountBtns[i].setText(String.valueOf(userIngredientList.get(i).getQuantity()));
                amountBtns[i].setForeground(new Color(26, 27, 34));

                increaseBtns[i] = new JButton("+");
                increaseBtns[i].setFont(new Font("Arial", Font.PLAIN, 19));
                increaseBtns[i].setForeground(new Color(26, 27, 34));

                btnContainerRight[i] = new JPanel(new GridLayout(1, 0));
                btnContainerRight[i].setBackground(new Color(26, 27, 34));
                btnContainerRight[i].add(decreaseBtns[i]);
                btnContainerRight[i].add(amountBtns[i]);
                btnContainerRight[i].add(increaseBtns[i]);

                decreaseBtns[i].addActionListener(bl);
                amountBtns[i].addActionListener(bl);
                increaseBtns[i].addActionListener(bl);

                totalBtnContainerRight.add(btnContainerRight[i]);

                userInventoryIncrementBtns.add(decreaseBtns[i]);
                userInventoryQuantityBtns.add(amountBtns[i]);
                userInventoryDecrementBtns.add(increaseBtns[i]);
            }
        }


        JPanel paneltopLeft = new JPanel(new BorderLayout());
        paneltopLeft.setBackground(new Color(26, 27, 34));

        JLabel test1 = new JLabel("Available Ingredients");
        test1.setForeground(new Color(249,250,244));
        test1.setFont(new Font("Helvetica", Font.BOLD, 22));
        test1.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel paneltopRight = new JPanel(new BorderLayout());
        paneltopRight.setBackground(new Color(26, 27, 34));

        JLabel test2 = new JLabel("Your Ingredient Inventory");
        test2.setForeground(new Color(249,250,244));
        test2.setFont(new Font("Helvetica", Font.BOLD, 22));
        test2.setHorizontalAlignment(SwingConstants.CENTER);

        paneltopLeft.add(test1, BorderLayout.CENTER);
        paneltopRight.add(test2, BorderLayout.CENTER);

        JSplitPane s1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, paneltopLeft, paneltopRight);
        JSplitPane s2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, mainleftPanel, mainrightPanel);
        JSplitPane main_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, s1, s2);
        JSplitPane bottom_split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, main_split, bottomPanel);

        final boolean firstResize = true;

        /* Action listener lines up the split panes to be centered as much as possible*/

        s1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(firstResize){
                    s1.setDividerLocation(0.5);
                    s2.setDividerLocation(0.5);
                    main_split.setDividerLocation(0.10);
                }
            }
        });


        /*Left Panel Layout */
        mainleftPanel.add(rightPanel, BorderLayout.WEST);
        mainleftPanel.add(totalBtnContainer, BorderLayout.CENTER);
        mainleftPanel.add(leftunitPanel, BorderLayout.EAST);
        mainleftPanel.setBackground(new Color(26, 27, 34));

        /*Right Panel Layout*/
        mainrightPanel.add(ingredientPortion, BorderLayout.WEST);
        mainrightPanel.add(totalBtnContainerRight, BorderLayout.CENTER);
        mainrightPanel.add(rightunitPanel, BorderLayout.EAST);
        mainrightPanel.setBackground(new Color(26, 27, 34));


        frame.add(main_split, BorderLayout.CENTER);
        frame.add(bottom_split, BorderLayout.SOUTH);
        frame.setSize(1024, 768);
        frame.setVisible(true);

    }


    private class RadioButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Loop to check through list of ingredients
            //If ingredient is selected, it will be added to center panel
            for(int i = 0; i < ingredientList.size(); i++)
            {
                // if the selected item == the ingredient's name
                if (ingredient_name_buttons[i].isSelected())
                {
                    // get the ingredient at i
                    IngredientDisplayObject ingredient = ingredientList.get(i);
                    String selectedIngredient = ingredient_name_buttons[i].getText();

                    if(selectedIngredient == ingredient.getName() && !addedIngredients.containsValue(ingredient))
                    {
                        // add ingredient to list of new ingredient for user
                        addedIngredients.put(ingredient.getName(), ingredient);

                        // testing purposes
                        System.out.println("Selected Item: " + ingredient.getName());
                        System.out.println("Unit of Measure: " + ingredient.getUnitOfMeasure());
                    }
                }
            }
        }

    }


    // ActionListeners for search, add, update, and home buttons
    private class ButtonListener implements ActionListener
    {
        /*
        * Updates the quantity of an ingredient that was already in the user's inventory
        */
        private int updateUserIngredientQuantity(int index, JButton operation){
            int currentQuantity = Integer.parseInt(userInventoryQuantityBtns.get(index).getText());
            int updatedQuantity = 0;

            if (operation.getText() == "+"){
                updatedQuantity = currentQuantity + 1;
            }
            else if (operation.getText() == "-" && currentQuantity > 0) {
                updatedQuantity = currentQuantity - 1;
            }

            return updatedQuantity;
        }


        /*
         * Updates the quantity of a newly added ingredient
         */
        private int updateNewIngredientQuantity(int index, JButton operation){
            int currentQuantity;

            if (availableInventoryAmountBtns.get(index).getText() == " ")
                currentQuantity = 0;
            else
                currentQuantity = Integer.parseInt(availableInventoryAmountBtns.get(index).getText());

            int updatedQuantity = 0;

            if (operation.getText() == "+"){
                updatedQuantity = currentQuantity + 1;
            }
            else if (operation.getText() == "-" && currentQuantity > 0) {
                updatedQuantity = currentQuantity - 1;
            }

            return updatedQuantity;
        }


        @Override
        public void actionPerformed(ActionEvent e) {

            JButton src2 = (JButton) e.getSource();
            int updatedQuantity;


            // loop through array of increment buttons associated with existing ingredients
            for(int i = 0; i < userIngredientList.size(); i++)
            {
                if(src2 == userInventoryIncrementBtns.get(i))
                {
                        updatedQuantity = updateUserIngredientQuantity(i, userInventoryIncrementBtns.get(i));
                        userInventoryQuantityBtns.get(i).setText(String.valueOf(updatedQuantity));
                        userIngredientList.get(i).setQuantity(updatedQuantity);
                }
            }

            // loop through array of decrement buttons associated with existing ingredients
            for(int i = 0; i < userIngredientList.size(); i++){
                if(src2 == userInventoryDecrementBtns.get(i))
                {
                        updatedQuantity = updateUserIngredientQuantity(i, userInventoryDecrementBtns.get(i));
                        userInventoryQuantityBtns.get(i).setText(String.valueOf(updatedQuantity));
                        userIngredientList.get(i).setQuantity(updatedQuantity);
                }
            }

            // loop through every newly added inc button and set the quantity to its corresponding ingredient
            // in the addedIngredients List
            for (int i = 0; i < ingredientList.size(); i++)
            {
                if(src2 == availableInventoryIncrementBtns.get(i) && ingredient_name_buttons[i].isSelected())
                {
                    updatedQuantity = updateNewIngredientQuantity(i, availableInventoryIncrementBtns.get(i));
                    availableInventoryAmountBtns.get(i).setText(String.valueOf(updatedQuantity));
                    addedIngredients.get(ingredientList.get(i).getName()).setQuantity(updatedQuantity);
                }
            }

            // loop through every newly added dec button and set the quantity to its corresponding ingredient
            // in the addedIngredients List
            for (int i = 0; i < ingredientList.size(); i++)
            {
                if(src2 == availableInventoryDecrementBtns.get(i) && ingredient_name_buttons[i].isSelected())
                {
                    updatedQuantity = updateNewIngredientQuantity(i, availableInventoryDecrementBtns.get(i));
                    availableInventoryAmountBtns.get(i).setText(String.valueOf(updatedQuantity));
                    addedIngredients.get(ingredientList.get(i).getName()).setQuantity(updatedQuantity);
                }
            }

            // if the user clicked the search button
            if(src2 == recipes)
            {
                new RecipeUI();
                frame.setVisible(false);
                frame.dispose();
            }

            // else if the user clicked the update button
            else if(src2 == update)
            {
                pendingIngredientList = new ArrayList<>();          // to be sent to the controller for processing
                pendingIngredientList.addAll(userIngredientList);   // add the users ingredient list to the pending list
                pendingIngredientList.addAll(addedIngredients.values());     // add the newly added ingredients to the pending list
                serviceDispatcher.updateUserInventory(pendingIngredientList);

                new InventoryUI();

                // refresh this page
                frame.setVisible(false);
                frame.dispose();
            }

            // else if the user clicked the home button
            else if(src2 == home)
            {
                new HomeUI(serviceDispatcher.getUserName());

                //Instantiate home Class to display home GUI
                frame.setVisible(false);
                frame.dispose();
            }
        }
    }

}
