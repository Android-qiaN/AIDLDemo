package com.qk.chapter_2.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @描述:
 * @包名: com.example.myapplication
 * @类名: BookManagerService
 * @日期: 2017/6/13
 * @版权: Copyright ® 烽火星空. All right reserved.
 * @作者: QianKun
 */

/**
 * 服务端创建一个Service用来监听客户端的连接请求
 * 实现AIDL接口
 */

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);
//            } else {
//                Log.d(TAG, "already exists.");
//            }
//            Log.d(TAG, "registerListener,size:" + mListenerList.size());
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.d(TAG, "unregister listener succeed.");
//            } else {
//                Log.d(TAG, "not found,can not unregister.");
//            }
//            Log.d(TAG, "unregisterListener,current size:" + mListenerList.size());
            mListenerList.unregister(listener);
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "Ios"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
//        Log.d(TAG, "onNewBookArrived,notify listeners:" + mListenerList.size());
//        for (int i = 0; i < mListenerList.size(); i++) {
//            IOnNewBookArrivedListener listener = mListenerList.get(i);
//            Log.d(TAG, "onNewBookArrived,notify listener:" + listener);
//            listener.onNewBookArrived(book);
//        }
        final int N = mListenerList.beginBroadcast();
        for(int i=0;i<N;i++){
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if(l!=null){
                try {
                    l.onNewBookArrived(book);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }


}
