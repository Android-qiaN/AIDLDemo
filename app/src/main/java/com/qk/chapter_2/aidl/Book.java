package com.qk.chapter_2.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述:
 * @包名: com.qk.chapter_2.aidl
 * @类名: Book
 * @日期: 2017/6/13
 * @版权: Copyright ® 烽火星空. All right reserved.
 * @作者: QianKun
 */

public class Book implements Parcelable
{

	public int bookId;
	public String bookName;

	public Book(int bookId, String bookName)
	{
		this.bookId = bookId;
		this.bookName = bookName;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(bookId);
		parcel.writeString(bookName);
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>()
	{
		@Override
		public Book createFromParcel(Parcel parcel)
		{
			return new Book(parcel);
		}

		@Override
		public Book[] newArray(int i)
		{
			return new Book[i];
		}
	};

	private Book(Parcel parcel)
	{
		bookId = parcel.readInt();
		bookName = parcel.readString();
	}
}
