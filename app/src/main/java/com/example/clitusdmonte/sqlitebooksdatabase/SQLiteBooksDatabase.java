package com.example.clitusdmonte.sqlitebooksdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;

public class SQLiteBooksDatabase extends AppCompatActivity {
    Context context;
    DBHandler db;
    ListAdapter adapter;
    ListView listView;
    Dialog dialog;
    Button addBookBtn, okBtn;
    EditText searchEditText, bookNameEdittext, authorNameEdittext, ratingEdittext;
    TextView bookNameTextView, authorNameTextView ;
    RatingBar ratingBarView;
    String bookName, authorName, ratings;
    ArrayList<BookModel> arrList;
    PopupWindow mpopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_books_database);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        arrList = db.getAllbooks();
        adapter = new ListAdapter(arrList,context);
        listView.setAdapter(adapter);
        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final Dialog dialog = new Dialog(context);
                View popUpView =
                        getLayoutInflater().inflate(R.layout.view_book_layout,
                                null);
                mpopup = new PopupWindow(popUpView,
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
                bookNameTextView = (TextView)
                        popUpView.findViewById(R.id.bookNameTextView);
                authorNameTextView = (TextView)
                        popUpView.findViewById(R.id.authorNameTextView);
                ratingBarView = (RatingBar)popUpView.findViewById(R.id.ratingBarView);
                okBtn = (Button)popUpView.findViewById(R.id.okBtn);
                bookNameTextView.setText(arrList.get(position).getBookName());
                authorNameTextView.setText(arrList.get(position).getAuthorName());
                ratingBarView.setRating((float)arrList.get(position).getRating());
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpopup.dismiss();
                    }
                });
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String enteredText = searchEditText.getText().toString();
                refreshList(enteredText);
            }
        });
    }
    private void init(){
        context = this;
        db = new DBHandler(context);
        arrList = new ArrayList<BookModel>();
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        listView = (ListView) findViewById(R.id.listView);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_book_layout);
        dialog.setCancelable(true);
        bookNameEdittext = (EditText)dialog.findViewById(R.id.bookNameEdittext);
        authorNameEdittext = (EditText) dialog.findViewById(R.id.authorNameEdittext);
        ratingEdittext = (EditText)dialog.findViewById(R.id.ratingEdittext);
        addBookBtn = (Button)dialog.findViewById(R.id.addBtn);
    }
    private void refreshList(String searchText) {
        arrList = db.getsearchedBooks(searchText);
        adapter = new ListAdapter(arrList, context);
        listView.setAdapter(adapter);
    }
    private void addBook() {
        bookName = bookNameEdittext.getText().toString();
        authorName = authorNameEdittext.getText().toString();
        ratings = ratingEdittext.getText().toString();
        if(TextUtils.isEmpty(bookName) || TextUtils.isEmpty(authorName) ||
                TextUtils.isEmpty(ratings)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please, fill in all the fields");
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        } else if ( !TextUtils.isDigitsOnly(ratings)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please, fill in the Ratings correctly");
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        } else {
            db.addBook(new BookModel(0,bookName, authorName,
                    Integer.parseInt(ratings)));
            arrList = db.getAllbooks();
            adapter = new ListAdapter(arrList, context);
            listView.setAdapter(adapter);
            dialog.cancel();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBookMenu : {
                dialog.show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class ListAdapter extends BaseAdapter {
        ArrayList<BookModel> arrList;
        Context context;
        public ListAdapter(ArrayList<BookModel> arrList, Context context) {
            this.arrList = arrList;
            this.context = context;
        }
        @Override
        public int getCount() {
            return arrList.size();
        }
        @Override
        public Object getItem(int position) {
            return arrList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return arrList.indexOf(getItem(position));
        }
        private class ViewHolder {
            TextView nameTextview;
            TextView authorTextview;
            RatingBar ratingBar;
        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup
                parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_layout, null);
                holder = new ViewHolder();
                holder.nameTextview =
                        (TextView)convertView.findViewById(R.id.nameTextview);
                holder.authorTextview =
                        (TextView)convertView.findViewById(R.id.authorTextview);
                holder.ratingBar =
                        (RatingBar)convertView.findViewById(R.id.ratingBar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BookModel book = (BookModel) getItem(position);
            holder.nameTextview.setText(book.getBookName());
            holder.authorTextview.setText(book.getAuthorName());
            holder.ratingBar.setRating((float) book.getRating());

            return convertView;
        }
    }
}