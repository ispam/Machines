package tech.destinum.machines.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModelFactory;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MachineViewModel.class)
    abstract ViewModel getMachineViewModel(MachineViewModel machineViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel.class)
    abstract ViewModel getIncomeViewModel(IncomeViewModel incomeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(IncomeViewModelFactory factory);

}
