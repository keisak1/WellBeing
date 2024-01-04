package edu.ufp.pam.wellbeing.data.model

import androidx.room.*

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Delete
    fun update(user: User)

    @Query("SELECT * FROM user WHERE displayName = :displayName")
    fun getUserByDisplayName(displayName: String): User?

    @Query("SELECT * FROM user WHERE displayName = :displayName AND password = :password")
    fun getUserByCredentials(displayName: String, password: String): User?

    /**@Query("select * from note_table order by priority desc")
   fun getAllNotes(): LiveData<List<Note>>**/


}