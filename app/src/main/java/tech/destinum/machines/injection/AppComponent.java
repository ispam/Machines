package tech.destinum.machines.injection;

import javax.inject.Singleton;

import dagger.Component;
import tech.destinum.machines.ACTIVITIES.Graph;
import tech.destinum.machines.ACTIVITIES.LineChart;
import tech.destinum.machines.ACTIVITIES.MachineInfo;
import tech.destinum.machines.ACTIVITIES.MainActivity;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(MachineInfo activity);

    void inject(Graph activity);

    void inject(LineChart activity);

}
