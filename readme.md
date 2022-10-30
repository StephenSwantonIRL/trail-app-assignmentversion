**About Trail App**

Trail app is an Android native app developed in Kotlin which allows users to store details about trails, i.e. collections of places known as trail markers.

Key features include:  
- Create, Read, Update and Delete (CRUD) of Trails in the system

- Displaying Markers on a Map

- Allowing the user to associate locally stored images with their markers

These are accompanied by a basic system of user management and users are required to authenticate in order to access the Trails stored in the system.

**The Trail & TrailMarker Models**

The data model for this project differed significantly from the one supplied in the labs as the trail app was designed to store a collection of collections rather than just a single collection.

As a result two data classes were required to represent this data effectively - TrailMarker, which contains latitude, longitude and a notes field and a Trail which contains a collection of Trail Markers and associated meta data about the collection, such as description as well as fields for future implementation such as distance (intended to be length between first point in trail and last point if travelling through all intermediate markers)

In anticipation of future complexity a design decision was taken to separate trails and markers into separate activities but retain them as a single storage object within file storage. This would allow additional screen space for entry and representation of data. Mindful that a user could be making edits to markers repeatedly within a single session and to minimise repeated loading and reloading of the entire data set a temporary Trail object was created that would facilitate the persistence of operations on a single trail until such time as the trail was closed out of and changes were saved.

Randomly generated numeric ids were used to identify trails and trail markers in the system.

Create, Read and Update largely replicated the approach taken in the following tutorials from the SETU HDip in Computer Science Mobile Application Development module.

<![if !supportLists]>· <![endif]>Lab-A05 Placemark-04

<![if !supportLists]>· <![endif]>Lab-A04 Placemark-03

Delete then expanded on these approaches to simply filter out the object to be deleted and then serialise the new dataset.

Challenges were encountered however in implementing the approach directly for markers as the recyclerview for these was contained in a fragment. As fragments, data needed to be included in a Bundle as a Parcelable Array and then passed as an argument to the fragment.

([https://medium.com/pongploydev/android-pass-object-arraylist-modelobject-from-activity-or-fragment-to-bundle-153847af90f0](https://medium.com/pongploydev/android-pass-object-arraylist-modelobject-from-activity-or-fragment-to-bundle-153847af90f0))

**UX ENHANCEMENTS**

**User Input Validation**

Two primary methods were employed to ensure valid data was provided by the user.

<![if !supportLists]>1. <![endif]>Restricting data that can be entered using the <EditText>  android:inputType property – this was used to require email entry for the username (“textEmailAddress”), restrict latitude and longitude to signed decimal values (“numberDecimal|numberSigned”) and present only numbers on the key pad when a number was expected.

<![if !supportLists]>2. <![endif]>Comparing the data provided against a Regular Expression representing the required format and providing feedback to the user via either Toast or Snack Bar notifications. – This was used to ensure the username matched the pattern for an email address ([https://www.abstractapi.com/tools/email-regex-guide](https://www.abstractapi.com/tools/email-regex-guide)) and that latitude and longitude would present valid data within the list of possible values.( expression used modified from [https://stackoverflow.com/posts/31408260/revisions](https://stackoverflow.com/posts/31408260/revisions) to allow 7 point precision as provided by the google location api )

**Spinner**

A spinner was implemented to provide a user friendly method of selecting between trail type options. This followed the instructions outlined in the following tutorial: [https://www.youtube.com/watch?v=ovGZYK9bq2o](https://www.youtube.com/watch?v=ovGZYK9bq2o) to set up a string array to provide the values to the spinner and a listener which captured the selected value and stored it in the trail object.

When editing a trail the value for trailtype had to then be converted back to a “position” in the string array containing the list of spinner values. Resources.getStringArray() was used to convert the resource reference for the string array into a kotlin Array allowing indexOf() to be used to identify the array position to be selected.

**User Location**

As the app is intended to record trails of markers, it was deemed desirable that a user could walk or run through the appropriate trail and tag places as they go. To enable this the app needed to obtain permissions from the user to access their location when using the app.

The following two tutorials from the Android Developer docs were relied on to implement this functionality

<![if !supportLists]>· <![endif]>Request Location Permissions - [https://developer.android.com/training/location/permissions](https://developer.android.com/training/location/permissions)

<![if !supportLists]>· <![endif]>Get Last Known Location - [https://developer.android.com/training/location/retrieve-current](https://developer.android.com/training/location/retrieve-current)

After updating the manifest with the relevant <uses-permission> tag an onClickListener was applied to a button in the layout to firstly, launch the permissions request and then secondly retrieve the last known location for the user.

This location was then stored to the marker object in preparation for saving to the JSON file as part of a Trail.

**Splash Screen**

The Android 12 Splash Screen API was used to created a branded splash screen. The implementation for this followed the tutorial provided at [https://www.youtube.com/watch?v=Loo4i5IrZ4Y](https://www.youtube.com/watch?v=Loo4i5IrZ4Y) and provides good backwards compatibility as far back as at least Android 10 via the inclusion of a dependency:

implementation 'androidx.core:core-splashscreen:1.0.0-alpha02'

This new implementation allows for pre-loading of data and other set up operations to be carried out as needed in the background while the splash screen is displayed. In this project there aren’t any network requests etc that need to be carried out so the splash screen is only displayed for as long as the app takes to load.

**Dialog Alert**

A number of key UI transactions required the user to be explicitly required to confirm their action. Firstly the delete all trails functionality could result in accidental deletion unless a secondary check was undertaken. Also the storage of user credentials as a current logged in user required a specific opt -in to prevent unauthorised disclosure of data. The following tutorial was

**USER MANAGEMENT**

To implement a basic form of user authentication a JSON store was created by generalising the approach outlined in _Mobile Application Development Lab-A07 Placemark-06_ to first create a new parcelable data class to store username and password values and an interface to define functions to be applied to the class. This interface was then implemented in a modified version of the placemarkJSONstore.kt file presented in the lab.

A second implementation of the JSON store was used as local storage for the currently logged in user. As devices are likely to be owned be a single user this feature was deemed essential. An accompanying log out function was developed to erase this stored user should the device user wish to do so.

As a security measure passwords were hashed using the Patrick Favre-Bulle bcrypt library before storage in the JSON file.

**References**

Abstract API 2022  - How to Validate Emails with Regex. [https://www.abstractapi.com/tools/email-regex-guide](https://www.abstractapi.com/tools/email-regex-guide)

Android Developer Documentation 2022 - Request Location Permissions - [https://developer.android.com/training/location/permissions](https://developer.android.com/training/location/permissions)

Android Developer Documentation 2022 - Get Last Known Location - [https://developer.android.com/training/location/retrieve-current](https://developer.android.com/training/location/retrieve-current)

Baeldung 2022 – Regular Expressions in Kotlin - [https://www.baeldung.com/kotlin/regular-expressions](https://www.baeldung.com/kotlin/regular-expressions)

Dave Drohan & Dave Hearne 2022- Mobile Application Development Lab-A04 Placemark-03. [https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-04-interfaces/unit-02-dh/book-03-placemark-03](https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-04-interfaces/unit-02-dh/book-03-placemark-03)

Dave Drohan & Dave Hearne 2022- Mobile Application Development Lab-A05 Placemark-04. [https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-05-images/unit-02-dh/book-04-placemark-o4](https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-05-images/unit-02-dh/book-04-placemark-o4)

Dave Drohan & Dave Hearne 2022- Mobile Application Development Lab-A07 Placemark-06. [https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-07-persistence/unit-02-dh/book-01-placemark-06](https://reader.tutors.dev/#/lab/setu-hdip-comp-sci-2021-mobile-app-dev.netlify.app/topic-07-persistence/unit-02-dh/book-01-placemark-06)

Google Play Services Documentation 2022 – Map Fragment [https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment)

Marco Ferrari 2017 – StackOverflow Answer to Regular expression for matching latitude/longitude coordinates?  [https://stackoverflow.com/posts/31408260/revisions](https://stackoverflow.com/posts/31408260/revisions)

Patrick Favre-Bulle 2018 - Bcrypt Java Library and CLI Tool. [https://github.com/patrickfav/bcrypt](https://github.com/patrickfav/bcrypt)

Philipp Lackner 2020 – Android Fundamentals  -Alert Dialog. [https://www.youtube.com/watch?v=PqRp3-t9GPM](https://www.youtube.com/watch?v=PqRp3-t9GPM)  

Philipp Lackner 2020  -Android Fundamentals  - Fragments. [https://www.youtube.com/watch?v=-vAI7RSPxOA](https://www.youtube.com/watch?v=-vAI7RSPxOA).

Philipp Lackner 2020 – Android Fundamentals – Spinner. [https://www.youtube.com/watch?v=ovGZYK9bq2o](https://www.youtube.com/watch?v=ovGZYK9bq2o)

Philipp Lackner 2022 – The New Splash Screen API is Insane!. [https://www.youtube.com/watch?v=Loo4i5IrZ4Y](https://www.youtube.com/watch?v=Loo4i5IrZ4Y)