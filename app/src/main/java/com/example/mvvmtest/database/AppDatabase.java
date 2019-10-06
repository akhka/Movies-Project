package com.example.mvvmtest.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.mvvmtest.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
        private static AppDatabase instanse;

        public abstract MovieDao movieDao();

        public static synchronized AppDatabase getInstance(Context context){
                if (instanse == null){
                        instanse = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "movie_db")
                                .fallbackToDestructiveMigration()
                                .addCallback(roomCallback)
                                .build();
                }

                return instanse;
        }


        private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new PopulateDbTask(instanse).execute();
                }
        };


        private static class PopulateDbTask extends AsyncTask<Void, Void, Void>{

                private MovieDao movieDao;

                private PopulateDbTask(AppDatabase db){
                        movieDao = db.movieDao();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                        movieDao.insert(new Movie(1));
                        movieDao.insert(new Movie(2));
                        movieDao.insert(new Movie(3));
                        movieDao.insert(new Movie(4));
                        movieDao.insert(new Movie(5));
                        movieDao.insert(new Movie(6));
                        return null;
                }
        }
}
