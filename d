[1mdiff --git a/.idea/caches/build_file_checksums.ser b/.idea/caches/build_file_checksums.ser[m
[1mindex 0e7e8b2..dd69aeb 100644[m
Binary files a/.idea/caches/build_file_checksums.ser and b/.idea/caches/build_file_checksums.ser differ
[1mdiff --git a/app/build.gradle b/app/build.gradle[m
[1mindex 36ebd4e..5c789b8 100644[m
[1m--- a/app/build.gradle[m
[1m+++ b/app/build.gradle[m
[36m@@ -2,7 +2,7 @@[m [mapply plugin: 'com.android.application'[m
 [m
 android {[m
     compileSdkVersion 27[m
[31m-    buildToolsVersion '27.0.2'[m
[32m+[m[32m    buildToolsVersion '27.0.3'[m
     defaultConfig {[m
         applicationId "tech.destinum.machines"[m
         minSdkVersion 19[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/ACTIVITIES/MachineInfo.java b/app/src/main/java/tech/destinum/machines/ACTIVITIES/MachineInfo.java[m
[1mindex 46d121c..ae4b522 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/ACTIVITIES/MachineInfo.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/ACTIVITIES/MachineInfo.java[m
[36m@@ -115,7 +115,12 @@[m [mpublic class MachineInfo extends AppCompatActivity implements DatePickerDialog.O[m
                                 hideSoftKeyboard(v);[m
                             } else {[m
                                 value = Double.parseDouble(money);[m
[31m-                                incomeViewModel.addIncome(date, notes, value, id);[m
[32m+[m[32m                                disposable.add(incomeViewModel.addIncome(date, notes, value, id)[m
[32m+[m[32m                                                .subscribeOn(Schedulers.io())[m
[32m+[m[32m                                                .observeOn(AndroidSchedulers.mainThread())[m
[32m+[m[32m                                                .subscribe([m
[32m+[m[32m                                                        () -> {},[m
[32m+[m[32m                                                        throwable -> Log.e(TAG, "MachineInfo: ", throwable)));[m
                             }[m
 [m
                             hideSoftKeyboard(v);[m
[36m@@ -148,6 +153,7 @@[m [mpublic class MachineInfo extends AppCompatActivity implements DatePickerDialog.O[m
                             String formatted = formatter.format(total_amount);[m
                             mMoney.setText(formatted);[m
                             Log.d(TAG, "MachineInfo: money" + formatted);[m
[32m+[m[32m                            showMenu = true;[m
                         } else {[m
                             mMoney.setText("$0.0");[m
                         }[m
[36m@@ -186,7 +192,7 @@[m [mpublic class MachineInfo extends AppCompatActivity implements DatePickerDialog.O[m
 [m
     @Override[m
     public boolean onCreateOptionsMenu(Menu menu) {[m
[31m-        if (showMenu == true){[m
[32m+[m[32m        if (showMenu){[m
             getMenuInflater().inflate(R.menu.menu_line_chart, menu);[m
             return super.onCreateOptionsMenu(menu);[m
         } else {[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/ACTIVITIES/MainActivity.java b/app/src/main/java/tech/destinum/machines/ACTIVITIES/MainActivity.java[m
[1mindex 8c279cc..327ce87 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/ACTIVITIES/MainActivity.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/ACTIVITIES/MainActivity.java[m
[36m@@ -49,8 +49,6 @@[m [mpublic class MainActivity extends AppCompatActivity {[m
     private RecyclerView mRecyclerView;[m
     private MachinesAdapter mAdapter;[m
 [m
[31m-    @Inject MachinesDB db;[m
[31m-[m
     @Inject[m
     MachineViewModel machineViewModel;[m
 [m
[36m@@ -82,8 +80,12 @@[m [mpublic class MainActivity extends AppCompatActivity {[m
                     .setPositiveButton("Crear", (dialog, which) -> {[m
 [m
                         String machine = mEditText.getText().toString();[m
[31m-                        machineViewModel.addMachine(machine);[m
[31m-                        mAdapter.notifyDataSetChanged();[m
[32m+[m[32m                        disposable.add(machineViewModel.addMachine(machine)[m
[32m+[m[32m                                .subscribeOn(Schedulers.io())[m
[32m+[m[32m                                .observeOn(AndroidSchedulers.mainThread())[m
[32m+[m[32m                                .subscribe([m
[32m+[m[32m                                        () -> mAdapter.notifyDataSetChanged(),[m
[32m+[m[32m                                        throwable -> Log.e(TAG, "MachineInfo: ", throwable)));[m
 [m
             }).setView(view).show();[m
         });[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/data/MachinesDB.java b/app/src/main/java/tech/destinum/machines/data/MachinesDB.java[m
[1mindex 809ff4f..ba739fc 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/data/MachinesDB.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/data/MachinesDB.java[m
[36m@@ -14,16 +14,16 @@[m [mimport tech.destinum.machines.data.dao.MachineDAO;[m
 public abstract class MachinesDB extends RoomDatabase{[m
     private static MachinesDB INSTANCE;[m
 [m
[31m-    public static MachinesDB getDB(Context context){[m
[31m-        if (INSTANCE == null){[m
[31m-            INSTANCE =[m
[31m-                    Room.databaseBuilder([m
[31m-                            context.getApplicationContext(),[m
[31m-                            MachinesDB.class,[m
[31m-                            "machines_db").build();[m
[31m-        }[m
[31m-        return  INSTANCE;[m
[31m-    }[m
[32m+[m[32m//    public static MachinesDB getDB(Context context){[m
[32m+[m[32m//        if (INSTANCE == null){[m
[32m+[m[32m//            INSTANCE =[m
[32m+[m[32m//                    Room.databaseBuilder([m
[32m+[m[32m//                            context.getApplicationContext(),[m
[32m+[m[32m//                            MachinesDB.class,[m
[32m+[m[32m//                            "machines_db").build();[m
[32m+[m[32m//        }[m
[32m+[m[32m//        return  INSTANCE;[m
[32m+[m[32m//    }[m
     public abstract IncomeDAO getIncomeDAO();[m
 [m
     public abstract MachineDAO getMachineDAO();[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/data/ViewModel/IncomeViewModel.java b/app/src/main/java/tech/destinum/machines/data/ViewModel/IncomeViewModel.java[m
[1mindex d6fe749..c3277e5 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/data/ViewModel/IncomeViewModel.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/data/ViewModel/IncomeViewModel.java[m
[36m@@ -32,8 +32,8 @@[m [mpublic class IncomeViewModel {[m
     public Flowable<List<Double>> getAllIncomesFromAllMachines(){[m
         return machinesDB.getIncomeDAO().getAllIncomesFromAllMachines();[m
     }[m
[31m-    public void addIncome(String date, String note, Double money, Long machines_id){[m
[31m-       machinesDB.getIncomeDAO().addIncome(new Income(date, note, money, machines_id));[m
[32m+[m[32m    public Completable addIncome(String date, String note, Double money, Long machines_id){[m
[32m+[m[32m        return Completable.fromAction(() -> machinesDB.getIncomeDAO().addIncome(new Income(date, note, money, machines_id)));[m
     }[m
 [m
     public Flowable<List<Income>> getAllMachinesIncome(){[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/data/ViewModel/MachineViewModel.java b/app/src/main/java/tech/destinum/machines/data/ViewModel/MachineViewModel.java[m
[1mindex dcf4c30..322bc00 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/data/ViewModel/MachineViewModel.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/data/ViewModel/MachineViewModel.java[m
[36m@@ -30,30 +30,14 @@[m [mpublic class MachineViewModel {[m
         this.machinesDB = machinesDB;[m
     }[m
 [m
[31m-    public void addMachine(final String name){[m
[31m-        machinesDB.getMachineDAO().addMachine(new Machine(name));[m
[32m+[m[32m    public Completable addMachine(final String name){[m
[32m+[m[32m        return Completable.fromAction(() -> machinesDB.getMachineDAO().addMachine(new Machine(name)));[m
     }[m
 [m
     public void deleteMachine(Machine machine){[m
         machinesDB.getMachineDAO().deleteMachine(machine);[m
     }[m
 [m
[31m-    public Maybe getIncomeOfMachine(long id){[m
[31m-        return new Maybe() {[m
[31m-            @Override[m
[31m-            protected void subscribeActual(MaybeObserver observer) {[m
[31m-                Maybe.just(new Optional(machinesDB.getIncomeDAO().getIncomeOfMachine(id)))[m
[31m-                        .subscribe(optional -> {[m
[31m-                        if (optional.isEmpty()) {[m
[31m-                            Log.d(TAG, "Object is null");[m
[31m-                            Double nada = 0.0;[m
[31m-                        } else {[m
[31m-                            Log.d(TAG, "Object value is " + optional.get());[m
[31m-                        }[m
[31m-                    });[m
[31m-            }[m
[31m-        };[m
[31m-    }[m
 [m
     public Flowable<List<Machine>> getAllMachines(){[m
         return machinesDB.getMachineDAO().getAllMachines2();[m
[1mdiff --git a/app/src/main/java/tech/destinum/machines/injection/AppModule.java b/app/src/main/java/tech/destinum/machines/injection/AppModule.java[m
[1mindex b90fc4a..36e54d0 100644[m
[1m--- a/app/src/main/java/tech/destinum/machines/injection/AppModule.java[m
[1m+++ b/app/src/main/java/tech/destinum/machines/injection/AppModule.java[m
[36m@@ -52,15 +52,14 @@[m [mpublic class AppModule {[m
         return getInstance(context);[m
     }[m
 [m
[31m-    public static MachinesDB getInstance(Context context){[m
[32m+[m[32m    private static MachinesDB getInstance(Context context){[m
 [m
         if (instance == null){[m
[31m-            instance = Room.databaseBuilder(context.getApplicationContext(),[m
[31m-                    MachinesDB.class,[m
[31m-                    DB_NAME)[m
[31m-                    .allowMainThreadQueries()[m
[31m-                    .fallbackToDestructiveMigration()[m
[31m-                    .build();[m
[32m+[m[32m            instance =[m
[32m+[m[32m                    Room.databaseBuilder([m
[32m+[m[32m                            context.getApplicationContext(),[m
[32m+[m[32m                            MachinesDB.class,[m
[32m+[m[32m                            DB_NAME).build();[m
         }[m
         return instance;[m
     }[m
