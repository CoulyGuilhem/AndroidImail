package DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * Il s'agit de l ensemble des requettes sql utilisé
 */
@Dao
public interface EmailDAO
{
    // Renvoi l'ensemble des emails sauvegardés
    @Query("SELECT * FROM Email")
    List<Email> getAllEmail();

    // Permet de sauvegarder un nouveau mail
    @Insert
    void insertEmail(Email... emails);

    // Permet de supprimer le mais selectionné
    @Delete
    void delete (Email email);
}
