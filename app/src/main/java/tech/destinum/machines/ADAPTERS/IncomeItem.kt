package tech.destinum.machines.ADAPTERS

import tech.destinum.machines.data.local.POJO.Income

data class IncomeItem(var income: Income) : InfoItems() {

    override fun getType(): Int {
        return InfoItems.TYPE_GENERAL
    }
}
