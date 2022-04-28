package com.example.lab2



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.adapters.PersonAdapter
import com.example.lab2.databinding.ActivityMainBinding
import com.example.lab2.entity.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private var type = Constants.LIST
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var personsDBRef: DatabaseReference

    override fun onStart() {
        super.onStart()

        if (!isShowAllPersons) {
            binding.resetFiltersButton.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContext(this)

        auth = FirebaseAuth.getInstance()
        personsDBRef = FirebaseDatabase.getInstance().reference.child("persons")

        personsDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isShowAllPersons) {
                    personsList.clear()
                    for (s in snapshot.children) {
                        val user = s.getValue(Person::class.java)
                        if (user != null) {
                            personsList.add(user)
                        }
                    }
                    fullCoursesList.clear()
                    fullCoursesList.addAll(personsList)
                    personAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
        init()
    }



    private fun init() {
        setPersonRecycler()
        binding.apply {

            filtersButton.setOnClickListener {
                val intent = Intent(this@MainActivity, FiltersActivity::class.java)
                startActivity(intent)
            }

            resetFiltersButton.setOnClickListener {
                isShowAllPersons = true
                resetFiltersButton.visibility = View.GONE
                personsList.clear()
                personsList.addAll(fullCoursesList)
                personAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun setPersonRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        type = Constants.LIST
        personAdapter.setType(Constants.LIST)
        binding.personRecycler.layoutManager = layoutManager
        binding.personRecycler.adapter = personAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.men, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOut -> {
                auth.signOut()
                val devIntent = Intent(this@MainActivity, AuthorizationActivity::class.java)
                startActivity(devIntent)
                finish()
            }
            R.id.list -> {
                if (type == Constants.GRID) {
                    binding.personRecycler.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    type = Constants.LIST
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_grid)
                } else {
                    binding.personRecycler.layoutManager =
                        GridLayoutManager(applicationContext, 2)
                    type = Constants.GRID
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_list)
                }
                personAdapter.setType(type)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private lateinit var context: Context
        var personsList = mutableListOf<Person>()

        lateinit var personAdapter: PersonAdapter
        var fullCoursesList = mutableListOf<Person>()
        var isShowAllPersons = true
        var type = Constants.LIST

        fun showPersonsByFilters(
            languages: List<String>,
            ageTo: Int,
            ageFrom: Int,
            priceFrom: Double,
            priceTo: Double
        ) {
            isShowAllPersons = false
            personsList.clear()
            var filterCourses = mutableListOf<Person>()

            for (c in fullCoursesList) {
                if(languages.isNotEmpty()){
                        if ( c.language in languages && c.costPerHour in priceFrom..priceTo && c.age in ageFrom..ageTo) {
                            filterCourses.add(c)

                    }
                }
                else {
                    if (c.costPerHour in priceFrom..priceTo && c.age in ageFrom..ageTo) {
                        filterCourses.add(c)

                    }
                }

            }
            personsList.clear()
            personsList.addAll(filterCourses)
            personAdapter.notifyDataSetChanged()
        }

        fun setContext(ctx: Context) {
            context = ctx
            personAdapter = PersonAdapter(context, personsList, type)
        }
    }
}