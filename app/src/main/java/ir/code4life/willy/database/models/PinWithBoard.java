package ir.code4life.willy.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;

public class PinWithBoard {

    @Embedded
    public Pin pin;

    @Relation(parentColumn = "board_id",entityColumn = "id")
    public Board board;
}
