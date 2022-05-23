package com.example.mylibrary.adapter

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylibrary.EditBookActivity
import com.example.mylibrary.R
import com.example.mylibrary.model.Book
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_book.view.*
import kotlinx.android.synthetic.main.obtions_more_to_book.*
import java.sql.Date
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.Duration.Companion.seconds

class AdapterBook(var activity: Activity, var data: ArrayList<Book>, var click: onClick, var clickOnMore:onClickOnMore): RecyclerView.Adapter<AdapterBook.MyViewHolder>() {


    interface onClick{
        fun onclickItem(position: Int, id: String, name:String,  isFavorite:Boolean)
    }

    interface onClickOnMore{
        fun onclickItemOnClickMore(position: Int, id: String, name:String)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
var imageBook = itemView.image_book_item
        var nameBook = itemView.name_book
        var nameAuthor = itemView.name_author
//        var yearRelease = itemView.year_release
        var rating = itemView.rating2
        var category = itemView.category
        var btnFavorite = itemView.btn_favorite
//        var btnDownload = itemView.btn_download
        var btnMore = itemView.btn_more

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(activity).inflate(R.layout.item_book, parent, false)
        return MyViewHolder(root)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(activity).load(data[position].image).into(holder.imageBook)
        holder.nameBook.text = data[position].name
        holder.nameAuthor.text = data[position].authorName
       // holder.yearRelease.text = data[position].yearRelease.toString()
       // Toast.makeText(activity, "${data[position].yearRelease}", Toast.LENGTH_LONG).show()
//        var date= data[position].yearRelease as Date
//        var date2 =date.time.seconds
//        date2.toString()
        var date= data[position].yearRelease.toString()
        var date2 = ""
        var indexstart = 0

   for (i in 0 until date.length){
       if (date[i].toString() == "="){
           //Toast.makeText(activity, "${date[i]}", Toast.LENGTH_LONG).show()
           indexstart = i+1
       }else{
           if (indexstart >0){
               if (date[i].toString() == ","){
                   break
               }else{
                   date2+="${date[i]}"
               }
           }

       }

   }
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
//        val formattedDate = date2.format(formatter)

//        holder.yearRelease.text = getDateString(date2.toLong(), "yyyy")

        holder.rating.text = data[position].rating.toString()
        holder.category.text = data[position].categoryName
        if (data[position].isFavorite != null){
            if (data[position].isFavorite){
//            holder.btnFavorite.setImageIcon(R.drawable.ic_baseline_favorite_24)
                holder.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)

            }else{
                holder.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }


        holder.btnFavorite.setOnClickListener {
            if (data[position].isFavorite){
//            holder.btnFavorite.setImageIcon(R.drawable.ic_baseline_favorite_24)
                holder.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                data[position].isFavorite = false
                click.onclickItem(
                    position,
                    data[position].id.toString(),
                    data[position].name.toString(),
                    false)
            }else{
                holder.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                data[position].isFavorite = true
                click.onclickItem(
                    position,
                    data[position].id.toString(),
                    data[position].name.toString(),
                    true)
            }

        }

        holder.btnMore.setOnClickListener {

            clickOnMore.onclickItemOnClickMore(
                position,
                data[position].id.toString(),
                data[position].name.toString(),)


        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDateString(seconds: Long, outputPattern: String): String {
        try {
            val dateFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = seconds * 1000
            val date = calendar.time
            return dateFormat.format(date)
        } catch (e: Exception) {
          //  Log.e("utils", "Date format", e)
            return ""
        }
    }
    override fun getItemCount(): Int {
return data.size
    }
}