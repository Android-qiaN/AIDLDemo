// IOnNewBookArrivedListener.aidl
package com.qk.chapter_2.aidl;
import com.qk.chapter_2.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
