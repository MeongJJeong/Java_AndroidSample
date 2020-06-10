package com.example.sql_sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    myDBHelper myHelper;
    EditText edtNum,edtName;
    TextView edtNumResult,edtNameResult;
    Button btnReset,btnInsert,btnView,btnChange,btnDelete;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNum = findViewById(R.id.edtNum);
        edtName = findViewById(R.id.edtName);
        edtNumResult = findViewById(R.id.edtNumberResult);
        edtNameResult = findViewById(R.id.edtNameResult);
        btnReset = findViewById(R.id.btnReset);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnChange = findViewById(R.id.btnChange);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        myHelper = new myDBHelper(this);
        if(v == btnReset){
            sqlDB = myHelper.getWritableDatabase();
            myHelper.onUpgrade(sqlDB,1,2);
            sqlDB.close();
        }
        if (v == btnInsert){
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO groupTBL VALUES ( '"
                    + edtName.getText().toString() +"' , "
                    + edtNum.getText().toString() + ");");
            sqlDB.close();
            Toast.makeText(getApplicationContext(),"입력됨", Toast.LENGTH_SHORT).show();
        }
        if (v == btnView){
            sqlDB = myHelper.getWritableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);

            String strNames = "그룹 이름" + "\r\n" + "------------" + "\r\n";
            String strNumbers = "인원" + "\r\n" + "------------" + "\r\n";

            while (cursor.moveToNext()){
                strNames += cursor.getString(0) + "\r\n";
                strNumbers += cursor.getString(1)+ "\r\n";
            }
            edtNameResult.setText(strNames);
            edtNumResult.setText(strNumbers);

            cursor.close();
            sqlDB.close();
        }
        if (v == btnChange){
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("UPDATE groupTBL SET gNumber = "
                    +edtNum.getText().toString()+
                    " WHERE gName = '"
                    +edtName.getText().toString()+"'");
            Toast.makeText(getApplicationContext(),"수정됨",Toast.LENGTH_SHORT).show();
            onClick(btnView);
            sqlDB.close();
        }
        if (v == btnDelete){
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '"
                            +edtName.getText().toString() +"'" );
            Toast.makeText(getApplicationContext(),"삭제됨",Toast.LENGTH_SHORT).show();
            onClick(btnView);
            sqlDB.close();
        }
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(@Nullable Context context) {
            super(context,"groupDB",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }
}
