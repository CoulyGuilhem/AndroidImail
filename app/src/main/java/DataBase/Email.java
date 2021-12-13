package DataBase;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Email")
/**
 * Table contenant les emails
 * id correspond à l'id du mail
 * Destinataire correspond aux adresses emails saisie
 * Object correspond au titre du mail
 * Content correspond au contenu du mail
 */
public class Email
{
    @PrimaryKey(autoGenerate = true)
    public  int id ;
    @ColumnInfo (name  = "Destinataire")
    public  String Destinataire ;
    @ColumnInfo (name = "Object")
    public  String Object;
    @ColumnInfo (name = "Content")
    public  String Content;



}
