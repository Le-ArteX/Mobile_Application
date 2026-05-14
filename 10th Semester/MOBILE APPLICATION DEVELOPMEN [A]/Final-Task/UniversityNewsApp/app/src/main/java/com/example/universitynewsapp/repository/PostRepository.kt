package com.example.universitynewsapp.repository

import com.example.universitynewsapp.network.RetrofitClient

class PostRepository {

    private val api = RetrofitClient.instance

    suspend fun getPosts() = api.getAllPosts()

    suspend fun getPost(id: Int) =
        api.getPostById(id)

    suspend fun getComments(postId: Int) =
        api.getCommentsByPost(postId)

    suspend fun getUsers() =
        api.getAllUsers()

    suspend fun getUser(id: Int) =
        api.getUserById(id)

    suspend fun getPostsByUser(id: Int) =
        api.getPostsByUser(id)
}