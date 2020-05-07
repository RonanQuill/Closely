# Technical Design of Closely

- [Firebase Firestore](https://firebase.google.com/docs/firestore)
- [Firebase Auth](https://firebase.google.com/docs/auth)
- [Firebase Storage](https://firebase.google.com/docs/storage)
- [Glide](https://bumptech.github.io/glide/)
- [Material Design Components](https://material.io/design)
- [avatars.adorable.io](http://avatars.adorable.io/)

## Firebase Firestore
We use Firestore to store our user data and blog data, this data includes things likes comments on blogs and connections a user has.

## Firebase Auth
We use Firebase Auth to add authentication to our app, this service is behind our sign in and sign up and integrates well with 
the other Firebase services we use 

## Firebase Storage
We use Firebase Storage for storing images. The images that we store are the user's profile image and blog images. 

## Glide
"Glide is a fast and efficient image loading library for Android focused on smooth scrolling". We use glide for all image loading from 
external sources such as profiles images and blog images.

## Material Design Components
Instead of creating completely custom UI components we chose to use some that have already been created for us that work well with
Android Jetpack. We use Material Design Bottom Navigation for the navigation bar in our project.

## Avatars.adorable.io
This is an API that generates profile images based on a string, we use this to populate a users profile image when they 
have not specificied a different profile image to use.

# Technical Choices
Many of our techincal choices due to learning android best practices. At the beginning of development we were using older fragment navigation
methods to create the navigation for our application. We later found out that the way we were doing it was both more difficult and outdated.
We switched to using Navigation from Android Jetpack which allowed us to use Navigation Graphs for all pages of our app. This was 
especially helpful in getting the bottom navigation to work as Navigation integrated well with the Material Design BottomNavigation
component.

With regards to our NFC implementation, we originally wanted to use a technology called host card emulation to do the NFC connecting in
our application. We later found our that Android offers a simple API with Android Beam that allows us to use NFC without having to write a 
lot of unnecesary code. Discovering Beam made developing this portion of the project much simpler.


## Lessons Learned
Often times we jumped into implementing things that we didnt have full knowledge of i.e bottom navigation. We spent a long time making
workarounds for things that didn't work only to realise that there were newer ways to do something that was a lot simpler. We began to understand
the importance of properly researching something before implementing it and this reduced the amount of time we wasted. 

We learned that Android is an every changing ecosystem and it is difficult to find the most up to date best practices and APIs to use. 
It was not easy to find solutions to problems that we ran into and often time we had to try to figure something out ourselves which didnt
always work. Android has a lot of APIs that are constantly changing and improving which means that documentation goes out of date very quickly.
We found this most often when writing Java because lots of the latest android tutorials and documentation is written in Kotlin. The reliance on
Java meant we were often looking at old tutorials and not being shown the latest stuff.
