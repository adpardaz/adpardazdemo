package com.adpardaz.adpardaztest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adbaan.AdBaanCallBack;
import com.adbaan.AdBaanDownloadCallBack;
import com.adbaan.AdView;
import com.adbaan.AdViewActivity;
import com.adbaan.ApplicationHandler;
import com.adbaan.VideoAdViewActivity;

public class MainActivity extends Activity
{
	public static final String ADBAAN_TEST_TAG = "AdBaanTest";
	public static final String token = "OJDIROBW6Y";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AdView.setDoLogging(true);
		setContentView(R.layout.activity_main);

		// Video Ad
		final ProgressBar per = (ProgressBar) findViewById(R.id.percentage);
		per.getProgressDrawable().setColorFilter(Color.parseColor("#00ae8b"), Mode.SRC_IN);

		final Button videoButton = (Button) findViewById(R.id.video_ad);
		videoButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				VideoAdViewActivity.isAvailable(MainActivity.this, token, new AdBaanCallBack()
				{
					@Override
					public void onSuccess()
					{
						Log.e(ADBAAN_TEST_TAG, "There is some ad available");

						AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
						dialog.setTitle("تبلیغات ادپرداز");
						dialog.setMessage("آیا مایل به دیدن تبلیغات هستید؟");
						dialog.setIcon(android.R.drawable.ic_dialog_alert);
						
						dialog.setPositiveButton("بله", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								VideoAdViewActivity.download(MainActivity.this, new AdBaanDownloadCallBack()
								{
									@Override
									public void onDownloadStart()
									{
										Toast.makeText(MainActivity.this, "Start Downloading", Toast.LENGTH_SHORT).show();
										Log.e(ADBAAN_TEST_TAG, "Download started");
									}

									@Override
									public void onDownloadProgress(int percentage)
									{
										Log.e(ADBAAN_TEST_TAG, "Download progress: " + percentage);
										per.setProgress(percentage);
									}

									@Override
									public void onDownloadFailed(String result)
									{
										Log.e(ADBAAN_TEST_TAG, "Download failed");
									}

									@Override
									public void onDownloadComplete()
									{
										Log.e(ADBAAN_TEST_TAG, "Download finished");

										VideoAdViewActivity.show(MainActivity.this, new AdBaanCallBack()
										{
											@Override
											public void onSuccess()
											{
												Log.e(ADBAAN_TEST_TAG, "User watched the video");
											}

											@Override
											public void onFailure(String result)
											{
												Log.e(ADBAAN_TEST_TAG, "Something went wrong while playing video");
											}
										});
									}
								});
							}
						});

						dialog.setNegativeButton("خیر", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						});

						dialog.show();
					}

					@Override
					public void onFailure(String result)
					{
						Log.e(ADBAAN_TEST_TAG, "There is no ad available");
					}
				});

			}
		});

		Button fullScreen = (Button) findViewById(R.id.fullscreen_ad);
		fullScreen.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AdViewActivity.run(MainActivity.this, token, new AdBaanCallBack()
				{
					@Override
					public void onSuccess()
					{
					}

					@Override
					public void onFailure(String result)
					{
					}
				});
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		ApplicationHandler.checkInstallation(this, new AdBaanCallBack()
		{
			@Override
			public void onSuccess()
			{
				Toast.makeText(MainActivity.this, "installed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(String result)
			{
				Toast.makeText(MainActivity.this, "basterd", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
