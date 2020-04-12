package com.example.roombasic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 3, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    public static WordDatabase INSTACE;
    static final Migration migration_2_3=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("update word set is_chinese = 0");
            //database.execSQL("alter table word add column is_chinese integer not null default 1");
        }
    };

    public static synchronized WordDatabase getInstace(Context context) {
        if (INSTACE == null) {
            INSTACE = Room.databaseBuilder(context, WordDatabase.class, "wordData")
                     //.fallbackToDestructiveMigration()//破坏式迁移
                    .addMigrations(migration_2_3)
                    .build();
        }
        return INSTACE;
    }


    public abstract WordDao getWordDao();
}
