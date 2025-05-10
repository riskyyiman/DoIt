package project.riskyiman.doit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.riskyiman.doit.R
import project.riskyiman.doit.model.User

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameText: TextView = itemView.findViewById(R.id.fullNameText)
        val usernameText: TextView = itemView.findViewById(R.id.usernameText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.fullNameText.text = user.fullName
        holder.usernameText.text = user.username
    }

    override fun getItemCount(): Int = users.size
}
