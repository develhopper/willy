package ir.code4life.willy.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;

public class BoardWithCount {

    @Embedded
    public Board board;

    @Relation(parentColumn = "id",entityColumn = "board_id")
    public List<Pin> pins;

    public Integer count;
}
