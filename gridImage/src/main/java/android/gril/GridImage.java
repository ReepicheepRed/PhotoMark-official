package android.gril;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.*;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * User: tc
 * Date: 13-7-9
 * Time: 上午9:47
 * <p>多图选择的界面</p>
 */
public class GridImage extends FragmentActivity {
    private final static String RESULT_URIS = "result_uris";
    private final static String INTENT_CLAZZ = "clazz";

    private Class clazz;   //需要跳转的Activity类对象

    private ImageWorker imageWorker;

    private ArrayList<Uri> uriArray = new ArrayList<Uri>();
    private ArrayList<Long> origIdArray = new ArrayList<Long>();

    private TreeMap<Long, Uri> selectedTree = new TreeMap<Long, Uri>();

    private SelectedTreeMap selectedTreeMap = new SelectedTreeMap();


    private ImageAdapter adapter;
    private GridView gridView;
    
    private View loadView;//进度条View

    private Button doneBtn;

    private TextView selectedNum;

    private LoadLoacalPhotoCursorTask cursorTask;

    private AlphaAnimation inAlphaAni;
    private AlphaAnimation outAlphaAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.sdcard);
        createView();
        init();
    }

    /**
     * 创建视图
     */
    private void createView() {
        gridView = (GridView) findViewById(R.id.sdcard);
        loadView = findViewById(R.id.load_layout);
        doneBtn = (Button) findViewById(R.id.ok_btn);
        selectedNum = (TextView) findViewById(R.id.selected_num);
    }

    /**
     * 初始化
     */
    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clazz = (Class) bundle.get(INTENT_CLAZZ);
        }else {
			Log.i(GridImage.class.getSimpleName(), "bundle == null");
		}
        imageWorker = new ImageWorker(this);
        Bitmap b = Bitmap.createBitmap(new int[]{0x00000000}, 1, 1, Bitmap.Config.ARGB_8888);
        imageWorker.setLoadBitmap(b);
        adapter = new ImageAdapter(imageWorker, this);
        gridView.setAdapter(adapter);
        loadData();
        initAnimation();
        onItemClick();
        onScroll();
        doneClick();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        float fromAlpha = 0;
        float toAlpha = 1;
        int duration = 200;
        inAlphaAni = new AlphaAnimation(fromAlpha, toAlpha);
        inAlphaAni.setDuration(duration);
        inAlphaAni.setFillAfter(true);
        outAlphaAni = new AlphaAnimation(toAlpha, fromAlpha);
        outAlphaAni.setDuration(duration);
        outAlphaAni.setFillAfter(true);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        cursorTask = new LoadLoacalPhotoCursorTask(this);
        cursorTask.setOnLoadPhotoCursor(new LoadLoacalPhotoCursorTask.OnLoadPhotoCursor() {
            @Override
            public void onLoadPhotoSursorResult(ArrayList<Uri> uriArray, ArrayList<Long> origIdArray) {
                if (isNotNull(uriArray) & isNotNull(origIdArray)) {
                    GridImage.this.uriArray = uriArray;
                    GridImage.this.origIdArray = origIdArray;
                    loadView.setVisibility(View.GONE);
                    adapter.setOrigIdArray(origIdArray);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        cursorTask.execute();
    }

    /**
     * 选择图片
     */
    private void onItemClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox selectBtn = (CheckBox) view.findViewById(R.id.select_btn);
                boolean checked = !selectBtn.isChecked();
                selectBtn.setChecked(checked);
                adapter.putSelectMap(id, checked);
                Uri uri = uriArray.get(position);
                if (checked) {
                    selectedTree.put(id, uri);
                } else {
                    selectedTree.remove(id);
                }
                if (doneBtn.getVisibility() == View.GONE
                        && selectedTree.size() > 0) {
                    doneBtn.startAnimation(inAlphaAni);
                    doneBtn.setVisibility(View.VISIBLE);
                } else if (doneBtn.getVisibility() == View.VISIBLE
                        && selectedTree.size() == 0) {
                    doneBtn.startAnimation(outAlphaAni);
                    doneBtn.setVisibility(View.GONE);
                }
                CharSequence text = selectedTree.size() == 0 ? "" : "已选择 " + selectedTree.size() + " 张";
                selectedNum.setText(text);
            }
        });
    }

    /**
     * 滚动的时候不加载图片
     */
    private void onScroll() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    imageWorker.setPauseWork(false);
                } else {
                    imageWorker.setPauseWork(true);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     * 图片完成事件
     */
    private void doneClick() {
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clazz != null) {
                    selectedTreeMap.setTreeMap(selectedTree);
                    Intent intent = new Intent(GridImage.this, clazz);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RESULT_URIS, selectedTreeMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
					Log.i(GridImage.class.getSimpleName(), "clazz==null");
				}
            }
        });

    }

    /**
     * 判断list不为空
     * @param list
     * @return
     */
    private static boolean isNotNull(ArrayList list) {
        return list != null && list.size() > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursorTask.setExitTasksEarly(true);
        imageWorker.setExitTasksEarly(true);
    }
}
