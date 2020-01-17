package com.example.androidnotestesttask.db.helper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidnotestesttask.db.model.NoteModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<NoteModel, String> notesModelDao = null;

    @Inject
    public DatabaseHelper(Application application) {
        super(application, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NoteModel.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NoteModel.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<NoteModel, String> getNoteModelDao() throws SQLException {
        if (notesModelDao == null)
            notesModelDao = getDao(NoteModel.class);
        return notesModelDao;
    }

    public List<NoteModel> getAllNoteModel(boolean accending) {
        try {
            return getNoteModelDao().queryBuilder().orderBy(NoteModel.UPDATED, accending).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateNoteModel(NoteModel model) {
        try {
            getNoteModelDao().update(model);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createNoteModel(NoteModel model) {
        try {
            getNoteModelDao().create(model);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearAllNoteModel() {
        return dropTable(NoteModel.class);
    }

    @Override
    public void close() {
        super.close();
        notesModelDao = null;
    }

    public boolean dropTable(Class modelClass) {
        try {
            TableUtils.dropTable(connectionSource, modelClass, true);
            TableUtils.createTable(connectionSource, modelClass);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
