package com.example.bigfamilyv20.Utils;

import android.content.Context;
import android.os.AsyncTask;

public class BackgroundTask extends AsyncTask<String,Void,Void> {
    Context ctx;


    public BackgroundTask(Context ctx) {
        this.ctx=ctx;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String method=strings[0];
        if(method.equals("get_info"))
            {


            }
        else
            {


            }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
