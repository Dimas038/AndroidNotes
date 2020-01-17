package com.example.androidnotestesttask.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;

@DatabaseTable(tableName = "NOTES")
public class NoteModel implements Parcelable {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DEFINITION = "definition";
    public static final String UPDATED = "updated";

    @DatabaseField(columnName = ID, generatedId = true)
    private long id;
    @DatabaseField(columnName = TITLE)
    private String title;
    @DatabaseField(columnName = DEFINITION)
    private String definition;
    @DatabaseField(columnName = UPDATED)
    private long updated;

    public NoteModel() {
    }

    protected NoteModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        definition = in.readString();
        updated = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(definition);
        dest.writeLong(updated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDefinition() {
        return definition;
    }

    public long getUpdated() {
        return updated;
    }

    public String getDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(updated);
    }
}
