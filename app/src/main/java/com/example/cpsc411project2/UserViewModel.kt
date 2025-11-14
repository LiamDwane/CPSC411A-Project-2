// In file: UserViewModel.kt
package com.example.cpsc411project2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.distinctUntilChanged

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val users: Flow<List<User>> = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.allUsers
        } else {
            repository.searchUsers(query)
        }
    }.distinctUntilChanged() // Prevents unnecessary recompositions

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun refreshUsers() {
        viewModelScope.launch {
            try {
                val userList = RetrofitInstance.api.getUsers()
                repository.insertAll(userList)
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch from network. Displaying offline data."
            }
        }
    }

    companion object {
        fun Factory(repository: UserRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserViewModel(repository)
            }
        }
    }

    fun updateUserInDatabase(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
}
