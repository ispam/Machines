package tech.destinum.machines;

public class IncomeClass {

    private String date, note;
    private Double money;
    private long id;
    private MachinesClass mMachinesClass;

    public IncomeClass(double money, String date, String note, long id){
        this.money = money;
        this.date = date;
        this.note = note;
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

    public MachinesClass getMachinesClass(){
        return mMachinesClass;
    }

    public void setMachinesClass(MachinesClass mMachinesClass){
        this.mMachinesClass = mMachinesClass;
    }

}
