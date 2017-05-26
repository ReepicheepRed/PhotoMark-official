package android.gril.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.gril.R;
import android.gril.adapter.AlbumItemAdapter;
import android.gril.adapter.SelectedAdapter;
import android.gril.bean.PhotoUpImageBucket;
import android.gril.bean.PhotoUpImageItem;
import android.content.Intent;
import android.gril.bean.PhotoUpImageItem;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import org.w3c.dom.Text;

public class AlbumItemActivity extends Activity implements OnClickListener,SelectedAdapter.OnItemClickListener{

	private GridView gridView;
	private TextView back,ok;
	private PhotoUpImageBucket photoUpImageBucket;
	private List<PhotoUpImageItem> selectImages;
	private AlbumItemAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album_item_images);
		init();
		setListener();
	}
	private void init(){
		initSelected();
		gridView = (GridView) findViewById(R.id.album_item_gridv);
		back = (TextView) findViewById(R.id.back);
		ok = (TextView) findViewById(R.id.sure);
		selectImages = new ArrayList<PhotoUpImageItem>();
		
		Intent intent = getIntent();
		photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
		adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);
		gridView.setAdapter(adapter);
	}
	private void setListener(){
		back.setOnClickListener(this);
//		ok.setOnClickListener(this);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
				updateSelectedPicture(!checkBox.isChecked(),position);

			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back) {
			finish();
		}
		if(v.getId() == R.id.puzzle_begin_btn_girl){
			if(selectImages.size() <= 0){
				Toast.makeText(this,"Please select picture",Toast.LENGTH_SHORT).show();
				return;
			}
			Bundle bundle = new Bundle();
			if(selectImages.size() >= 9)
				selectImages = selectImages.subList(0,9);
			ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();
			arrayList.addAll(selectImages);
			bundle.putSerializable("selectIma", arrayList);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(1,intent);
			finish();
		}

		if (v.getId() ==  R.id.sure)
		{
			Intent intent = new Intent(AlbumItemActivity.this,SelectedImagesActivity.class);
			ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();
			arrayList.addAll(selectImages);
			intent.putExtra("selectIma", arrayList);
			startActivity(intent);
		}

	}

	private void updateSelectedPicture(boolean flag,int position){
		if(flag && selectImages.size() >= 9) {
			Toast.makeText(this,"The maximum is 9 pictures",Toast.LENGTH_SHORT).show();
			return;
		}
		photoUpImageBucket.getImageList().get(position).setSelected(flag);
		adapter.notifyDataSetChanged();
		if (photoUpImageBucket.getImageList().get(position).isSelected()) {
			if (selectImages.contains(photoUpImageBucket.getImageList().get(position))) {

			}else {
				selectImages.add(photoUpImageBucket.getImageList().get(position));
			}
		}else {
			if (selectImages.contains(photoUpImageBucket.getImageList().get(position))) {
				selectImages.remove(photoUpImageBucket.getImageList().get(position));
			}else {

			}
		}

		data = selectImages;
		if(data.size() > 9)
			data = data.subList(0,9);
		((SelectedAdapter)adapter_rv).setData(data);
		adapter_rv.notifyDataSetChanged();
		numberSelected.setText(getString(R.string.selected_picture,data.size()));
	}


	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter_rv;
	private RecyclerView.LayoutManager layoutManager;
	private List<PhotoUpImageItem> data;

	private TextView numberSelected;
	private Button beginBtn;
	private void initSelected(){
		recyclerView = (RecyclerView) findViewById(R.id.puzzle_selected_rv_girl);
		numberSelected = (TextView) findViewById(R.id.puzzle_selected_pic_tv_girl);
		numberSelected.setText(getString(R.string.selected_picture,0));
		beginBtn = (Button) findViewById(R.id.puzzle_begin_btn_girl);
		beginBtn.setOnClickListener(this);

		layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
		data = new ArrayList<PhotoUpImageItem>();
		adapter_rv = new SelectedAdapter(this,data);
		recyclerView.setAdapter(adapter_rv);
		recyclerView.setLayoutManager(layoutManager);
		((SelectedAdapter)adapter_rv).setOnItemClickListener(this);
		recyclerView.addItemDecoration(new SelectedAdapter.SpacesItemDecoration(this,8));
	}

	@Override
	public void onItemClick(View view, int position) {
		if(view.getId() == R.id.i_puzzle_selected_cancel_iv){
			updateSelectedPicture(false,position);
		}
	}
}
