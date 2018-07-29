package tech.destinum.machines.ADAPTERS

data class DateItem(var date: String) : InfoItems() {
    var month: Int = 0

    override fun getType(): Int {
        return InfoItems.TYPE_DATE
    }
}
