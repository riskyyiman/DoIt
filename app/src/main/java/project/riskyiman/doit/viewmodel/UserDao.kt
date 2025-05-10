package project.riskyiman.doit.viewmodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import project.riskyiman.doit.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
