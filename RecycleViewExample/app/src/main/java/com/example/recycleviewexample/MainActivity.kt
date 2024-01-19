package com.example.recycleviewexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleviewexample.databinding.ActivityMainBinding
import com.example.recycleviewexample.model.User
import com.example.recycleviewexample.model.UsersListener
import com.example.recycleviewexample.model.UsersService

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UsersActionListener {
            override fun onUserMove(user: User, moveBy: Int){
                usersService.moveUser(user, moveBy)
            }

            override fun onDelete(user: User){
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User){
                Toast.makeText(this@MainActivity, "${user.name}", Toast.LENGTH_SHORT).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}