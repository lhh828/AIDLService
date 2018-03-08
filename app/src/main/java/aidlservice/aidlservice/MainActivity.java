package aidlservice.aidlservice;

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

public class MainActivity extends AppCompatActivity {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("TAG", "建立连接");

            AIDLInterface binder = AIDLInterface.Stub.asInterface(service);
            try {

                Person bean = binder.addPerson(new Person("张三", 22));
                Person person = binder.getPerson();


                Toast.makeText(MainActivity.this,bean.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,person.toString(),Toast.LENGTH_SHORT).show();
                Log.d("info",bean.toString());
                Log.d("info",person.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
    }

    public void bind(View view) {
        Intent it = new Intent(this,MyService.class);
        isbind = bindService(it, connection, BIND_AUTO_CREATE);
    }

    public void unbind(View view) {
        if(isbind) {
            unbindService(connection);
            isbind = false;
        }
    }
}
