package tech.destinum.machines.di

import javax.inject.Singleton

import dagger.Component
import dagger.Provides
import tech.destinum.machines.ACTIVITIES.Graph
import tech.destinum.machines.ACTIVITIES.LineChart
import tech.destinum.machines.ACTIVITIES.MachineInfo
import tech.destinum.machines.ACTIVITIES.MainActivity

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: MachineInfo)

    fun inject(activity: Graph)

    fun inject(activity: LineChart)

}
