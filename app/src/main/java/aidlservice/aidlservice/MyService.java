package aidlservice.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by ${lhh} on 2017/12/27.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyAidlService();
    }


    class MyAidlService extends AIDLInterface.Stub {

        @Override
        public Person addPerson(Person person) throws RemoteException {
            return person;
        }

        @Override
        public Person getPerson() throws RemoteException {
            return new Person("李四",50);
        }
    }

}
