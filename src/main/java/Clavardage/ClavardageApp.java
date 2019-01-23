package Clavardage;

import Clavardage.MODEL.MulticastModel;

public class ClavardageApp {

    private ClavardageApp(){

    }

    private static ClavardageApp INSTANCE = null;

    public static ClavardageApp GetInstance(){
        if (INSTANCE == null){
            INSTANCE = new ClavardageApp();
        }
        return INSTANCE;
    }

    public void LogOut(){
        MulticastModel.ShutdownMulticast();
    }
}
