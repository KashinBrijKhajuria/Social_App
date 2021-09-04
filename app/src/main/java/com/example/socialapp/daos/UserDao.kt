package com.example.socialapp.daos

import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {
    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")

    fun addUser( user : User?){
        user?.let{
            GlobalScope.launch( Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)

            }
        }

    }
    fun getUserById( uId : String): Task<DocumentSnapshot>{
        return usersCollection.document(uId).get()

    }

}