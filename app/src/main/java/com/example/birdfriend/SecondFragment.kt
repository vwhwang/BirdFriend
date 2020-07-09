package com.example.birdfriend

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.util.Log
/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var flyAnimation: AnimationDrawable
    lateinit var pizzaAnimation: AnimationDrawable

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

// try hide flyimage if away status
        Log.d("check", MainActivity.mainHomeStatus.toString())

        val flyImage = view.findViewById<ImageView>(R.id.fly).apply {
            setBackgroundResource(R.drawable.animation)
            flyAnimation = background as AnimationDrawable
        }
//        flyAnimation.start()
            flyImage.setOnClickListener({ flyAnimation.start() })




        //tyring to check for backenddisplay
//        view.findViewById<Button>(R.id.textview_second).setOnClickListener {
//            setOneTimeWorkRequet()
//        }

    }


}