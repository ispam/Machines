package tech.destinum.machines.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tech.destinum.machines.data.local.ViewModel.IncomeViewModelFactory
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel
import tech.destinum.machines.data.local.ViewModel.MachineViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MachineViewModel::class)
    abstract fun getMachineViewModel(machineViewModel: MachineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    abstract fun getIncomeViewModel(incomeViewModel: IncomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: IncomeViewModelFactory): ViewModelProvider.Factory

}
