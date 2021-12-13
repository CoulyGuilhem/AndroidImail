package DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EmailDAO
{
    @Query("SELECT * FROM Email")
    List<Email> getAllEmail();
    @Insert
    void insertEmail(Email... emails);
    @Delete
    void delete (Email email);
}
