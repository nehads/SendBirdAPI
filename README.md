Chat API with that takes in UserId and Nickname for logging in to SendBird.

1-to-1 chatting interface is created by using Group Channel thus creating a channel id with only 2 users(Currently logged and admin).

All users are connected to the same admin 'admin123'.

Messages can be directly viewed by admin123 in the Desktop API.

InputUserActivity takes in the credentials to connect to SendBird and passes to GroupChannelActivity under groupcchannel package which establishes a connection creating a specific channel id.

GroupChatFragment handles the functions of all the buttons and other user input.

BaseApplication Activity under main package contains the app_id connected to our Dashboard.



