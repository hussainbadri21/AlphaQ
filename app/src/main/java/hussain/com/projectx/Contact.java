package hussain.com.projectx;



/**
 * Created by KMZENON on 12-08-2017.
 */

public class Contact {
    String name;
    String number;
    public Contact(){

    }
    public Contact(String name,String number){
        this.name=name;
        this.number=number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
