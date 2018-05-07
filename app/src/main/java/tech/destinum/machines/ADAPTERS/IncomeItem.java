package tech.destinum.machines.ADAPTERS;

import tech.destinum.machines.data.local.POJO.Income;

public class IncomeItem extends InfoItems {

    private Income mIncome;

    public Income getIncome() {
        return mIncome;
    }

    public void setIncome(Income income) {
        mIncome = income;
    }

    public IncomeItem(Income income) {

        mIncome = income;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
