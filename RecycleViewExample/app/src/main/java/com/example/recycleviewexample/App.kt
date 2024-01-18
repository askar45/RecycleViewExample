package com.example.recycleviewexample

import android.app.Application
import com.example.recycleviewexample.model.UsersService

class App: Application() {
    val usersService = UsersService()
}