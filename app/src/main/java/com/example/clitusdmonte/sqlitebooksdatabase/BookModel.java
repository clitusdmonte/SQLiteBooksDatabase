package com.example.clitusdmonte.sqlitebooksdatabase;

/**
 * Created by clitus dmonte on 3/30/2018.
 */

public class BookModel {
    int mId;
    String mBookName;
    String mAuthorName;
    int mRating;
    public BookModel(int mId, String mBookName, String mAuthorName, int mRating) {
        this.mId = mId;
        this.mBookName = mBookName;
        this.mAuthorName = mAuthorName;
        this.mRating = mRating;
    }
    public int getId() {
        return mId;
    }
    public String getBookName() {
        return mBookName;
    }
    public String getAuthorName() {
        return mAuthorName;
    }
    public int getRating() {
        return mRating;
    }
}