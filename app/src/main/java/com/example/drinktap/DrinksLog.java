package com.example.drinktap;

import java.util.ArrayList;

/**
 * Contains all the drinks added
 */
class DrinksLog {
    private ArrayList<Drink> drinkLog;

    public DrinksLog(){
        drinkLog = new ArrayList<>();
    }

    /**
     * Adds a drink to the log.
     * @param description drink description
     * @param volume drink volume in oz
     * @param content drink content in %
     */
    public void addDrink(String description, Double volume, Double content){
        Drink drink = new Drink(description, volume, content);
        drinkLog.add(drink);
    }

    /**
     * Returns the log list.
     * @return log list.
     */
    public ArrayList<Drink> getList(){
        return drinkLog;
    }

    /**
     * Set the list of the log.
     * @param list new list.
     */
    public void setList(ArrayList<Drink> list){
        drinkLog = list;
    }

    /**
     * Clear log.
     */
    public void clearLog(){
        drinkLog.clear();
    }
}