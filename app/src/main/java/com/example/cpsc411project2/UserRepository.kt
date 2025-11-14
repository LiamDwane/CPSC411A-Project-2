package com.example.cpsc411project2

import kotlinx.coroutines.flow.Flow

class UserRepository (private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users)
    }

    fun searchUsers(query: String): Flow<List<User>> {
        return userDao.searchUsers(query)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}