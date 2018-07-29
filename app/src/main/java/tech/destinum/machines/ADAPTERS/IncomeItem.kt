package tech.destinum.machines.ADAPTERS

import tech.destinum.machines.data.local.POJO.Income

data class IncomeItem(var income: Income) : InfoItems() {

    override val type: Int
        get() = InfoItems.TYPE_GENERAL
}
