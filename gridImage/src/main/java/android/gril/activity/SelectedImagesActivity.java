package android.gril.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.gril.R;
import android.gril.adapter.SelectedImagesAdapter;
import android.gril.bean.PhotoUpImageItem;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedImagesActivity extends Activity implements OnClickListener{

	private GridView gridView;
	private TextView back,ok;
	private ArrayList<PhotoUpImageItem> arrayList;
	private SelectedImagesAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selected_images_grid);
		init();
		setclickListener();
	}

	@SuppressWarnings("unchecked")
	private void init(){
		gridView = (GridView) findViewById(R.id.selected_images_gridv);
		back = (TextView) findViewById(R.id.back);
		ok = (TextView) findViewById(R.id.sure);
		arrayList = (ArrayList<PhotoUpImageItem>) getIntent().getSerializableExtra("selectIma");
		adapter = new SelectedImagesAdapter(SelectedImagesActivity.this,
				arrayList);
		gridView.setAdapter(adapter);
	}
	private void setclickListener(){
		back.setOnClickListener(this);
		ok.setOnClickListener(this);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back)
			finish();

		if(v.getId() == R.id.sure) {
			Toast.makeText(SelectedImagesActivity.this,
					"上传等操作", Toast.LENGTH_LONG).show();
		}
	}

}
