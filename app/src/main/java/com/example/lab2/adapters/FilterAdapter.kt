package com.example.lab2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import com.example.lab2.databinding.FilterItemBinding

class FilterAdapter (private var context: Context, private var languages : List<String>) :
    RecyclerView.Adapter<FilterAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FilterItemBinding.bind(itemView)
        private var isSelected = false

        fun bind(category : String) = with( binding){
            languageTitle.text = category
            itemView.setOnClickListener { view ->
                if(isSelected) {
                    binding.imageView3.setImageResource(R.drawable.ic_filter_item)
                    isSelected = !isSelected
                    languageList.remove(languageTitle.text.toString())
                } else {
                    binding.imageView3.setImageResource(R.drawable.ic_filter_item_selected)
                    isSelected = !isSelected
                    languageList.add(languageTitle.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val categoryItemView : View = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false)
        return PersonViewHolder(categoryItemView)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int {
        return languages.count()
    }

    companion object {
        var languageList = mutableListOf<String>()
    }
}