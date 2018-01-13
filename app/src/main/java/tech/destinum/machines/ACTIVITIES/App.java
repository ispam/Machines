package tech.destinum.machines.ACTIVITIES;

import android.app.Application;

import tech.destinum.machines.injection.AppComponent;
import tech.destinum.machines.injection.AppModule;
import tech.destinum.machines.injection.DaggerAppComponent;

public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    AppComponent getComponent(){
        if (component==null){
            component = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return component;
    }
}
