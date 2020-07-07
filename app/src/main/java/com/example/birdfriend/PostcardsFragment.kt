package com.example.birdfriend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room

/**
 * A simple [Fragment] subclass.
 */

class PostcardsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_postcards, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.outofpost_button).setOnClickListener {
            findNavController().navigate(R.id.action_PostcardsFragment_to_SecondFragment)
        }
        val cardList = view.findViewById<LinearLayout>(R.id.cards_display)
// Build data

        val context = activity?.applicationContext
        if (context != null) {
            val db = UserCardsRoomDatabase.getDatabase(context)
            val userCardList = db.userCardsDao().getAlluserCards()

            for (card in userCardList) {
                Log.d("help", card.toString())
                val imgSrc = ImageView(getActivity())
                imgSrc.layoutParams = LinearLayout.LayoutParams(400, 400)
                imgSrc.setImageResource(card.nameid)
                cardList.addView(imgSrc)
            }
        } else {
            Log.d("help", "context was null")
        }

//
//        var c1 = Cards( R.drawable.post_1)
//        var c2 = Cards(R.drawable.post_2)
//        val testList =  arrayListOf<Cards>(c1,c2)
//        Log.d("cards", c1.toString())
//        for (card in testList){
//            var imgSrc =  ImageView(getActivity())
//            imgSrc.layoutParams = LinearLayout.LayoutParams(400, 400)
//            imgSrc.setImageResource(card.name)
//            cardList.addView(imgSrc)
//        }


    }
}