package com.sportsfire;
import java.util.ArrayList;
import com.sportsfire.db.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SquadList {
    private ArrayList<Squad> squadList = new ArrayList<Squad>();
    private ArrayList<String> squadNameList = new ArrayList<String>();
    private Context context;
    // loads the current SquadList from DB
    public SquadList(Context context){
    	this.context = context;
    	DBHelper dbHelp = new DBHelper(context);
    	SQLiteDatabase db = dbHelp.getReadableDatabase();
        String selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Squad sq = new Squad(cursor.getString(1),cursor.getString(0),db);
                squadList.add(sq);
                squadNameList.add(sq.getSquadName());
            } while (cursor.moveToNext());
        }
        dbHelp.close();
    }
    public void refresh(){
    	//refresh the squads
		DBHelper dbHelp = new DBHelper(context);
    	SQLiteDatabase db = dbHelp.getReadableDatabase();
    	for (Squad squad: squadList){
    		squad.refresh(db);
    	}
        dbHelp.close();
    }
    public ArrayList<Squad> getSquadList(){
        return squadList;
    }
    
    public ArrayList<String> getSquadNameList(){
        return squadNameList;
        
    }
}