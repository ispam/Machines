package tech.destinum.machines.Adapters

import tech.destinum.machines.Data.Local.Entities.Income

data class IncomeItem(var income: Income) : InfoItems() {

    override val type: Int
        get() = InfoItems.TYPE_GENERAL
}
