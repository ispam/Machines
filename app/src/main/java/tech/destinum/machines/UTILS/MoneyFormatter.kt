package tech.destinum.machines.UTILS

import java.text.DecimalFormat

object MoneyFormatter {

    fun moneyFormat(money: Double): String{
        val formatter = DecimalFormat("$#,##0.000")
        return formatter.format(money)
    }
}