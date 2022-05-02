package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.repository.postRepo.PostRepository
import ru.netology.mydiploma.repository.postRepo.PostRepositoryImpl

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data: MutableLiveData<List<Post>> get() =  repository.data
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
    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post.id)
    }

    fun dislikePostById(post: Post) = viewModelScope.launch {
        repository.dislikePostById(post.id)
    }

    fun removePostByID(post: Post) = viewModelScope.launch {
        repository.removePostById(post.id)
    }
}