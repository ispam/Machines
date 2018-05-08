package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.UTILS.NumberTextWatcher;
import tech.destinum.machines.data.local.POJO.Income;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;



public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<InfoItems> mInfoItems;
    private List<Income> mIncomeArrayList;
    private CompositeDisposable disposable = new CompositeDisposable();
    private IncomeViewModel incomeViewModel;
    private PublishSubject<Long> publishSubject = PublishSubject.create();
    public Observable<Long> clickEvent = publishSubject;
    private String name;

    private static final String TAG = ListAdapter.class.getSimpleName();

    public ListAdapter(Context context, List<InfoItems> infoItems, IncomeViewModel incomeViewModel, String name) {
        mContext = context;
        mInfoItems = infoItems;
        this.incomeViewModel = incomeViewModel;
        this.name = name;
    }

    @Override
    public int getItemViewType(int position) {
        return mInfoItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case InfoItems.TYPE_DATE:
                return new DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.format_date, parent, false));
            case InfoItems.TYPE_GENERAL:
                return new GeneralViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false));

                default: throw new IllegalStateException("Unsupported item type");
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case InfoItems.TYPE_DATE:
                DateItem dateItem = (DateItem) mInfoItems.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;

                dateViewHolder.mDate.setText(dateItem.getDate());

                break;

            case InfoItems.TYPE_GENERAL:
                IncomeItem incomeItem = (IncomeItem) mInfoItems.get(position);
                Income income = incomeItem.getIncome();
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;

                DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                String formatted = formatter.format(income.getMoney());

                generalViewHolder.mNote.setText(income.getNote());
                generalViewHolder.mMoney.setText(formatted);

                long dateLong = income.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = sdf.format(new Date(dateLong));
                generalViewHolder.mDate.setText(dateString);



                generalViewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//        generalViewHolder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.mRelativeLayout);
                generalViewHolder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) {

                    }

                    @Override
                    public void onOpen(SwipeLayout layout) {

                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {

                    }

                    @Override
                    public void onClose(SwipeLayout layout) {

                    }

                    @Override
                    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                    }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                    }
                });

                generalViewHolder.mShare.setOnClickListener(v -> {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Maquina: " + name + "\nFecha: " + dateString + "\n" + "Recaudado: " + formatted);
                    v.getContext().startActivity(Intent.createChooser(sendIntent, "Compartir"));
                });

                generalViewHolder.mDelete.setOnClickListener(v -> {
                    AlertDialog.Builder dialogg = new AlertDialog.Builder(mContext);
                    dialogg.setTitle(Html.fromHtml("<font color='black'>Confirmación</font>")).setMessage(Html.fromHtml("<font color='black'>Segura de <b>BORRAR</b> el ingreso: \n<font color='#7a0843'>" + formatted + "</font></font>"))
                            .setNegativeButton("No", null)
                            .setPositiveButton("Si", (dialog, which) -> {
                                publishSubject.onNext(income.get_id());
                                notifyDataSetChanged();
                            });
                    dialogg.create();
                    dialogg.show();
                });

                generalViewHolder.mEdit.setOnClickListener(v -> {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

                    LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.dialog_update_income, null, true);
                    EditText edt = dialogView.findViewById(R.id.dialog_edt_date);
                    edt.addTextChangedListener(new NumberTextWatcher(edt));
                    TextView msg = dialogView.findViewById(R.id.dialog_tv_msg);

                    msg.setText(Html.fromHtml("Esta remplazando el ingreso de la fecha: \n<font size='18px' color='#7a0843'>" + dateString + "</font>"));

                    dialog.setNegativeButton("Cancelar", null)
                            .setPositiveButton("Cambiar", (dialog1, which) -> {

                                String money = edt.getText().toString();
                                if (money.equals("") || money.isEmpty()) {
                                    Toast.makeText(v.getContext(), "Necesitamos algun dato", Toast.LENGTH_SHORT).show();

                                } else {
                                    double value;
                                    value = Double.parseDouble(edt.getText().toString());

                                    disposable.add(incomeViewModel.updateIncomeByID(income.get_id(), income.getNote(), value)
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(() -> Log.d(TAG, "ListAdapter: INCOME UPDATED")));

                                    dialog1.dismiss();
                                }
                            });
                    dialog.setView(dialogView).show();
                });

                break;

            default:
                throw new IllegalStateException("Unsupported item Type");
        }

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }

        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {return mInfoItems != null ? mInfoItems.size(): 0;
    }


    public class GeneralViewHolder extends RecyclerView.ViewHolder {

        private TextView mNote, mMoney, mDate;
        private SwipeLayout mSwipeLayout;
        private ImageView mDelete, mShare, mEdit;

        private GeneralViewHolder(View v) {
            super(v);

            mNote = v.findViewById(R.id.notes_list_note);
            mMoney = v.findViewById(R.id.notes_list_money);
            mDate = v.findViewById(R.id.notes_list_date);
            mDelete = v.findViewById(R.id.trash);
            mShare = v.findViewById(R.id.share);
            mEdit = v.findViewById(R.id.edit);
            mSwipeLayout = v.findViewById(R.id.swipe_notes_list);
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{

        private TextView mDate, mTotalMonth;

        public DateViewHolder(View view) {
            super(view);

            mDate = view.findViewById(R.id.format_date);
            mTotalMonth = view.findViewById(R.id.format_date_total_month);
        }
    }
}
