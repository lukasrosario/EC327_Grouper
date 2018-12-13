Grouper App
Thuy Pham, Trung Pham, Lukas Rosario, Andreas Francisco and Ethan Hung

1. Clone or download zip file from GitHub repository.
2. Open the project in Android studio.
    (Android studio will change the directory for the SDK files for you, hit OK when the prompt shows)
3. Build and run project.
4. Register as a new user or login using an existing account. (Must use valid email and password of sufficient length).
5. In the main page, a feed of all group projects availible to join will be shown. Upon clicking on a group, it will open a new page showing that group's information.
6. You can join/leave a group by clicking the button on the top right corner of the projects page. As a memmber, you will be displayed the names of other members in the group as well as their email.
7. Upon clicking on the current projects button, a list of all your current groups will also be shown. This list is also clickable.
8. To create a group, click on the '+' buttons on either the main page or current projects page. Enter in the information shown and click the button to create your group.
9. To logout, go back to the main page and click the logout button.

Test Cases:
1. Attempt to login with any information, the app will prompt you that it failed.
2. Attempt to register without an email address, app will say registration fail. Continue to then register with a valid e-mail address.
3. Choose a group that is not full and join it. Then, open the current projects page to see that it shows up under your current projects list. Then, open the project and leave it. It should no longer show up in your current projects list.
4. Click on a group that is not full, the following page should not display member lists. Once the user joins the page, the user will be able to see members. Then leave the group and it should show that the same project page with no members.
5. Create a group, enter valid information. The project group should show with the button to delete the group. Click on back button and locate the group in the main feed. Go into the project profile page and now use the delete button. Go back to the main page, the group should no longer be in the feed.
