<!---
Markdown notes:
1) There are two spaces at the end of each line in order to have have line breaks after each line.
2) The double asterisks make the user stories bold.
-->

**As a user**  
**I want voting buttons on the side of each post**  
**So I can up-vote and down-vote each post when I am logged in.**  

scenario 1:  
Given a user is currently not logged in  
when the upvote button is pressed on a post that the user has not voted on already  
then a dialog pops up alerting the user that they must be logged in to upvote.  

scenario 2:  
Given a user is currently not logged in  
when the downvote button is pressed on a post that the user has not voted on already  
then a dialog pops up alerting the user that they must be logged in to downvote.  

scenario 3:  
Given a user is currently logged in  
when the upvote button is pressed on a post that the user has not voted on already  
then the upvote button becomes orange and the downvote button remains grey.  

scenario 4:  
Given a user is currently logged in  
when the downvote button is pressed on a post that the user has not voted on already  
then the downvote button becomes blue and the upvote button remains grey.  

scenario 5:  
Given a user is currently logged in and has upvoted a post  
when the upvote button is pressed on this post (the one which was previously upvoted)  
then the upvote button becomes grey and the downvote button remains grey.  

scenario 6:  
Given a user is currently logged in and has downvoted a post  
when the downvote button is pressed on this post (the one which was previously downvoted)  
then the downvote button becomes grey and the upvote button remains grey.  

scenario 7:  
Given a user is currently logged in and has upvoted a post  
when the downvote button is pressed on this post (the one which was previously upvoted)  
then the upvote button becomes grey and the downvote button becomes blue.  

scenario 8:  
Given a user is currently logged in and has downvoted a post  
when the upvote button is pressed on this post (the one which was previously downvoted)  
then the downvote button becomes grey and the upvote button becomes orange.  

**As a user**  
**I want a score on the side of each post**  
**so I can see how many net upvotes (i.e. upvotes - downvotes) each post has to determine a post's popularity**  

scenario 1:  
Given a user is currently logged in  
when the upvote button is pressed on a post that the user has not voted on already  
then the net upvote count is increased by 1 and is colored orange.  

scenario 2:  
Given a user is currently logged in  
when the downvote button is pressed on a post that the user has not voted on already  
then the net upvote count is decreased by 1 and is colored blue.  

scenario 3:  
Given a user is currently logged in and has upvoted a post  
when the upvote button is pressed on this post (the one which was previously upvoted)  
then the net upvote count is decreased by 1 and is colored grey.  

scenario 4:  
Given a user is currently logged in and has downvoted a post  
when the downvote button is pressed on this post (the one which was previously downvoted)  
then the net upvote count is increased by 1 and is colored grey.  

scenario 5:  
Given a user is currently logged in and has upvoted a post  
when the downvote button is pressed on this post (the one which was previously upvoted)  
then the net upvote count is decreased by 2 and is colored blue.  

scenario 6:  
Given a user is currently logged in and has downvoted a post  
when the upvote button is pressed on this post (the one which was previously downvoted)  
then the net upvote count is increased by 2 and is colored orange.  

**As a user**  
**I want to be able to log in**  
**So I can log in to my account.**  

scenario 1: 
Given a correct username and incorrect password  
when the user is trying to login to his/her account  
then the user will be alerted that the login crudentials are inccorect.  

scenario 2:  
Given a incorrect username and correct password  
when the user is trying to login to his/her account  
then the user will be alerted that the login crudentials are inccorect.  

scenario 3:  
Given a blank username and correct password field  
when the user is trying to login to his/her account  
then the user will be alerted that the login crudentials are inccorect.  

scenario 4:  
Given a correct username and correct password  
when the user is trying to login to his/her account  
then the user will be logged into his/her account.  

**As a user**  
**I want a subreddit navigation bar**  
**So I can easily navigate other subreddits** 

scenario 1: 
Given the user is on the home page
When the user clicks on the all button
Then the user should be redirected to a page consisting of all of Reddit's subreddits

scenario 2:
Given the user is on the home page
When the user clicks on the dropdown list on the top bar and then clicks on the 'ASKREDDIT' link in the list
Them the user should be redirected to the AskReddit subreddit

scenario 3:
Given the user is on the all page 
When the user clicks to the front button on the top bar
Then the user should be redirected to Reddit's home page

scenario 4:
Given the user is on the home page 
When the user clicks on the random button on the top bar
Then the user should be redirected to a random subreddit
 
