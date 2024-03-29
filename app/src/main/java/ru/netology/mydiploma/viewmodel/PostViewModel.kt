package ru.netology.mydiploma.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.MediaUpload
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.model.ModelState
import ru.netology.mydiploma.model.PhotoModel
import ru.netology.mydiploma.repository.postRepo.PostRepository
import java.io.File
import java.lang.Exception
import java.time.Instant
import javax.inject.Inject

private val empty = Post(
    0,
    0,
    "",
    null,
    "",
    "",
    null,
    null
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val auth: AppAuth,
    postRepository: PostRepository
) : ViewModel() {
    private val repository: PostRepository = postRepository
    val data: LiveData<List<Post>>
        get() = auth.authLiveData.switchMap { (myId, _) ->
            repository.data.map { posts ->
                posts.map { post ->
                    post.copy(ownedByMe = post.authorId == myId)
                }
            }
        }

    private val _dataState: MutableLiveData<ModelState> = MutableLiveData()
    val dataState: LiveData<ModelState> get() = _dataState

    private val noPhoto = PhotoModel()

    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel> get() = _photo

    private val edited = MutableLiveData(empty)

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

            edited.value?.let { post ->
                when(_photo.value) {
                    noPhoto -> repository.save(post.copy(published = Instant.now().toString()))
                    else -> _photo.value?.file?.let { file ->
                        repository.saveWithAttachment(
                            post.copy(published = Instant.now().toString())
                            , MediaUpload(file)
                        )
                    }
                }
            }
            _dataState.postValue(ModelState())
        } catch (e: Exception) {
            _dataState.postValue(ModelState(error = true))
        }
        edited.postValue(empty)
        _photo.postValue(noPhoto)
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}