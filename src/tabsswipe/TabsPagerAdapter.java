package tabsswipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kinkypizza.donut.eventsFragment;
import com.kinkypizza.donut.friendsFragment;
import com.kinkypizza.donut.meFragment;
import com.kinkypizza.donut.settingsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter{
	
	 
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int index){
		switch(index){
		case 0: 
			//top rated fragment activity
			return new eventsFragment();
		case 1: 
			//top rated fragment activity
			return new friendsFragment();
		case 2: 
			//top rated fragment activity
			return new meFragment();
		case 3: 
			//top rated fragment activity
			return new settingsFragment();
		}
		
		return null;
	}
	
	@Override
	public int getCount(){
		//get item count - equal to number of tabs
		return 4;
	}
	
	

}

