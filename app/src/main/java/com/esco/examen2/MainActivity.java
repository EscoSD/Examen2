package com.esco.examen2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class MainActivity extends AppCompatActivity {

	@SuppressLint("Range")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActivityCompat.requestPermissions(this, new String[] {"android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE"}, 0);

		String[] cProjection =
				{
						ContactsContract.Contacts._ID,
						ContactsContract.Contacts.DISPLAY_NAME
				};

		String[] mProjection =
				{
						MediaStore.Images.Media.DATA
				};
		String selectionClause = null;

		String[] selectionArgs = null;

		String sortOrder = null;

		Cursor contactCursor = null;
		Cursor imageCursor = null;

		try {
			contactCursor = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					cProjection,
					selectionClause,
					selectionArgs,
					sortOrder);


			imageCursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					mProjection,
					selectionClause,
					selectionArgs,
					sortOrder);

		} catch (Exception e) {

			e.printStackTrace();
		}

		int cursorPosition = 0;


		if (contactCursor.moveToFirst() && imageCursor.moveToFirst()){

			do {

				String contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
				String contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String cText = contactId + ".- " + contactName;

				String imageData = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
				Bitmap bitmap = BitmapFactory.decodeFile(imageData);

				LinearLayoutCompat layOut =  findViewById(R.id.main_layout);
				LinearLayoutCompat row = new LinearLayoutCompat(this);
				row.setOrientation(LinearLayoutCompat.VERTICAL);
				row.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

				TextView text=new TextView(this);
				text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				text.setText(cText);
				text.setTextSize(30);

				ImageView image = new ImageView(this);
				image.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

				image.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
				image.setScaleType(ImageView.ScaleType.FIT_CENTER);

				row.addView(text);
				row.addView(image);

				layOut.addView(row);

				cursorPosition++;
			} while ((contactCursor.moveToNext() && imageCursor.moveToNext()) && cursorPosition < 3);
		}
	}
}