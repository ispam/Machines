package tech.destinum.machines.Adapters

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView


import com.daimajia.swipe.SwipeLayout

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import tech.destinum.machines.UTILS.NumberTextWatcher
import tech.destinum.machines.Data.Local.Entities.Income
import tech.destinum.machines.R
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModel


class ListAdapter(private val mContext: Context, private val mInfoItems: List<InfoItems>?, private val incomeViewModel: IncomeViewModel, private val name: String, private val id: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = ListAdapter::class.java.simpleName
    }

    private val disposable = CompositeDisposable()
    private val publishSubject = PublishSubject.create<Long>()
    var clickEvent: Observable<Long> = publishSubject

    override fun getItemViewType(position: Int): Int {
        return mInfoItems!![position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            InfoItems.TYPE_DATE -> DateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.format_date, parent, false))
            InfoItems.TYPE_GENERAL -> GeneralViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_list, parent, false))

            else -> throw IllegalStateException("Unsupported item type")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            InfoItems.TYPE_DATE -> {
                val dateItem = mInfoItems!![position] as DateItem
                val dateViewHolder = holder as DateViewHolder

               dateViewHolder.bind(dateItem)
            }

            InfoItems.TYPE_GENERAL -> {
                val income = mInfoItems!![position] as IncomeItem
                val generalViewHolder = holder as GeneralViewHolder

                generalViewHolder.bind(income.income)

            }
            else -> throw IllegalStateException("Unsupported item Type")
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {

        if (disposable != null && !disposable.isDisposed) {
            disposable.clear()
        }

        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return mInfoItems?.size ?: 0
    }


    inner class GeneralViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val mNote: TextView = v.findViewById(R.id.notes_list_note)
        private val mMoney: TextView = v.findViewById(R.id.notes_list_money)
        private val mDate: TextView = v.findViewById(R.id.notes_list_date)
        private val mSwipeLayout: SwipeLayout = v.findViewById(R.id.swipe_notes_list)
        private val mDelete: ImageView = v.findViewById(R.id.trash)
        private val mShare: ImageView = v.findViewById(R.id.share)
        private val mEdit: ImageView = v.findViewById(R.id.edit)


        fun bind(income: Income){

            val formatter = DecimalFormat("$#,##0.000")
            val formatted = formatter.format(income.money)

            mNote.text = income.note
            mMoney.text = formatted

            val dateLong = income.date
            val sdf3 = SimpleDateFormat("dd/MM/yyyy")
            val dateString = sdf3.format(Date(dateLong))
            mDate.text = dateString



            mSwipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            //        mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.mRelativeLayout);
            mSwipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {

                }

                override fun onOpen(layout: SwipeLayout) {

                }

                override fun onStartClose(layout: SwipeLayout) {

                }

                override fun onClose(layout: SwipeLayout) {

                }

                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {

                }

                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {

                }
            })

            mShare.setOnClickListener { v ->

                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Maquina: $name\nFecha: $dateString\nRecaudado: $formatted")
                v.context.startActivity(Intent.createChooser(sendIntent, "Compartir"))
            }

            mDelete.setOnClickListener { v ->
                val dialogg = AlertDialog.Builder(mContext)
                dialogg.setTitle(Html.fromHtml("<font color='black'>Confirmación</font>")).setMessage(Html.fromHtml("<font color='black'>Segura de <b>BORRAR</b> el ingreso: \n<font color='#7a0843'>$formatted</font></font>"))
                        .setNegativeButton("No", null)
                        .setPositiveButton("Si") { dialog, which ->
                            publishSubject.onNext(income._id)
                            notifyDataSetChanged()
                        }
                dialogg.create()
                dialogg.show()
            }

            mEdit.setOnClickListener { v ->
                val dialog = AlertDialog.Builder(v.context)

                val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_update_income, null, true)
                val edt = dialogView.findViewById<EditText>(R.id.dialog_edt_date)
                edt.addTextChangedListener(NumberTextWatcher(edt))
                val msg = dialogView.findViewById<TextView>(R.id.dialog_tv_msg)

                msg.text = Html.fromHtml("Esta remplazando el ingreso de la fecha: \n<font size='18px' color='#7a0843'>$dateString</font>")

                dialog.setNegativeButton("Cancelar", null)
                        .setPositiveButton("Cambiar") { dialog1, which ->

                            val money = edt.text.toString()
                            if (money == "" || money.isEmpty()) {
                                Toast.makeText(v.context, "Necesitamos algun dato", Toast.LENGTH_SHORT).show()

                            } else {
                                val value: Double
                                value = java.lang.Double.parseDouble(edt.text.toString())

                                disposable.add(incomeViewModel.updateIncomeByID(income._id, income.note, value)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe { Log.d(TAG, "ListAdapter: INCOME UPDATED") })

                                dialog1.dismiss()
                            }
                        }
                dialog.setView(dialogView).show()
            }
        }
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mDate: TextView = view.findViewById(R.id.format_date)
        private val mTotalMonth: TextView = view.findViewById(R.id.format_date_total_month)

        fun bind(dateItem: DateItem){
            var dateString = dateItem.date
            val sdf = SimpleDateFormat("MMMM")

            var date1: Date? = null
            try {
                date1 = sdf.parse(dateString)

                val numDate = SimpleDateFormat("M")
                val format2 = numDate.format(date1)
                val month = Integer.parseInt(format2)

                disposable.add(incomeViewModel.getTotalMonth(id, month)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { total_month ->
                            val formatter = DecimalFormat("$#,##0.000")
                            val format = formatter.format(total_month)
                            mTotalMonth.text = format
                        })

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            mDate.text = dateItem.date.capitalize()
        }
    }
}
