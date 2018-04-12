package tech.destinum.machines.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import tech.destinum.machines.data.local.POJO.Income;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;



public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context mContext;
    private List<Income> mIncomeArrayList;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MachineViewModel machineViewModel;
    private IncomeViewModel incomeViewModel;
    private PublishSubject<Long> publishSubject = PublishSubject.create();
    public Observable<Long> clickEvent = publishSubject;
    private static final String TAG = ListAdapter.class.getSimpleName();

    public ListAdapter(Context context, List<Income> incomeArrayList, MachineViewModel machineViewModel, IncomeViewModel incomeViewModel) {
        mContext = context;
        mIncomeArrayList = incomeArrayList;
        this.machineViewModel = machineViewModel;
        this.incomeViewModel = incomeViewModel;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {

        Income income = mIncomeArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(income.getMoney());

        holder.mNote.setText(income.getNote());
        holder.mMoney.setText(formatted);
        holder.mDate.setText(income.getDate());

        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.mRelativeLayout);
        holder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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

        holder.mShare.setOnClickListener(v -> {

            String name = "";
            disposable.add(
                    machineViewModel.getMachineName(income.getMachines_id())
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(name2 -> name2 = name, throwable -> Log.e(TAG, "ListAdapter: ERROR",  throwable)));

            String date = income.getDate();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Maquina: " + name + "\nFecha: "+date+"\n"+"Recaudado: "+formatted);
            v.getContext().startActivity(Intent.createChooser(sendIntent, "Compartir"));
        });

        holder.mDelete.setOnClickListener(v -> {
            AlertDialog.Builder dialogg = new AlertDialog.Builder(mContext);
            dialogg.setTitle(Html.fromHtml("<font color='black'>Confirmación</font>")).setMessage(Html.fromHtml("<font color='black'>Segura de <b>BORRAR</b> el ingreso: \n<b>" + formatted+ "</b></font>"))
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", (dialog, which)-> publishSubject.onNext(income.get_id()));
            dialogg.create();
            dialogg.show();
            });

        holder.mEdit.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView =inflater.inflate(R.layout.dialog_update_income, null, true);
            EditText edt = dialogView.findViewById(R.id.dialog_edt_date);
            TextView msg = dialogView.findViewById(R.id.dialog_tv_msg) ;

            msg.setText("Esta remplazando el ingreso de la fecha: "+ income.getDate());

            dialog.setNegativeButton("Cancelar", null)
                    .setPositiveButton("Cambiar", (dialog1, which) -> {

                        String money = edt.getText().toString();
                        if (money.equals("") || money.isEmpty()){
                            Toast.makeText(v.getContext(), "Necesitamos algun dato", Toast.LENGTH_SHORT).show();

                        } else {
                            double value;
                            value = Double.parseDouble(edt.getText().toString());

                            disposable.add(incomeViewModel.updateIncomeByID(income.get_id(), income.getNote(), value)
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(()-> Log.d(TAG, "ListAdapter: INCOME UPDATED")));

                            dialog1.dismiss();
                        }
                    });
            dialog.setView(dialogView).show();
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }

        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {return mIncomeArrayList != null ? mIncomeArrayList.size(): 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNote, mMoney, mDate;
        private SwipeLayout mSwipeLayout;
        private ImageView mDelete, mShare, mEdit;

        private ViewHolder(View v) {
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
}
