package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sportsfire.Player;
import com.sportsfire.R;
import com.sportsfire.ScreeningData;
import com.sportsfire.Season;
import com.sportsfire.Squad;
import com.sportsfire.SquadList;
import com.sportsfire.db.DBHelper;

public class TestSelectionActivity extends Activity {
	SquadList squads;
	Season season;
	public static final String ARG_ITEM_SEASON_NAME = "argumentSeasonName";
	public static final String ARG_ITEM_SEASON_ID = "argumentSeasonId";

	HashMap<CompoundButton, Spinner> testSelectionMap = new HashMap<CompoundButton, Spinner>();
	ScreeningData screenData;
	Squad selected;
	String week;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_testselection_page);
		season = new Season(getIntent().getStringExtra(ARG_ITEM_SEASON_NAME), getIntent()
				.getStringExtra(ARG_ITEM_SEASON_ID), new DBHelper(this));
		squads = new SquadList(this);
		Spinner spinner = (Spinner) findViewById(R.id.squadSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, squads.getSquadNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = squads.getSquadList().get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spinner = (Spinner) findViewById(R.id.weekSpinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, season.getWeeklist());
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				week = ((TextView) arg1).getText().toString();
				screenData = new ScreeningData(arg0.getContext(), season.getSeasonName(),
						week);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		testSelectionMap.put((CompoundButton) findViewById(R.id.WeightSwitch),
				(Spinner) findViewById(R.id.WeightSpinner));
		testSelectionMap.put((CompoundButton) findViewById(R.id.SqueezeSwitch),
				(Spinner) findViewById(R.id.SqueezeSpinner));
		testSelectionMap.put((CompoundButton) findViewById(R.id.CMJSwitch),
				(Spinner) findViewById(R.id.CMJSpinner));
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, com.sportsfire.MainPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onSwitchClicked(View view) {
		if (((CompoundButton) view).isChecked()) {
			(testSelectionMap.get(view)).setVisibility(View.VISIBLE);
		} else {
			(testSelectionMap.get(view)).setVisibility(View.INVISIBLE);
		}
	}

	public void sendData(View view) {
		HashMap<String, Integer> selectedTests = new HashMap<String, Integer>();
		for (CompoundButton k : testSelectionMap.keySet()) {
			if (k.isChecked()) {
				selectedTests.put(k.getText().toString(),
						(testSelectionMap.get(k)).getSelectedItemPosition());
			}
		}
		String[] params = new String[2];
		params[0] = season.getSeasonID();
		params[1] = week;
		Intent intent = new Intent(this, TestInputForm.class);
		intent.putExtra(TestInputForm.ARG_ITEM_PARAM, params);
		intent.putExtra(TestInputForm.ARG_ITEM_SQUAD, selected.getPlayerList());
		intent.putExtra(TestInputForm.ARG_ITEM_TESTS, selectedTests);
		
		
		List<List<String>> column = new ArrayList<List<String>>();
		// add column headings
		List<String> headerRow = new ArrayList<String>();
		headerRow.add("Full Name");
		for (Entry<String, Integer> test : selectedTests.entrySet()) {
			headerRow.add(test.getKey());
			if (test.getValue() == 0) {
				headerRow.add(test.getKey() + " Avg");
				headerRow.add(test.getKey() + " Pre");
			} else if (test.getValue() == 1) {
				headerRow.add(test.getKey() + " Avg");
			} else if (test.getValue() == 2) {
				headerRow.add(test.getKey() + " Pre");
			}
		}
		column.add(headerRow);
		for (Player player : selected.getPlayerList()) {
			List<String> row = new ArrayList<String>();
			row.add(player.getName());
			column.add(row);
		}
		FormValues values = new FormValues(column);
		intent.putExtra(TestInputForm.ARG_ITEM_DATA, values);
		
		
		startActivity(intent);
	}
}
