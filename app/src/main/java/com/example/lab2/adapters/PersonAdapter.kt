package com.example.lab2.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.Actions
import com.example.lab2.Constants
import com.example.lab2.PersonDetailActivity
import com.example.lab2.R
import com.example.lab2.databinding.PersonListItemBinding
import com.example.lab2.entity.Person
import com.squareup.picasso.Picasso

class PersonAdapter(private var context: Context,
                    private var persons: List<Person>,
                    private var type: Int) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    class PersonViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private val type = viewType
        val listItemBinding = PersonListItemBinding.bind(itemView)
        val gridItemBinding = PersonListItemBinding.bind(itemView)

        fun bind(res: Person, ctx: Context) {
            if (type == Constants.GRID) {
                gridItemBinding.name.text = res.name
                gridItemBinding.price.text = "${res.language}"
                Picasso.get().load(res.pathImage).into(gridItemBinding.personImage)
            } else {
                listItemBinding.name.text = res.name
                listItemBinding.price.text = "${res.language}"
                Picasso.get().load(res.pathImage).into(listItemBinding.personImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        val view: View = if (viewType == Constants.GRID) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.person_grid_item, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.person_list_item, parent, false)
        }
        return PersonViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(persons[position], context)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PersonDetailActivity::class.java)

            val binding = if (type == Constants.GRID) {
                holder.gridItemBinding
            } else {
                holder.listItemBinding
            }

            val options = ActivityOptions.makeSceneTransitionAnimation(
                context as Activity,
                Pair<View, String>(binding.personImage, "personImg")
            )

            intent.apply {
                putExtra("action", Constants.ACTIONS.getValue(Actions.ADD))
                putExtra("personName", persons[position].name)
                putExtra("personImg", persons[position].pathImage)
                putExtra("personCost", persons[position].costPerHour)
                putExtra("personSex", persons[position].sex)
                putExtra("personLangLevel", persons[position].languageLevel)
                putExtra("personLang", persons[position].language)
                putExtra("personAge", persons[position].age)
                putExtra("personCountry", persons[position].country)
                putExtra("personCity", persons[position].city)
                putExtra("personId", persons[position].id)
                putExtra("personVideo", persons[position].pathVideo)
            }
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (type == Constants.GRID) {
            Constants.GRID
        } else {
            Constants.LIST
        }
    }

    fun setType(newType: Int){
        type = newType
    }
}