package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.repository.PostRepository
import ru.netology.mydiploma.repository.PostRepositoryImpl
import java.time.Instant

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = repository.data
    val data: LiveData<List<Post>>  get() = _data

    init {
        viewModelScope.launch {
            repository.getPosts()
        }
    }

    private fun getPosts() = viewModelScope.launch {
        repository.getPosts()
    }

    fun createPost(post: Post) = viewModelScope.launch {
        repository.save(post)
        updateData()
    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post.id)
        updateData()
    }

    fun dislikePostById(post: Post) = viewModelScope.launch {
        repository.dislikePostById(post.id)
        updateData()
    }

    fun removePostByID(post: Post) = viewModelScope.launch {
        repository.removePostById(post.id)
    }

    private fun updateData() {
        _data.value = repository.data.value
    }
}