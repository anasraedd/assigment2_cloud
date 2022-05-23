package com.example.mylibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylibrary.adapter.AdapterBook
import com.example.mylibrary.model.Book
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.obtions_more_to_book.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), AdapterBook.onClick, AdapterBook.onClickOnMore {
    var data = ArrayList<Book>()
    var db: FirebaseFirestore? = null
var isFavorite = false
    private val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
    var bookAdapter = AdapterBook(this,data, this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Firebase.firestore

        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.adapter = bookAdapter
//        tv_title.setText("المكتبة")
//
//        data.add(Book("00", "anas", "sport", 4.2, "anas raed", "2022", false))
//
//        data.add(Book("11", "anas11", "sport", 4.2, "anas raed11", "2022", true))
//        data.add(Book("22", "anas22", "sport", 4.2, "anas raed22", "2022", true))
//        data.add(Book("33", "anas33", "sport", 4.2, "anas raed33", "2022", false))
//        data.add(Book("44", "anas44", "sport", 4.2, "anas raed44", "2022",true))
//        data.add(Book("55", "anas555", "sport", 4.2, "anas raed55", "2022", false))

        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.adapter = bookAdapter

        getBooks()

        add_book.setOnClickListener {
var i = Intent(this, AddBookActivity::class.java)
            startActivity(i)
        }


    }


    fun getBooks(){

        db!!.collection("books").addSnapshotListener { value, error ->
            //Toast.makeText(this, "db:  ${value!!.documents.size}", Toast.LENGTH_LONG).show()
            //sizeDocument =  value!!.documents.size

            var data2 = ArrayList<Book>()

            for (document in value!!) {
                var id = document.id
               var name= document["name"].toString()
                var desciption= document["desciption"].toString()
               var categoryName= document["nameCategory"].toString()
                var rating= document["rate"]
               var  authorName= document["author"].toString()
               var yearRelease= document["yearRealese"]
                var image= document["image"].toString()

                if (document["isFavorite"] != null){
                     isFavorite= document["isFavorite"] as Boolean
                }

                val u = Book(id, name, desciption, categoryName,
                    rating, authorName, yearRelease,
                    isFavorite, image
                )


                data2.add(u)

            }

            bookAdapter.data = data2
            bookAdapter.notifyDataSetChanged()
        }

    }



    override fun onclickItem(position: Int, id: String, name: String, isFavorite: Boolean) {
//       db!!.collection("books").whereEqualTo("id", id).get().addOnSuccessListener { snapShot ->
//           snapShot.documents[0][id]
//
//       }.addOnFailureListener { e ->
//       }
        db!!.collection("books").document("$id").update("isFavorite", isFavorite).addOnSuccessListener {
            Log.d("TAG", "DocumentSnapshot successfully updated!")
        }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error updating document", e)
            }
    }

    override fun onclickItemOnClickMore(position: Int, id: String, name: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.obtions_more_to_book)

        bottomSheetDialog.tv_name_book.text = name
        bottomSheetDialog.cancble.setOnClickListener {
            bottomSheetDialog.cancel()
        }
        bottomSheetDialog.btn_edit.setOnClickListener {
            var i = Intent(this, EditBookActivity::class.java)
            i.putExtra("id", id)
             startActivity(i)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.btn_delete.setOnClickListener {
            db!!.collection("books").document("$id")
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error deleting document", e)
                }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }


}