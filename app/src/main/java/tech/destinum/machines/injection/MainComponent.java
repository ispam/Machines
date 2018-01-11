package tech.destinum.machines.injection;

import dagger.Component;
import tech.destinum.machines.ACTIVITIES.MainActivity;

@Component(modules = {AppModule.class})
public interface MainComponent {

    void injectMainActivity(MainActivity activity);
}
