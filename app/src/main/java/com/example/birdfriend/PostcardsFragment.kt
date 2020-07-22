package com.example.birdfriend

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_postcards.*


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
        //Button to delete all

            view.findViewById<Button>(R.id.delete_all_button).setOnClickListener{
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(activity)

                // set message of alert dialog
                dialogBuilder.setMessage("Do you want to delete all postcards?")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Yes", DialogInterface.OnClickListener {
                            dialog, id ->
                            db.userCardsDao().deleteAll()
                            findNavController().navigate(R.id.action_PostcardsFragment_to_SecondFragment)
                            Toast.makeText(activity,"Deleted All Post Cards", Toast.LENGTH_LONG).show()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Delete action:")
                // show alert dialog
                alert.show()
            }



            val userCardList = db.userCardsDao().getShowCards()

            for (card in userCardList) {

                val imgSrc = ImageView(getActivity())
                imgSrc.layoutParams = LinearLayout.LayoutParams(600, 650)
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


                    }

                    window.showAsDropDown(textView)

                }
            }
        } else {
            Log.d("help", "context was null")
        }


    }
}
