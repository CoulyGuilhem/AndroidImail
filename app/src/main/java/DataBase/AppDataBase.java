package DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Email.class},version  = 2)
public abstract class AppDataBase extends RoomDatabase
{
        private static final String DATABASE_NAME = "Imail";
        private static  AppDataBase INSTANCE;

        public static AppDataBase getDbInstance(Context context)
        {
            if (INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class , DATABASE_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return  INSTANCE;
        }
        public abstract EmailDAO emailDAO();

}
