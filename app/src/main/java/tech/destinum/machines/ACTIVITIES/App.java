package tech.destinum.machines.ACTIVITIES;

import android.app.Application;

import tech.destinum.machines.injection.AppModule;
import tech.destinum.machines.injection.DaggerMainComponent;
import tech.destinum.machines.injection.MainComponent;

public class App extends Application {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    MainComponent getComponent(){
        if (component==null){
            component = DaggerMainComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return component;
    }
}
