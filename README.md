# Tidal-interview
Employee Information application

### Description <br/>
This app is a basic application used display a list of employees retrieve from a backend api. This app will only update when the user force update by pull to refresh. The application will store the information from the api in a Room Database and only update his information when a force refresh is triggered.

### Libraries
- Coil - Image display
- Timber - Logging library
- Room - Database library
- Hilt - Dependency Injection
- Retrofit - Api network call support library

### Structure

***Core: /repo***
- ds - Datasource <br/>
- EmployeeRemoteDataSource - remote api class to pull information from backend
- EmployeeLocalDataSource - used to retrieve information from room db
- EmployeeRepository - Repository model encapsulating the data retrieval process<br/>
- RepoDi - Hilt dependency inject class<br/>

***UI: /ui***
- screen/EmployeesScreen - Compose screen for showing employee details<br/>
- screen/EmployeesViewModel - ViewModel that loads the employee information when its initialized and prepare for the ui display.
- theme - Generate theme information

MainActivity - The Single Activity used for displaying the screen<br/>
RickMortyApplication - Lightweight Application instance used for initializing Hilt<br/>
