package com.example.birdfriend

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*


class FirstFragment : Fragment() {

    // added for notification
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private  val channelId = "com.example.birdfriend"
    private  val description = "BirdFriend Notification"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showText = view.findViewById<TypeWriter>(R.id.tv)
        showText.setText("")
        showText.setCharacterDelay(150)
        showText.animateText("Hi bird friend!")

        view.findViewById<Button>(R.id.door_button).setOnClickListener {

            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // add Log button to show database
        val context = activity?.applicationContext

        view.findViewById<Button>(R.id.show_log_button).setOnClickListener{
            if (context != null) {
                val db = LogStateDatabase.getDatabase(context)
                val allLog = db.logStateDao().getlogState()

                val dbUser = UserCardsRoomDatabase.getDatabase(context)
                val allUserCards = dbUser.userCardsDao().getAlluserCards()

                for (log in allLog){
                    Log.d("log_but",log.uid.toString())
                    Log.d("log_but",log.creationDate.toString())
                    Log.d("log_but",log.stateHomeAway.toString())
                }

                for (card in allUserCards){
                    Log.d("log_but",card.imgname.toString())
                    Log.d("log_but",card.cardstatus.toString())
                }


            }
        }



        // Night background after 17pm and before 8am

        val currentTime = Date()
        val c = Calendar.getInstance()
        c.time = currentTime
        val t = c[Calendar.HOUR_OF_DAY] * 100 + c[Calendar.MINUTE]
        val checkNight : Boolean = t > 1700 || t < 800
        if (checkNight){
            view.setBackgroundResource(R.drawable.trash_night)
        }

        // letter bounce animation
        val bounceAnimation = AnimationUtils.loadAnimation(activity?.applicationContext,R.anim.bounce_animation)
        val letterView = view.findViewById<ImageView>(R.id.letter_img)
        val db = activity?.applicationContext?.let { UserCardsRoomDatabase.getDatabase(it) }
        val showLetterOrNot = db?.userCardsDao()?.getNewCards().isNullOrEmpty()
        if (!showLetterOrNot){
            letterView.startAnimation(bounceAnimation)
        }

        // MAIL BOX POP UP

        view.findViewById<Button>(R.id.temp_notify_button).setOnClickListener{
            var cardMail = view.findViewById<LinearLayout>(R.id.mail_pop_up)
            var window = PopupWindow(activity)
            var view = layoutInflater.inflate(R.layout.dialog_mail_pop_up, null)
            window.contentView = view


            if (context != null){
                val db = UserCardsRoomDatabase.getDatabase(context)
                val userCardList = db.userCardsDao().getNewCards()

                if (userCardList.isNullOrEmpty()) {
                    //Hide Add button
                    val hideAddButton = view.findViewById<Button>(R.id.add_post_button)
                    hideAddButton.isVisible = false
                } else {
                    val newPost = userCardList[0]

                    var resourceID = resources.getIdentifier(
                        newPost.imgname,
                        "drawable",
                        "com.example.birdfriend"
                    )
                    var imageView = view.findViewById<ImageView>(R.id.mail_view)
                    imageView.setImageResource(resourceID)

                    val addButton = view.findViewById<Button>(R.id.add_post_button)

                    addButton.setOnClickListener{
                        //change db card status to true
                        db.userCardsDao().updateCard(newPost.imgname,true)
                        setAddCardStatus()
                        window.dismiss()
                        letterView.clearAnimation()
                    }
                }

                val dismissButton = view.findViewById<Button>(R.id.mail_pop_up_dismiss)
                dismissButton.setOnClickListener{
                    window.dismiss()
                }

            }

            window.showAsDropDown(textview_first)

        }


    }

    private fun setAddCardStatus(){
        Log.d("FirstFragment","set add card status called!")
    }

}




