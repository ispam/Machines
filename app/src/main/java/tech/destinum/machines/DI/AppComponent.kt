package tech.destinum.machines.DI

import javax.inject.Singleton

import dagger.Component
import tech.destinum.machines.Activities.Graph
import tech.destinum.machines.Activities.LineChart
import tech.destinum.machines.Activities.MachineInfo
import tech.destinum.machines.Activities.MainActivity

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: MachineInfo)

    fun inject(activity: Graph)

    fun inject(activity: LineChart)

}
