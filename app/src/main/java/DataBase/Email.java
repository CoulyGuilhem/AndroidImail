package DataBase;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Email")
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
