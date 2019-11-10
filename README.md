# Student API
Simple Student API made for Schoolproject

## JSON Body

{

    forename: "String",
    
    lastName: "String",
    
    email: "String"
}
## Paths

**GET:**


/school/api/students 
>Returns list of students

/school/api/students?lastName=lastName 
>Returns list of students with specified last name

/school/api/students/{email}
>Returns student with specified email

**POST:**

/school/api/students
>Student-JSON in body, adds a new Student

**PUT:**

/school/api/students/{email}?forename=forename&lastName=lastName
>Replaces forename and last name of Student with specified email

**PATCH:**

/school/api/students/{email}?forename=forename
>Replaces forename of Student with specified email

**DELETE:**

/school/api/students/{email}
>Deletes Student with specified email

## Wildfly configuration

Install any Wildfly release you want. I use 18.

Add a user, and place the school.cli script under the bin folder.<br>
Create database school. The script will need a mysql connector under `C:\Users`
to work. 

The script is predefined for `mysql.connector-java-8.0.12.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.



 
