package com.example.sos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.example.sos.data.DbSOS;

public class MainActivity extends ActionBarActivity {

	private Toolbar toolBar = null;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		initViews();
		initViewPager();
		checkLogin();
	}

	private void checkLogin() {
		DbSOS db = new DbSOS(this);
		
		if (db.detail.get() == null)
			pager.setCurrentItem(1, true);
		
		db.close();
	}

	private void initViews() {
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
	}

	private void initViewPager() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
		tabs.setIndicatorColor(Color.WHITE);
		tabs.setTextColor(Color.WHITE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sos_main, menu);
		return true;
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "HOME", "Details", "Contacts",
				"Message" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			default:
			case 0:
				return new FragHome();
			case 1:
				return new FragDetails();
			case 2:
				return new FragContacts();
			case 3:
				return new FragMessage();
			}

		}

	}

}