package com.example.socialapp.daos
import com.example.socialapp.models.Post
import com.google.android.gms.tasks.Task
import com.example.socialapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = Firebase.firestore
     val postsCollections = db.collection("posts")
     val auth = Firebase.auth
    fun addPost( text : String){

        GlobalScope.launch(Dispatchers.IO){
            val currentUserId = auth.currentUser!!.uid
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post( text , user , currentTime)
            postsCollections.document().set(post)
        }
    }

    fun getPostById( postId : String) : Task<DocumentSnapshot>{
        return postsCollections.document(postId).get()
    }

    fun updateLikes( postId : String ){
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }

            postsCollections.document(postId).set(post)
        }
    }

}