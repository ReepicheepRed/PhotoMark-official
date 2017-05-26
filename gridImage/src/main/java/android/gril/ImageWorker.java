package android.gril;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.*;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * User: tc
 * Date: 13-7-9
 * Time: 上午10:03
 * 图片异步加载处理类
 */
public class ImageWorker {
    private static final int FADE_IN_TIME = 200;

    private boolean mExitTasksEarly = false;
    private boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();//锁对象

    protected final Resources mResources;
    private final ContentResolver mContentResolver;
    private final BitmapFactory.Options mOptions;

    private final HashMap<Long, SoftReference<BitmapDrawable>> bitmapCache = new HashMap<Long, SoftReference<BitmapDrawable>>();

    private Bitmap mLoadBitmap;

    public ImageWorker(Context context) {
        this.mResources = context.getResources();
        this.mContentResolver = context.getContentResolver();
        mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 3;
    }

    /**
     * 加载图片
     * @param origId
     * @param imageView
     */
    public void loadImage(long origId, ImageView imageView) {
        BitmapDrawable bitmapDrawable = null;
        if (bitmapCache.containsKey(origId)) {
            bitmapDrawable = bitmapCache.get(origId).get();
        }
        if (bitmapDrawable != null) {
            imageView.setImageDrawable(bitmapDrawable);
        } else if (cancelPotentialWork(origId, imageView)) {
            final LoadBitmapTask loadBitmapTask = new LoadBitmapTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mResources, mLoadBitmap, loadBitmapTask);
            imageView.setImageDrawable(asyncDrawable);
            //SERIAL_EXECUTOR 启动线程，保证线程顺序依次执行
            loadBitmapTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, origId);
        }
    }

    public void setLoadBitmap(Bitmap mLoadBitmap) {
        this.mLoadBitmap = mLoadBitmap;
    }

    /**
     * 设置图片动画  加载图片渐隐渐显的效果
     *
     * @param imageView
     * @param drawable
     */
    private void setImageDrawable(ImageView imageView, Drawable drawable) {
        // Transition drawable with a transparent drawable and the final drawable
        final TransitionDrawable td =
                new TransitionDrawable(new Drawable[]{
                        new ColorDrawable(android.R.color.transparent),
                        drawable
                });
        imageView.setImageDrawable(td);
        td.startTransition(FADE_IN_TIME);
    }

    /**
     * 取消可能在运行并且暂停的任务。
     *
     * @param origId
     * @param imageView
     * @return
     */
    private static boolean cancelPotentialWork(long origId, ImageView imageView) {
        final LoadBitmapTask loadBitmapTask = getBitmapWorkerTask(imageView);

        if (loadBitmapTask != null) {
            final long bitmapOrigId = loadBitmapTask.origId;
            if (bitmapOrigId == origId) {
                loadBitmapTask.cancel(true);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * 图片异步加载类
     */
    private class LoadBitmapTask extends AsyncTask<Long, Void, BitmapDrawable> {
        private long origId;
        private WeakReference<ImageView> imageViewReference;

        public LoadBitmapTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(Long... params) {
            origId = params[0];
            Bitmap bitmap = null;
            BitmapDrawable drawable = null;

            // Wait here if work is paused and the task is not cancelled
            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            if (bitmapCache != null && !isCancelled() && getAttachedImageView() != null
                    && !mExitTasksEarly) {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                        mContentResolver,
                        origId,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        mOptions
                );
            }

            if (bitmap != null) {
                drawable = new BitmapDrawable(mResources, bitmap);
                bitmapCache.put(origId,new SoftReference<BitmapDrawable>(drawable));
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            if (isCancelled() || mExitTasksEarly) {
                drawable = null;
            }

            final ImageView imageView = getAttachedImageView();
            if (drawable != null && imageView != null) {
                setImageDrawable(imageView, drawable);
            }
        }

       @Override
        protected void onCancelled(BitmapDrawable drawable) {
            super.onCancelled(drawable);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * 返回与此任务相关的 <code>ImageView</code>，
         * 如果 <code>ImageView</code> 内的任务是当前任务，
         * 则返回当前 <code>ImageView</code>,否则返回 <code>null</code>。
         * @return
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final LoadBitmapTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask) {
                return imageView;
            }
            return null;
        }
    }

    /**
     * 存储异步信息图片资源类
     */
    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<LoadBitmapTask> bitmapWorkerTaskReference;
        public AsyncDrawable(Resources res, Bitmap bitmap, LoadBitmapTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<LoadBitmapTask>(bitmapWorkerTask);
        }
        public LoadBitmapTask getLoadBitmapTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * 返回图片资源内存放的异步线程，如果存在，则返回，不存在，返回 <code>null</code>。
     *
     * @param imageView 当前存放异步资源图片的<code> ImageView</code>
     * @return
     */
    private static LoadBitmapTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getLoadBitmapTask();
            }
        }
        return null;
    }

    /**
     * 设置异步任务是否暂停，false为启动，true为暂停。
     * @param pauseWork
     */
    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    /**
     * 退出线程
     * @param exitTasksEarly
     */
    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
        setPauseWork(false);
    }
}