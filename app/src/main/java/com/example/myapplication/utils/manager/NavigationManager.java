package com.example.myapplication.utils.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

public class NavigationManager {

    private static NavigationManager instance;

    private NavigationManager() {}

    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public <T, D extends Serializable> void navigateToDestinationWithData(Context context, Class<T> destinationClass, D data) {
        if (context != null) {
            Intent intent = new Intent(context, destinationClass);
            intent.putExtra("data", data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG", "Error al cambiar a la pantalla");
        }
    }

    public <T> void navigateToDestination(Context context, Class<T> destinationClass) {
        navigateToDestinationWithData(context, destinationClass, null);
    }
}

