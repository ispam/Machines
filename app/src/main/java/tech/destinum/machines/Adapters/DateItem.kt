package tech.destinum.machines.Adapters

data class DateItem(var date: String) : InfoItems() {
    var month: Int = 0

    override val type: Int
        get() = InfoItems.TYPE_DATE
}
