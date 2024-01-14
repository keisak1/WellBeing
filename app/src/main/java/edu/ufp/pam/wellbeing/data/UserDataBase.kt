package edu.ufp.pam.wellbeing.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.ufp.pam.wellbeing.data.model.User
import edu.ufp.pam.wellbeing.data.model.UserDAO


@Database(entities = [User::class], version = 2)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object {
        private var instance: UserDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): UserDataBase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    UserDataBase::class.java,
                    "wellbeing"
                ).fallbackToDestructiveMigration().addCallback(roomCallback).build()

            return instance!!
        }

        private val roomCallback = object : Callback(){
            override fun onCreate(db: SupportSQLiteDatabase){
                Log.d("DATABASE","Database created")
                super.onCreate(db)
            }
        }
    }

    fun insert(user: User) {
        Log.d("DATABASE","User inserted")

        userDao().insert(user)
    }

    fun getUser(user: User): User? {
       return userDao().getUserByDisplayName(user.displayName)
    }

    fun getUserByCredentials(user: User) : User?{
        return userDao().getUserByCredentials(user.displayName, user.password)
    }

}