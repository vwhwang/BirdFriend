package com.example.birdfriend

import android.net.Uri
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
import android.content.Intent as Intent

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

            //CODES TO MANUALLY UPDATE DATA FOR TESTING
//            db.userCardsDao().insertCards(UserCards("post_2",true))
//            db.userCardsDao().updateCard("lao_post",false)
//            db.userCardsDao().updateCard("post_1",false)
//            db.userCardsDao().insertCards(UserCards("post_4",false))

            val userCardList = db.userCardsDao().getShowCards()

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

                    val shareButton = view.findViewById<Button>(R.id.share_button)
                    shareButton.setOnClickListener{
                        var imageName = card.imgname
                        val imageURI = Uri.parse("android.resource://com.example.birdfriend/"+resourceID)

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, imageURI)
                            putExtra(Intent.EXTRA_TEXT, "My bird friend went on an adventure today! $imageName")
                            type = "image/jpg"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Send your image:")
                        startActivity(shareIntent)


//                        val sendIntent: Intent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//                            type = "text/plain"
//                        }
//
//                        val shareIntent = Intent.createChooser(sendIntent, null)
//                        startActivity(shareIntent)
                        Log.d("PostCardFragment", R.drawable.post_1.toString())
                    }

                    window.showAsDropDown(textView)

                }
            }
        } else {
            Log.d("help", "context was null")
        }


    }
}