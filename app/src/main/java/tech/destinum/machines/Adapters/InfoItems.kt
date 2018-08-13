package tech.destinum.machines.Adapters

abstract class InfoItems {

    abstract val type: Int

    var info_items_id: Int = 0

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }
}
