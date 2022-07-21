package com.ucu.salastahan.ui;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ucu.salastahan.R;
import com.ucu.salastahan.domain.DateTimeStrategy;
import com.ucu.salastahan.domain.LanguageController;
import com.ucu.salastahan.domain.inventory.Inventory;
import com.ucu.salastahan.domain.sale.Register;
import com.ucu.salastahan.domain.sale.SaleLedger;
import com.ucu.salastahan.techicalservices.AndroidDatabase;
import com.ucu.salastahan.techicalservices.Database;
import com.ucu.salastahan.techicalservices.DatabaseExecutor;
import com.ucu.salastahan.techicalservices.inventory.InventoryDao;
import com.ucu.salastahan.techicalservices.inventory.InventoryDaoAndroid;
import com.ucu.salastahan.techicalservices.sale.SaleDao;
import com.ucu.salastahan.techicalservices.sale.SaleDaoAndroid;

/**
 * This is the first activity page, core-app and database created here.
 * Dependency injection happens here.
 * 
 * @author Refresh Team
 * 
 */
public class SplashScreenActivity extends Activity {

	public static final String POS_VERSION = "Mobile POS 0.8";
	private static final long SPLASH_TIMEOUT = 2000;
	private Button goButton;
	private boolean gone;
	
	/**
	 * Loads database and DAO.
	 */
	private void initiateCoreApp() {
		Database database = new AndroidDatabase(this);
		InventoryDao inventoryDao = new InventoryDaoAndroid(database);
		SaleDao saleDao = new SaleDaoAndroid(database);
		DatabaseExecutor.setDatabase(database);
		LanguageController.setDatabase(database);

		Inventory.setInventoryDao(inventoryDao);
		Register.setSaleDao(saleDao);
		SaleLedger.setSaleDao(saleDao);
		
		DateTimeStrategy.setLocale("th", "TH");
		setLanguage(LanguageController.getInstance().getLanguage());

		Log.d("Core App", "INITIATE");
	}
	
	/**
	 * Set language.
	 * @param localeString
	 */
	private void setLanguage(String localeString) {
		Locale locale = new Locale(localeString);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initiateUI(savedInstanceState);
		initiateCoreApp();
	}
	
	/**
	 * Go.
	 */
	private void go() {
		gone = true;
		Intent newActivity = new Intent(SplashScreenActivity.this,
				MainActivity.class);
		startActivity(newActivity);
		SplashScreenActivity.this.finish();	
	}

	/**
	 * Initiate this UI.
	 * @param savedInstanceState
	 */
	private void initiateUI(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splashscreen);
		goButton = (Button) findViewById(R.id.goButton);
		goButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				go();
			}

		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!gone) go();
			}
		}, SPLASH_TIMEOUT);
	}

}