# ctp471_team_project

If you use smart import of eclipse, you can easily build projects.

### project list
- project 1 : Data Collection
- project 2 : Data Visualization

### Used Tool version
- eclipse : 2020-03 (4.15.0)
- HeidiSQL : 11.0.0.5919 (64Bit)
- Chrome (local) : 81.0.4044.138
- FireFox (linux) : Mozilla Firefox 76.0.1
### Language version
- java (local) : 1.8.0_251
- java (linux) : 1.8.0_252
- processing : 3.5.4

### Test server information
- linux server ssh info
  - ip : 211.231.80.215
  - id : ctp471
  - pwd : ctp471
- linux server sftp info : you can download or upload through this
  - ip : 211.231.80.215 (same as above)
  - id : ctp471 (same as above)
  - pwd : ctp471 (same as above)
  - port : 22
- db : You can see collected data here.
  - network : MySQL (TCP/IP)
  - Library : libmariadb.dll
  - ip : 211.231.80.215 (same as above)
  - id : ctp471 (same as above)
  - pwd : ctp471 (same as above)
  - port : 3306

### How to run Task & Task rule (Data Collection project)
- extract DataCollection project to "Runnable JAR File"
	- Launch configuration : CollectFriendsTask, UpdateKAISTFriendsListTask or UpdateUserInfoTask
		- it will run main function of the class
		- choose one from above three.
	- Export destination : set name of jar file & directory where you make jar file.
	- Library handling : extract required libraries into generated JAR
- upload jar file to test server
	- you can easily run by java command
		- DISPLAY=:1 java -cp {jar file name}.jar task.UpdateUserInfoTask
		- DISPLAY=:1 java -cp {jar file name}.jar task.CollectFriendsTask
		- java -cp {jar file name}.jar task.UpdateKAISTFriendsListTask
- crontab : run the task repeatedly. find from google how to use.
	- you can see crontab with command "crontab -l"
	- UpdateUserInfoTask : 5,15,25,35,45,55 minute. find 10 users for each..
	- CollectFriendsTask : 0,20,40 minute. collect 3 users' friends for each.
	- UpdateKAISTFriendsListTask : every day 09:30. update 100 users every day.
