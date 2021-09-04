package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.daos.PostDao
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postDao : PostDao
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)
        fab.setOnClickListener {
            val intent = Intent( this , CreatePostActivity::class.java)
            startActivity(intent)
        }
        SetUpRecyclerView()
    }

    private fun SetUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postsCollections
        val query = postsCollections.orderBy("createdAt" , Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()


        adapter = PostAdapter(recyclerViewOptions , this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun OnLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

}