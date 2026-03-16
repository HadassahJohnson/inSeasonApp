package com.nowhere;

import java.util.*;
import java.time.Month;

public enum NYSeasonalProduce {
    JANUARY("No Produce Available"),
    FEBRUARY("No Produce Available"),
    MARCH("No Produce Available"),
    APRIL("No Produce Available"),
    MAY("Asparagus", "Broccoli", "Radishes", "Rhubarb", "Spinach"),
    JUNE("Asparagus", "Broccoli", "Radishes", "Rhubarb", "Spinach", "Strawberries", "Beans", "Cabbage", "Lettuce", "Summer Squash"),
    JULY("Beans", "Beets", "Blackberries", "Blueberries", "Broccoli", "Cabbage", "Carrots", "Cauliflower", "Cherries", "Corn", "Cucumbers", "Lettuce", "Nectarines", "Peaches", "Peas", "Potatoes", "Radishes", "Raspberries", "Rhubarb", "Spinach", "Summer Squash", "Strawberries", "Tomatoes", "Turnips"),
    AUGUST("Apples", "Beans", "Beets", "Blackberries", "Blueberries", "Broccoli", "Cabbage", "Carrots", "Cauliflower", "Cherries", "Corn", "Cucumbers", "Eggplant", "Grapes", "Lettuce", "Melons", "Nectarines", "Onions", "Peaches", "Pears", "Peppers", "Potatoes", "Radishes", "Rhubarb", "Spinach", "Summer Squash", "Winter Squash", "Tomatoes", "Turnips"),
    SEPTEMBER("Apples", "Beans", "Beets", "Blueberries", "Broccoli", "Brussels Sprouts", "Cabbage", "Carrots", "Cauliflower", "Corn", "Cucumbers", "Eggplant", "Grapes", "Lettuce", "Melons", "Onions", "Peppers", "Potatoes", "Pumpkins", "Radishes", "Spinach", "Summer Squash", "Winter Squash", "Tomatoes", "Turnips"),
    OCTOBER("Apples", "Beans", "Beets", "Blueberries", "Broccoli",  "Brussels Sprouts", "Cabbage", "Carrots", "Cauliflower", "Corn", "Cucumbers", "Eggplant", "Grapes", "Lettuce", "Onions", "Peppers", "Potatoes", "Pumpkins", "Radishes", "Spinach", "Summer Squash", "Winter Squash", "Tomatoes", "Turnips"),
    NOVEMBER("Broccoli", "Brussels Sprouts", "Cabbage", "Carrots", "Cauliflower", "Radishes", "Winter Squash", "Turnips"),
    DECEMBER("Broccoli", "Cabbage", "Carrots", "Cauliflower");

    private final Set<String> produce;

    NYSeasonalProduce(String... items) {
	this.produce = new HashSet<>(Arrays.asList(items));
    }

    public Set<String> getProduce() {
	return produce;
    }
}
