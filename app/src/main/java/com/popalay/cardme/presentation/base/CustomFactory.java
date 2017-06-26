package com.popalay.cardme.presentation.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.popalay.cardme.App;
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsViewModel;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("WeakerAccess")
public class CustomFactory extends ViewModelProvider.NewInstanceFactory {

    private final App application;
    private final NavigationExtrasHolder navigationExtras;

    public CustomFactory(@NonNull App application, NavigationExtrasHolder navigationExtras) {
        this.application = application;
        this.navigationExtras = navigationExtras;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        try {
            if (modelClass.isAssignableFrom(HolderDetailsViewModel.class)) {
                return modelClass.getConstructor(Application.class, long.class)
                        .newInstance(application, navigationExtras.getHolderId());
            } else if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                return modelClass.getConstructor(Application.class).newInstance(application);
            }
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
        return super.create(modelClass);
    }
}