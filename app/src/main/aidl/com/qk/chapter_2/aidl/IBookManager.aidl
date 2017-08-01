// IBookManager.aidl
package com.qk.chapter_2.aidl;
import com.qk.chapter_2.aidl.Book;
import com.qk.chapter_2.aidl.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
