package tech.destinum.machines.UTILS;

import android.support.v7.util.DiffUtil;

import java.util.List;

import tech.destinum.machines.ADAPTERS.DateItem;
import tech.destinum.machines.ADAPTERS.InfoItems;

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

        return false;

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }
}
