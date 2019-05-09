package tech.destinum.machines.DI


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModelFactory
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModel
import tech.destinum.machines.Data.Local.ViewModel.MachineViewModel

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
