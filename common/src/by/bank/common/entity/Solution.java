package by.bank.common.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class Solution extends Entity {

    private Set<Item> items;
    private String title;
    private String description;

    public Solution() {
        items = new TreeSet<>();
    }

    public Set<Item> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Item implements Serializable, Comparable<Item> {

        private double rate;
        private String description;
        private int ordinal;

        public Item() {
            rate = 0.0;
            description = "";
        }

        public Item(double rate, String description, int ordinal) {
            this.rate = rate;
            this.description = description;
            this.ordinal = ordinal;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(int ordinal) {
            this.ordinal = ordinal;
        }

        public String getFullDescription() {
            return String.format("A%d: %s", ordinal, description);
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare(o.rate, this.rate);
        }
    }
}
