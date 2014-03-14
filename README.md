quizorama - cs108 final project
========================================================================
Team members (4): Tyler Conklin, Ali Fauci, Adrian Spanu, Chris Williams

--note: Adrian and Chris are 'local' distance students (w/in 25 miles of campus)

Database:
========================================================================
The files FinalDB.sql (creates schema) and TestData.sql (data) should be
sourced

Features implemented:
========================================================================
-- Users:
   - guest, standard, and admin types
     - guests can view minimal user-summary info and quiz summarries
       but cannot take quizzes, create quizzes, flag quizzes, send messages, 
       or friend other users
   - Login system, with password hashing and salt
      - Password hashing was taken from the internet at http://crackstation.net/hashing-security.htm
      - Cookie generation was added to the PasswordHash class by the authors
   - achievements based on the number of quizzes created and taken, 
     displayed on user summary pages
   - standard and admin users can friend and unfriend each other
   - each non-guest user has an associated page, non-guest

-- Quiz question types:
   - Question-response
   - Fill in the blank 
   - Multiple choice
   - Picture-response

-- Quiz properties and options
   - Quiz summary
   - Random vs defined ordering of questions
   - Immediate correction vs answers at the end
     - Summary quiz results displayed at end of quiz regardless 
   - Quiz categories and tags
   - Quiz rating system. Non-guest users can update their rating at any time
   - Quiz history (creation and scores), per user per quiz
     - Quiz summary stats displayed on quiz summary page
     - Histories displayed on quiz summary page and on user summary page
   - Quiz flagging (non-admin users, not guests)
   - Quiz deletion (admin users)
   - Option for taking in practice mode, if the quiz creator allowed
     - Note: our practice mode is not like the one suggested in the handout. Instead it simply allows the user to take the quiz without having their score recorded. 

-- Search
   - all user types can search for users, filtering by user name
   - all user types can search for quizzes, filtering by quiz name, as
     well as by quiz tags and categories

-- Messages
   - Notes and auto-generated friend requests, quiz challenges, and quiz
     flag messages for non-admin non-guest users
   - Site-wide announcements can be sent by admins to all users, displayed 
     on a user's own page
   - Quiz flagging messages sent to admins when a user flags a quiz
   - Guests cannot send, receive, or view any type of messages
   - Inbox sorted by date and has concept of read vs unread messages
   - Deletion functionality for all messages
   - Reply functionality for the note type
   - Error handling for valid recipients and quizzes (for challenges)

-- Extensive page display customization
