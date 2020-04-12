package com.example.roombasic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository repository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
    }

    LiveData<List<Word>> getAllwords() {
        return repository.getAllwords();
    }
    LiveData<List<Word>> findWordsWithPatten(String patten) {
        return repository.findWordsWithPatten(patten);
    }
    void insert(Word... words) {
        repository.insert(words);
    }

    void updata(Word... words) {
        repository.updata(words);
    }

    void delete(Word... words) {
        repository.delete(words);
    }

    void deleteAll() {
        repository.deleteAll();
    }


}
