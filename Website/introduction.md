# Closely
Closely is a social media blogging app where you connect with other users using NFC. Our blogs allow you to write blogs posts either in  plain text or you can style the content of your blog post using HTML, CSS + JavaScript to create richer content. This will then be rendered when someone views your blog post.  
Note: You do NOT need to wrap your blog post in a <HTML> or <BODY> tag to enable HTML blog posts. Simple just add tags and they will be rendered automatically.

To test our NFC features in an android emulator you can use the following app that we made to emulate an NFC intent being created https://github.com/RonanQuill/Closely-NFCSpoofing/tree/master.
The NFC message to connect with another user is created by touching 2 NFC capable device together, unfortunately it is not possible to demonstrate this without physical devices.

# Testing

When testing the application you can sign up for the application and create your own profile. If you want to connect to another user that is currently created use the NFC Spoofer above with one of the UIDs of the users listed below.

# Features
- Sign Up.
- Sign In.
- Create Profile with image uploading.
- Auto generated profile image if you do not choose one.
- Edit Profile (allows for editing of bio and profile image)
- Create a blog (blogs now have images too)
- Full support for HTML, CSS + JavaScript in blog posts to enhance presentation
- View individual blogs
- Comment on a blog
- View all blogs you have created
- View all users you are connected to and visit their profile
- Full custom UI
- Sending NFC connections requests from home page

## Users
| UID        | Username           | Number of Posts  | Email| password
| ------------- |:-------------:| -----:|:--------: | :--------: |
| VSDTqD2Kxjhv72kGu1KW5XvI6z62      | User 1 | 9| user@email.com | password |
| aNW8sh1ltONghyHzYCzSjk3omkE2      | user 4     |   4 |user4@email.com|  password |
| 2FDtyKEfsKNdkky4n8c8qTYxLQr1| User2      |    0 |user1@email.com| password |


## Firebase Project Overview
[Closely Firebase Project](https://console.firebase.google.com/project/closely-34ee4/overview)
