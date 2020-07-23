# BirdFriend
> BirdFriend is an Android app inspired by "Tabikaeru" - a mobile game about a travelling frog. Bird himself is inspried by "Haikyuu" - a volleyball sports anime. 

> Bird is either home or away, while bird is away, he will send users post cards (notifications) about his adventures.

> Tech Stack: Kotlin and Android SDK

![Demo](documentation/result.gif)

## Table of Contents
- [Kotlin Tech Highlights](#kotlin-tech-highlights )
- [App Features](#app-features)
- [FAQ](#faq)


## Kotlin Tech Highlights 

- [WorkManager](#workmanager)
- [Room Database](#room-database)
- [NotificationCompat](#notificationcompat)
- [Bitmap and Canvas](#bitmap-and-canvas)
- [Intent](#intent)
- [AnimationDrawable](#animationdrawable)
- [Permission](#permission)

## WorkManager
> WorkManger was used to schedule tasks on logging bird's latest status of home or away to log_state_table. And if away it will also queue for adding a new post card to trigger notification for users by changing image status to TRUE. 

![Work Graph](documentation/work.png)

Dependencies added for Work to build.gradle
```shell
    def work_version = "2.3.4"

    // (Java only)
    implementation "androidx.work:work-runtime:$work_version"

    // Kotlin + coroutines
    implementation "androidx.work:work-runtime-ktx:$work_version"
```
## Room Database
> Room database was used to create user_cards_table and log_state_table (for local storage). 3 main components of room: Entity, Dao and Database. 

![Data Table](documentation/table.png)

Dependencies added for Room to build.gradle
```shell
def room_version = "2.2.5"

    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation "androidx.room:room-ktx:$room_version"

    // optional - RxJava support for Room
    implementation "androidx.room:room-rxjava2:$room_version"

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation "androidx.room:room-guava:$room_version"

    // Test helpers
    testImplementation "androidx.room:room-testing:$room_version"
```

## NotificationCompat
> NotificationCompat APIs was used to set up notification to inform users of new post card from bird! 

Dependencies added for Work to build.gradle
```shell
    //noinspection GradleCompatible
    implementation "com.android.support:support-compat:28.0.0"
```

## Bitmap and Canvas
> The task of adding a bird's image on top of a user's photo requires some image manipulation and media st

## Intent 
> Intent was used to share post cards to other apps (Instagram, messages etc.)

Example code
```shell
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
```


## AnimationDrawable
> AnimationDrawable was used to create frame-by-frame animations of bird. 

Example animation xml file in drawable folder

```shell
<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:drawable="@drawable/fly_p1"
        android:duration="200"/>
    <item android:drawable="@drawable/fly_p2"
        android:duration="200"/>
    <item android:drawable="@drawable/fly_p3"
        android:duration="200"/>

</animation-list>
```


## Permission

> Manifest.permission was used to access exteranl photo of user's for bird and user image process. 

Add following to AnroidManifest.xml
```shell
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
---


## App Features 
![Home Page](documentation/street.png)
- Notifications of post cards from bird
- Put a bird on it (user's own photo)
- Share post card and photo to instagram or other apps 

#### Put a bird on it
![Put a bird on it](documentation/put.gif)

#### Post Cards
![Post Cards](documentation/post_card.gif)

---

## FAQ

- **Why did I code BirdFriend?**
    - BirdFriend is a Capstone project I worked on during Ada Developers Academy Cohort 13! (3 weeks project July 2020)

- **What is the hardest part of this project for me?**
    - Setting up bird's home or away status from backend. I used the WorkManager to queue for work request on logging bird's latest new status of away or home (see [WorkManager](#workmanager))
    - Adding bird's image to user's photo and sharing it to other apps (see [Bitmap and Canvas](#bitmap-and-canvas)). 

- **What you learned?**
    - Kotlin! And it is very fun to create an App 
