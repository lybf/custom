package org.lybf.customWidgets.TextView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.lybf.custom.views.OnScrollChangedListener;
import org.lybf.custom.views.TextView;
import zhuyuguang.com.verticalseekbar.view.VerticalSeekBar;

public class MainActivity extends Activity { 

	TextView tv;

	private EditText edit;

	private Button load;

	private VerticalSeekBar verticalSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = findViewById(R.id.activitymainTextView1);
		verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
		verticalSeekBar.setProgress(0);
        verticalSeekBar.setMaxProgress(1000);

        verticalSeekBar.setOrientation(1);
		tv.setOnScrollChangedListener(new OnScrollChangedListener(){
				@Override
				public void onScroll(TextView view, int x, int y, int line) {
					verticalSeekBar.setProgress(line);
				}	
			});
        verticalSeekBar.setOnSlideChangeListener(new VerticalSeekBar.SlideChangeListener(){

				@Override
				public void onStart(VerticalSeekBar slideView, int progress) {
				}

				@Override
				public void onProgress(VerticalSeekBar slideView, int progress) {
					tv.scrollToLine(progress);
				}

				@Override
				public void onStop(VerticalSeekBar slideView, int progress) {
				}


			});

		//lin.addView(tv);
		edit = (EditText)findViewById(R.id.activitymainEditText1);
		load = (Button)findViewById(R.id.activitymainButton1);
		load.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					try {
						Editable editable = edit.getText();
						if (editable.toString() != null) {
							File file = new File(editable.toString());
							if (file.exists()) {
								tv.setText("");
								FileReader read = new FileReader(file.getPath());
								BufferedReader br = new BufferedReader(read);
								String s;
								System.out.println("readFile");
								while ((s = br.readLine()) != null) {
									//System.out.println(s);
									tv.append(s);
								}
								verticalSeekBar.setMaxProgress(tv.getText().getLineCount());
								System.out.println("success\n\n");
							}
						}
					} catch (Exception e ) {
						e.printStackTrace();
					}
				}

			});
    }

} 
