package tech.destinum.machines.ADAPTERS;

public abstract class InfoItems {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;

    abstract public int getType();

    private int info_items_id;

    public int getInfo_items_id() {
        return info_items_id;
    }

    public void setInfo_items_id(int info_items_id) {
        this.info_items_id = info_items_id;
    }
}
