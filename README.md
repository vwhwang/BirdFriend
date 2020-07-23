# BirdFriend
> BirdFriend is an Android app inspired by "Tabikaeru" - a mobile game about a travelling frog. Bird himself is inspried by "Haikyuu" - a volleyball sports anime. 

> Bird is either home or away, while bird is away, he will send users post cards (notifications) about his adventures.

> Tech Stack: Kotlin and Android SDK

<!-- ![Demo](documentation/result.gif) -->

## Table of Contents
- [Kotlin Tech Highlights](#kotlin-tech-highlights )
- [App Features](#app-features)
- [FAQ](#faq)


## Kotlin Tech Highlights 

- [WorkManager](#workmanager)
- [Room Database](#room-database)
- [NotificationCompat](#notificationcompat)
- [Intent](#intent)
- [AnimationDrawable](#animationdrawable)
- [Bitmap and Canvas](#bitmap-and-canvas)
- [Permission](#permission)

## WorkManager
> WorkManger was used to queue for work requests on logging bird's latest status of home or away. And if away it will also queue for adding a new post card to trigger notification for users. 

![Work Graph](documentation/work.png)
## Room Database
> Room database stored user_cards_table and log_state_table (local storage)

![Data Table](documentation/table.png)

- Entity
- Dao
- Database

## NotificationCompat
> NotificationCompat APIs was used to set up notification for new post cards in mail box. 

## Intent 
> Intent was used to share data to external of App

## AnimationDrawable
> AnimationDrawable was used to create frame-by-frame animations of bird. 

## Bitmap and Canvas

## Permission

> Manifest.permission was used to access exteranl photo of user's for bird and user image process. 

Add following to AnroidManifest.xml
```shell
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
---


## App Features 

- Notifications of post cards from bird
- Put a bird on it (user's own photo)
- Share post card and photo to instagram or other apps 

#### Put a bird on it
<!-- ![Put a bird on it](documentation/put.gif) -->

#### Post Cards
<!-- ![Post Cards](documentation/post_card.gif) -->

---

## FAQ

- **Why did I code BirdFriend?**
    - BirdFriend is a Capstone project I worked on during Ada Developers Academy Cohort 13! (3 weeks project July 2020)

- **What is the hardest part of this project for me?**
    - Setting up bird's home or away status from backend. I used the WorkManager to queue for work request on logging bird's latest new status of away or home (see [WorkManager](#workmanager))

- **What you learned?**
    - Kotlin! And it is very fun to create an App 
