package com.sportsfire.injury;



import com.sportsfire.injury.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PlayerInjuryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_injury_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PlayerInjuryFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PlayerInjuryFragment.ARG_ITEM_ID));
            PlayerInjuryFragment fragment = new PlayerInjuryFragment();
            fragment.setArguments(arguments);
            //getSupportFragmentManager().beginTransaction()
              //      .add(R.id.player_injury_detail_container, fragment)
                //    .commit();
        }
               
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, PlayerListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
