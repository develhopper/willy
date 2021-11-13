package ir.code4life.willy.database;

import androidx.room.TypeConverter;

import ir.code4life.willy.util.Size;

public class Converter {

    @TypeConverter
    public Integer fromSize(Size size){
        return size.ordinal();
    }

    @TypeConverter
    public Size toSize(Integer i){
        return Size.values()[i];
    }
}
