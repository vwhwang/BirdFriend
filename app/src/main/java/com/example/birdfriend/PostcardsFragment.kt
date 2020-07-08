package com.example.birdfriend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_postcards.*

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

//            db.userCardsDao().deleteAll()

            //current db has post 1 already
//            db.userCardsDao().insertCards(UserCards("post_2",true))

            val userCardList = db.userCardsDao().getAlluserCards()

            for (card in userCardList) {

                val imgSrc = ImageView(getActivity())
                imgSrc.layoutParams = LinearLayout.LayoutParams(400, 400)

                //setting up imagename to resourceID

                var resourceID = resources.getIdentifier(
                    card.imgname,
                    "drawable",
                    "com.example.birdfriend"
                )

                imgSrc.setImageResource(resourceID)
                cardList.addView(imgSrc)

                //click on image to show popup window

                imgSrc.setOnClickListener(){

                    var window = PopupWindow(activity)
                    var view = layoutInflater.inflate(R.layout.dialog_pop_up, null)
                    window.contentView = view
                    var imageView = view.findViewById<ImageView>(R.id.imageView)
                    imageView.setImageResource(resourceID)

//                    imageView.setOnClickListener{
//                        window.dismiss()
//                    }

                    val dismissButton = view.findViewById<Button>(R.id.dismiss_button)
                    dismissButton.setOnClickListener{
                        window.dismiss()
                    }

                    window.showAsDropDown(textView)

                }
            }
        } else {
            Log.d("help", "context was null")
        }


    }
}