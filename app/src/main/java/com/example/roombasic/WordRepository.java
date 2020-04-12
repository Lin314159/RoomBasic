package com.example.roombasic;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allwords;

    WordRepository(Context context) {
        WordDatabase database = WordDatabase.getInstace(context.getApplicationContext());
        wordDao = database.getWordDao();
        allwords = wordDao.getAllWords();
    }

    LiveData<List<Word>> getAllwords() {
        return allwords;
    }
    LiveData<List<Word>> findWordsWithPatten(String patten) {
        return wordDao.findWords("%"+patten+"%");
    }

    void insert(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updata(Word... words) {
        new UpdataAsyncTask(wordDao).execute(words);
    }

    void delete(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    void deleteAll() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class UpdataAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        UpdataAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updaWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.delWords(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }



}
