package com.example.roombasic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insertWords(Word...words);
    @Update
    void updaWords(Word...words);
    @Delete
    void delWords(Word...words);
    @Query("delete from word")
    void deleteAllWords();
    @Query("select * from word order by id desc")
    LiveData<List<Word>> getAllWords();

    @Query("select * from word where  english_word like :patten order by id desc")
    LiveData<List<Word>> findWords(String patten);
}
