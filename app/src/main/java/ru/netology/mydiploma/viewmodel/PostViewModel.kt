package ru.netology.mydiploma.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.repository.postRepo.PostRepository
import ru.netology.mydiploma.repository.postRepo.PostRepositoryImpl
import java.lang.Exception

val empty = Post(
    0,
    0,
    "",
    null,
    "",
    "",
    null,
    null
)

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

    private val _dataState: MutableLiveData<ModelState> = MutableLiveData()
    val dataState: LiveData<ModelState> get() = _dataState

    val edited = MutableLiveData(empty)

    init {
        viewModelScope.launch {
            try {
                _dataState.postValue(ModelState(loading = true))
                repository.getPosts()
                _dataState.postValue(ModelState())
            } catch (e: Exception) {
                _dataState.postValue(ModelState(error = true))
            }

        }
    }


    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(refreshing = true))
            repository.getPosts()
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }


    fun likePostById(post: Post) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.likePostById(post.id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }

    }

    fun dislikePostById(post: Post) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.dislikePostById(post.id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun removePostById(post: Post) = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            repository.removePostById(post.id)
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        if (content.trim() == edited.value?.content) {
            return
        }
        edited.value = edited.value?.copy(content = content)
    }

    fun save() = viewModelScope.launch {
        try {
            _dataState.postValue(ModelState(loading = true))
            edited.value?.let {
                repository.save(it)
            }
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
        edited.postValue(empty)
    }
}