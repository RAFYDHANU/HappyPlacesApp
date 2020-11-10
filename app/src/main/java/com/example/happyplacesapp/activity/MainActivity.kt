package com.example.happyplacesapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.R
import com.example.happyplacesapp.adapter.HappyPlacesAdapter
import com.example.happyplacesapp.database.DatabaseHandler
import com.example.happyplacesapp.model.HappyPlaceModel
import com.example.happyplacesapp.util.SwipeToDeleteCallback
import com.example.happyplacesapp.util.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {

        var ADD_HAPPY_ACTIVITY_REQUEST_CODE = 1
        var HAPPY_PLACE_DETAILS = "extra_place_details"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener {
            val intent = Intent(this, AddHappyPlacesActivity::class.java)
            startActivityForResult(intent, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }

        getHappyPlaceFromLocalDB()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HAPPY_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getHappyPlaceFromLocalDB()
            }else{
                Log.e("Activity", "called or Back Pressed")
            }
        }
    }

    private fun getHappyPlaceFromLocalDB() {
        //variabel supaya database nya bisa kita gunakan di MainActivity
        val dbHandler = DatabaseHandler(this)
        //digunakan untuk menjalankan aksi get yang berasal dari databasehandler
        val getHappyPlacesList: ArrayList<HappyPlaceModel> =  dbHandler.getHappyPlaceList()

        //sebuah kondisi ketika data itu ada
        if (getHappyPlacesList.size > 0){
            rv_places.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE
            setupHappyPlaceRV(getHappyPlacesList)
            //kondisi kedua ketika data itu kosong
        }else {
            rv_places.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
        }
    }
    //function ini di gunakan untuk create recyclerview di dalam mainactivity
    private fun setupHappyPlaceRV(happyPlacesList: ArrayList<HappyPlaceModel>) {
        //untuk mendeteksi data ketika ada perubahan seperti ada data baru yang masuk ke dalam recyclerview
        rv_places.layoutManager = LinearLayoutManager(this)
        //buat trigger ketika ada data baru
        rv_places.setHasFixedSize(true)

        // untuk menjalankan adapter kita di dalam mainactivity sehingga recyclerview bisa berjalan dengan seharusnya
        val adapter = HappyPlacesAdapter(this,happyPlacesList)
        rv_places.adapter = adapter

        adapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity,HappyDetailActivity::class.java)
                intent.putExtra(HAPPY_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places.adapter as HappyPlacesAdapter
                adapter.notifyEdititem(this@MainActivity, viewHolder.adapterPosition, ADD_HAPPY_ACTIVITY_REQUEST_CODE)

            }
        }

        val editItemTouch = ItemTouchHelper(editSwipeHandler)
        editItemTouch.attachToRecyclerView(rv_places)

        val deleteSwipe = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places.adapter as HappyPlacesAdapter
                adapter.removeat(viewHolder.adapterPosition)

                getHappyPlaceFromLocalDB()
            }

        }

        val deleteItem = ItemTouchHelper(deleteSwipe)
        deleteItem.attachToRecyclerView(rv_places)

    }
}