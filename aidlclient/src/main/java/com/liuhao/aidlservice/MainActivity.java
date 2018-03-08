package com.liuhao.aidlservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import aidlservice.aidlservice.AIDLInterface;
import aidlservice.aidlservice.Person;

public class MainActivity extends AppCompatActivity {

    private AIDLInterface binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("TAG", "建立连接");

            binder = AIDLInterface.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("TAG", "解除连接");
        }
    };
    private boolean isbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent it = new Intent();
        it.setAction("com.liuhao.aidlservice");
        it.setPackage("aidlservice.aidlservice");

        isbind = bindService(it, connection, BIND_AUTO_CREATE);
    }

    public void bind(View view) {
        try {

            Person bean = binder.addPerson(new Person("张三", 22));
            Person person = binder.getPerson();


            Toast.makeText(MainActivity.this,"客户端" + bean.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this,"客户端" +  person.toString(),Toast.LENGTH_SHORT).show();
            Log.d("info","客户端" + bean.toString());
            Log.d("info","客户端" + person.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unbind(View view) {
        if(isbind) {
            unbindService(connection);
            isbind = false;
        }
    }
}
