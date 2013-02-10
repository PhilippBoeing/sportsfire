package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.List;

import com.sportsfire.Player;

import android.os.Parcel;
import android.os.Parcelable;

public class FormValues implements Parcelable{
	private List<List<String>> values;
	public FormValues(List<List<String>> values){
		this.values = values;
	}
	
	public FormValues(Parcel in) {
		this.values = new ArrayList<List<String>>();
		readFromParcel(in);
	}

	public List<String> getHeader(){
		return values.get(0);
	}
	public List<List<String>> getValues(){
		return values;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(values.size());
		for(List<String> l:values){
			dest.writeInt(l.size());
			for(String s:l){
				dest.writeString(s);
			}
		}
	}
	
	private void readFromParcel(Parcel in){
		int listlistsize = in.readInt();
		for(int i = 0;i<listlistsize;i++){
			int listsize = in.readInt();
			List<String> newentry = new ArrayList<String>();
			for(int j = 0;j<listsize;j++){
				newentry.add(in.readString());
			}
			values.add(newentry);
		}
	}
	
   public static final Parcelable.Creator CREATOR =
   	new Parcelable.Creator() {
           public FormValues createFromParcel(Parcel in) {
               return new FormValues(in);
           }

           public FormValues[] newArray(int size) {
               return new FormValues[size];
           }
       };

}
