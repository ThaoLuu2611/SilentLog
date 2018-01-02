package com.example.silentlog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button debug;
	Button silent;
	String TAG = "SilentLog";
	final int high = 2;
	final int middle = 1;
	final int low = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		debug = (Button) findViewById(R.id.debug);
		silent = (Button) findViewById(R.id.silent);
		debug.setOnClickListener(debugClick);

		int level = 0;

		String debuglevel = null;
		debuglevel = readOnLine("/proc/dump_enable");
		Log.i(TAG, "debug level : " + debuglevel);
		level = Integer.parseInt(debuglevel);
	}

	View.OnClickListener debugClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			writeFile("/proc/dump_enable", String.valueOf(middle) + "\n");
			rebootDevice();
		}
	};

	private void rebootDevice() {
		Log.i(TAG, " - reboot Device");
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		pm.reboot(null);
	}

	private String readOnLine(String filepath) {
		String result = "0";
		BufferedReader buf = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filepath);
			buf = new BufferedReader(fr, 8096);

			if (buf != null) {
				result = buf.readLine();
			}
		} catch (FileNotFoundException ex) {
			Log.e(TAG, filepath + "FileNotFoundException");
		} catch (IOException e) {
			Log.e(TAG, filepath + "IOException");
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}

				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				Log.e(TAG, filepath + "IOException");
			}
		}

		if (result == null) {
			return "0";
		} else {
			return result.trim();
		}
	}

	private void writeFile(String filepath, String value) {
		FileWriter fw = null;

		try {
			fw = new FileWriter(filepath);
			fw.write(value);
		} catch (IOException e) {
			Log.e(TAG, filepath + "IOException");
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				Log.e(TAG, filepath + "IOException");
			}
		}
	}
}
