package me.jessyan.mvparms.photomark.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.mvparms.photomark.R;
import me.jessyan.mvparms.photomark.databinding.PopPosterBinding;
import me.jessyan.mvparms.photomark.mvp.model.entity.Font;
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterFontAdapter;
import me.jessyan.mvparms.photomark.mvp.ui.adapter.PosterTextColorAdapter;


public class PopPoster extends PopupWindow implements OnClickListener,DefaultAdapter.OnRecyclerViewItemClickListener {
    private Context context;
    private BaseActivity activity;
    private BaseFragment fragment;
    private View mMenuView;
    private TextView curText;
    private RecyclerView.LayoutManager layoutManager;
    private DefaultAdapter adapter;
    private PopPosterBinding binding;
    private List<Font> fonts = new ArrayList<>();

    @SuppressLint("InflateParams")
    private PopPoster(Context context){
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater,R.layout.pop_poster,null,false);
        binding.setPopup(this);
        binding.ipteEt.setOnClickListener(this);


        onClick(binding.ipteColorTv);
        setAdapter(adapter);

        mMenuView = binding.getRoot();
        this.setContentView(mMenuView);
//        this.setClippingEnabled(false);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x4C000000);
        this.setBackgroundDrawable(dw);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mMenuView.setOnTouchListener((v, event) -> {
            int height = mMenuView.findViewById(R.id.pop_poster_ll).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });
    }


    public PopPoster(Context context, OnClickListener itemsOnClick) {
        this(context);

    }

    public PopPoster(BaseActivity activity) {
        this(activity.getBaseContext());
        this.activity = activity;

    }

    public PopPoster(BaseFragment fragment) {
        this(fragment.getContext());
        this.fragment = fragment;

    }

    public void setCurText(TextView curText) {
        this.curText = curText;
    }

    public void setFonts(List<Font> fonts) {
        this.fonts = fonts;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ipte_et:
                binding.ipteKeyboardIv.setActivated(false);
            case R.id.ipte_keyboard_iv:
                boolean isActivated = binding.ipteKeyboardIv.isActivated();
                binding.ipteKeyboardIv.setActivated(!isActivated);
                keyboard(!isActivated);
                break;
            case R.id.ipte_confirm_iv:
                curText.setText(binding.ipteEt.getText());
                dismiss();
                break;
            case R.id.ipte_color_tv:
                selected(binding.ipteColorTv);
                layoutManager = new GridLayoutManager(context, 6);
                adapter = new PosterTextColorAdapter(new ArrayList<>());
                setAdapter(adapter);
                break;
            case R.id.ipte_text_tv:
                selected(binding.ipteTextTv);
                layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                adapter = new PosterFontAdapter(fonts);
                setAdapter(adapter);
                break;
        }
    }



    @Override
    public void onItemClick(View view, Object data, int position) {
        if(data instanceof Integer){
            curText.setTextColor((Integer) data);
        }
    }

    private void initRecycleView() {
        configRecycleView(binding.ipteRv, layoutManager);
    }
    private void configRecycleView(RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager
    ) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public void setAdapter(DefaultAdapter adapter) {
        binding.ipteRv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        initRecycleView();
    }

    private TextView focusTv;
    private void selected(TextView textView){
        if(focusTv != null){
            focusTv.setActivated(false);
            focusTv.setTextColor(context.getResources().getColor(R.color.text_7c_c));
        }
        focusTv = textView;
        focusTv.setActivated(true);
        focusTv.setTextColor(Color.WHITE);
    }


    public void keyboard(boolean show) {
        attribute(!show);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(show){
            imm.showSoftInput(binding.ipteEt, 0);
            return;
        }
        imm.hideSoftInputFromWindow(binding.ipteEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void attribute(boolean show){
        if(show){
            binding.ipteRv.setVisibility(View.VISIBLE);
            binding.ipteAttribLl.setVisibility(View.VISIBLE);
            return;
        }
        binding.ipteRv.setVisibility(View.GONE);
        binding.ipteAttribLl.setVisibility(View.GONE);
    }
}
