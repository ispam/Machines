package tech.destinum.machines.ADAPTERS;

public class DateItem extends InfoItems {

    private String date;
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public DateItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
