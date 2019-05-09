package tech.destinum.machines.UTILS;


import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import tech.destinum.machines.Adapters.InfoItems;

public class InfoItemCallback extends DiffUtil.Callback {

    private List<InfoItems> mOldList;
    private List<InfoItems> mNewList;

    public InfoItemCallback(List<InfoItems> oldList, List<InfoItems> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        InfoItems currentItem = mOldList.get(oldItemPosition);
        InfoItems nextItem = mNewList.get(newItemPosition);

        return currentItem.getInfo_items_id() == nextItem.getInfo_items_id();

//        DateItem dateItem = (DateItem) mOldList.get(oldItemPosition);
//
//        IncomeItem incomeItem = (IncomeItem) mOldList.get(oldItemPosition);
//        Income income = incomeItem.getIncome();

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        InfoItems currentItem = mOldList.get(oldItemPosition);
        InfoItems nextItem = mNewList.get(newItemPosition);
        return currentItem.equals(nextItem);
    }
}
