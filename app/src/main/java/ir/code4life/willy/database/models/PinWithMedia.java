package ir.code4life.willy.database.models;

import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import ir.code4life.willy.http.models.Pin;

public class PinWithMedia {

    @Embedded
    public Pin pin;

    @Relation(parentColumn = "pinId",entityColumn = "pin_id")
    public Media media;
}
