package tech.destinum.machines.POJO;

public class Income {

    private String date, note;
    private Double money;
    private long id, mMachines_id;

    public Income(Double money, String date, String note, long id) {
        this.date = date;
        this.note = note;
        this.money = money;
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getMoney() {

        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public long getMachines_id() {
        return mMachines_id;
    }

    public void setMachines_id(long machines_id) {
        mMachines_id = machines_id;
    }
}

