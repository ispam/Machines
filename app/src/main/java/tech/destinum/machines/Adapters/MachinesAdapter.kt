package tech.destinum.machines.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay


import java.text.DecimalFormat

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import tech.destinum.machines.Activities.MachineInfo
import tech.destinum.machines.Data.Local.Entities.Machine
import tech.destinum.machines.R

class MachinesAdapter(private val machinesList: List<Machine>?, private val mContext: Context) : RecyclerView.Adapter<MachinesAdapter.MachineViewHolder>() {

    companion object {
        private val TAG = MachinesAdapter::class.java.simpleName
    }

    private val disposable = CompositeDisposable()
    private val clickSubject: PublishRelay<Long> = PublishRelay.create()
    var clickEvent: Observable<Long> = clickSubject


    //    public synchronized void refreshAdapter(List<Double> mNewMachines){
    //        incomeList.clear();
    //        incomeList.addAll(mNewMachines);
    //        notifyDataSetChanged();
    //    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachinesAdapter.MachineViewHolder {
        return MachineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.machines_list, parent, false))
    }

    override fun onBindViewHolder(holder: MachineViewHolder, position: Int) {

        val machine = machinesList!![position]
        holder.populate(machine)

        RxView.clicks(holder.itemView)
                .doOnNext {
                    val intent = Intent(mContext, MachineInfo::class.java)
                    intent.putExtra("id", machine.id)
                    intent.putExtra("name", machine.name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }.subscribe()

        RxView.longClicks(holder.itemView)
                .doOnNext {
                    val dialogg = AlertDialog.Builder(mContext)
                    dialogg.setTitle(Html.fromHtml("<font color='black'>Confirmación</font>")).setMessage(Html.fromHtml("<font color='black'>Segura de <b>BORRAR</b> la maquina: <b>" + machinesList[position].name + "</b></font>"))
                            .setNegativeButton("No", null)
                            .setPositiveButton("Si") { dialog, which -> clickSubject.accept(machine.id) }
                    dialogg.create()
                    dialogg.show()
                }
                .subscribe()

    }

    override fun getItemCount(): Int {
        return machinesList?.size ?: 0
    }

    inner class MachineViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val mName: TextView = v.findViewById(R.id.machines_list_name)
        private val mMoney: TextView = v.findViewById(R.id.machines_list_total)

        fun populate(machine: Machine) {
            mName.text = machine.name

            val total_amount = machine.total_income

            if (total_amount <= 0) {
                mMoney.text = "$0.0"
            } else {
                val formatter = DecimalFormat("$#,##0.000")
                val formatted = formatter.format(total_amount)
                mMoney.text = formatted
            }
        }
    }



    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onDetachedFromRecyclerView(recyclerView)
    }
}
