package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.repository.postRepo.PostRepository
import ru.netology.mydiploma.repository.postRepo.PostRepositoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryImpl()
    val data: LiveData<List<Post>>
        get() = AppAuth.getInstance().authLiveData.switchMap { (myId, _) ->
            repository.data.map { posts ->
                posts.map {
                    it.copy(ownedByMe = it.authorId == myId)
                }
            }
        }

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