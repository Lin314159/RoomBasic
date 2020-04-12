package com.example.roombasic;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="english_word")
    private String word;
    @ColumnInfo(name="chinese_meaning")
    private String chineseMeaning;
    @ColumnInfo(name = "is_chinese")
    private boolean chinese;


    public Word(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }


    public boolean isChinese() {
        return chinese;
    }

    public void setChinese(boolean chinese) {
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }
}
