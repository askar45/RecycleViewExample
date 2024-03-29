package com.example.recycleviewexample

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycleviewexample.databinding.ItemUserBinding
import com.example.recycleviewexample.model.User

interface UsersActionListener {
    fun onUserMove(user: User, moveBy: Int)

    fun onDelete(user: User)

    fun onUserDetails(user: User)
}

class UsersAdapter(private val actionListener:UsersActionListener): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {
    var users: List<User> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val user = v.tag as User

        when (v.id) {
            R.id.moreImageViewButton ->{
                showPopUpMenu(v)

            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.id == user.id }
        popupMenu.menu.add(0, ID_MOVE_UP,Menu.NONE,context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN,Menu.NONE,context.getString(R.string.move_down)).apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE,Menu.NONE,context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener{
            when(it.itemId){
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onUserMove(user,1)
                }
                ID_REMOVE -> {
                    actionListener.onDelete(user)
                }


            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }


    class UsersViewHolder(
        val binding: ItemUserBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding){
            holder.itemView.tag = user
            moreImageViewButton.tag = user
            userNameTextView.text = user.name
            userCompanyTextView.text = user.company
            if(user.photo.isNotBlank()){
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            }else {
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }

    companion object{
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}