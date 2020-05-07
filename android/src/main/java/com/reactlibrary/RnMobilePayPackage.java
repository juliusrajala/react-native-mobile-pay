package com.rnmobilepay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.rnmobilepay.RnMobilePayModule;

public class RnMobilePayPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        RnMobilePayModule tester = new RnMobilePayModule((reactContext));
        System.out.println("ReactContext");
        System.out.println(reactContext.toString());

        modules.add(new RnMobilePayModule(reactContext));

        System.out.println(tester.toString());

        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
