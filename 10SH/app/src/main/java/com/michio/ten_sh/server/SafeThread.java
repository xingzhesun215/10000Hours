package com.michio.ten_sh.server;


public abstract class SafeThread extends Thread {
    public static String Tag = "SafeThread :";
    protected boolean isRunFlg = false;

    public void SafeStart() {
        if (isRunFlg == true)
            return;

        setRunFlg(true);
        start();
    }

    public void SafeStop() {
        if (isRunFlg == false)
            return;

        setRunFlg(false);
        TryJoin();
    }

    public boolean isRunFlg() {
        return isRunFlg;
    }

    //提供该方法分步控制，用于需要高性能的场合
    public void setRunFlg(boolean isRunFlg) {
        this.isRunFlg = isRunFlg;
    }

    //提供该方法分步控制，用于需要高性能的场合
    public void TryJoin() {
        try {
            join();
        } catch (InterruptedException e) {
            System.out.println(Tag + " err : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
