package kr.ac.kaist.cs496.vokradio;

/**
 * Created by q on 2017-07-31.
 */

public class BroadcastItem {

    boolean onAir;
    String title;
    String id;

    public BroadcastItem(String title, String id){
        this.onAir = true;
        this.title = title;
        this.id = id;
    }

    public boolean getOnAir(){
        return this.onAir;
    }

    public String getTitle(){
        return this.title;
    }

    public String getId() {
        return this.id;
    }

    public void setOnAir(boolean value){
        this.onAir = value;
    }
    public void setTitle(String value){
        this.title = value;
    }
    public void setId(String value){
        this.id = value;
    }


}
