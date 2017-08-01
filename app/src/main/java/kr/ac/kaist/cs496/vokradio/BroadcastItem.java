package kr.ac.kaist.cs496.vokradio;

import java.util.Comparator;
import java.util.List;

/**
 * Created by q on 2017-07-31.
 */

public class BroadcastItem {


    String id;
    String title;
    String category;
    String day;
    String time;
    String thumbnail;
    List<String> producer;
    List<String> engineer;
    List<String> anouncer;
    List<String> songs;
    boolean onAir;

    public boolean getOnAir(){
        return this.onAir;
    }

    public String getTitle(){
        return this.title;
    }

    public String getId() {
        return this.id;
    }

    public String getCategory() {return this.category;}

    public String getDay() {return  this.day;}

    public String getTime() {return  this.time;}

    public String getThumbnail(){return this.thumbnail;}

    public List<String> getProducer(){return this.producer;}

    public List<String> getEngineer(){return this.engineer;}

    public List<String> getAnouncer(){return this.anouncer;}

    public List<String> getSongs(){return this.songs;}



    public void setOnAir(boolean value){
        this.onAir = value;
    }
    public void setTitle(String value){
        this.title = value;
    }
    public void setId(String value){
        this.id = value;
    }
    public void setCategory(String value){this.category = value;}
    public void setDay(String value){this.day = value;}
    public void setTime(String value){this.time = value;}
    public void setThumbnail(String value){this.thumbnail = value;}
    public void setProducer(List<String> value){this.producer = value;}
    public void setAnouncer(List<String> value){this.anouncer = value;}
    public void setEngineer(List<String> value){this.engineer = value;}
    public void setSongs(List<String> value){this.songs = value;}


    //Comparator
    public static final Comparator<BroadcastItem> ONAIRFIRST_COMPARATOR = new Comparator<BroadcastItem>() {
        @Override
        public int compare(BroadcastItem b1, BroadcastItem b2) {
            if(b1.getOnAir() == true && b2.getOnAir() == false){
                return -1;
            }
            return 1;
        }
    };


}
