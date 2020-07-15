package com.example.birdfriend

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var flyAnimation: AnimationDrawable
    lateinit var pizzaAnimation: AnimationDrawable
    lateinit var sleepAnimation: AnimationDrawable

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.outside_button).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        view.findViewById<Button>(R.id.postcards_button).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_PostcardsFragment)
        }

        view.findViewById<Button>(R.id.pizza_button).setOnClickListener {
            val pizzaImage = view.findViewById<ImageView>(R.id.fly).apply {
                setBackgroundResource(R.drawable.pizza_animation)
                pizzaAnimation = background as AnimationDrawable
            }
            pizzaAnimation.start()

        }

//        Log.d("check", MainActivity.mainHomeStatus.toString())

        //set up random activity : fly, sleep etc.

        val sleepImage = view.findViewById<ImageView>(R.id.sleep).apply {
            setBackgroundResource(R.drawable.sleep_animation)
            sleepAnimation = background as AnimationDrawable
        }

        val flyImage = view.findViewById<ImageView>(R.id.fly).apply {
            setBackgroundResource(R.drawable.animation)
            flyAnimation = background as AnimationDrawable
        }
//        flyAnimation.start()
            flyImage.setOnClickListener({ flyAnimation.start() })

        val bagImage = view.findViewById<ImageView>(R.id.bag)

// HOME OR NOT CALL FROM LOG STATE
        val context = activity?.applicationContext
        if (context != null) {
            val dbLogState = LogStateDatabase.getDatabase(context)
            val lastState = dbLogState.logStateDao().getLastState()
            if (lastState.isNotEmpty() && lastState.first().stateHomeAway == "Away") {
                flyImage.isVisible = false
                sleepImage.isVisible = false
                bagImage.isVisible = false

            } else {
                //Night Time Always show sleeping

                val currentTime = Date()
                val c = Calendar.getInstance()
                c.time = currentTime
                val t = c[Calendar.HOUR_OF_DAY] * 100 + c[Calendar.MINUTE]
                //Test daytime 9am
//              val t = 900
                val checkNight : Boolean = t > 1700 || t < 800
//                Log.i("TitleFragment", "$checkNight")

                if (checkNight){
                    view.setBackgroundResource(R.drawable.couch_night)
                    setBirdPosition(view,0)
                } else {
                    setBirdPosition(view,MainActivity.setBird)
                }

                // show one position of animation

//                flyAnimation.start()
//                sleepAnimation.start()
            }
        }

    }



    // METHOD to random draw an animation
    fun setBirdPosition(view: View, position: Int){
        val sleepImage = view.findViewById<ImageView>(R.id.sleep).apply {
            setBackgroundResource(R.drawable.sleep_animation)
            sleepAnimation = background as AnimationDrawable
        }

        val flyImage = view.findViewById<ImageView>(R.id.fly).apply {
            setBackgroundResource(R.drawable.animation)
            flyAnimation = background as AnimationDrawable
        }

        val birdOptinos =  position
        //position 1 is fly, 0 is sleep etc.


        if (birdOptinos == 1 ){
            sleepImage.isVisible = false
            flyAnimation.start()
        } else {
            flyImage.isVisible = false
            sleepAnimation.start()
        }

    }


}




//Logging lifecycle
/**
///LEARNING
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
Log.i("TitleFragment", "onCreate called")
}
override fun onActivityCreated(savedInstanceState: Bundle?) {
super.onActivityCreated(savedInstanceState)
Log.i("TitleFragment", "onActivityCreated called")
}
override fun onStart() {
super.onStart()
Log.i("TitleFragment", "onStart called")
}
override fun onResume() {
super.onResume()
Log.i("TitleFragment", "onResume called")
}
override fun onPause() {
super.onPause()
Log.i("TitleFragment", "onPause called")
}
override fun onStop() {
super.onStop()
Log.i("TitleFragment", "onStop called")
}

override fun onDestroyView() {
super.onDestroyView()
Log.i("TitleFragment", "onDestroyView called")
}
override fun onDetach() {
super.onDetach()
Log.i("TitleFragment", "onDetach called")
}
 */