// AIDLInterface.aidl
package aidlservice.aidlservice;

// Declare any non-default types here with import statements
import aidlservice.aidlservice.Person;
interface AIDLInterface {
     Person addPerson(in Person person);
     Person getPerson();
}
