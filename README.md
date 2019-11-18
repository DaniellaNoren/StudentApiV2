# Student API
Simple Student API made for Schoolproject

## JSON Bodies

### Student

    {
        forename: "String",
    
        lastName: "String",
    
        email: "String",
    
        subjects: SubjectArray
    }

### Subject

    {
    
        title: "String",
    
        students: StudentsArray,
    
        teacher: Teacher
    
    }
    
### Teacher

    {
        lastName: "String",
        
        subjects: SubjectArray
    }    


# Paths

**GET:**

/school/api/students 
>Returns list of students

/school/api/students?lastName=lastName 
>Returns list of students with specified last name

/school/api/students/{email}
>Returns student with specified email

/school/api/subjects
>Returns list of subjects

/school/api/subjects/{title}
>Returns subject with specified title

/school/api/teachers
>Returns list of teachers

/school/api/Students/{lastName}
>Returns teacher with specified last name


**POST:**

/school/api/students
>Student-JSON in body, adds a new Student

/school/api/subjects
>Subject-JSON in body, adds a new Subject

/school/api/teachers
>Teacher-JSON in body, adds a new Teacher

**PUT:**

/school/api/students/{email}?forename=forename&lastName=lastName
>Replaces forename and last name of Student with specified email

**PATCH:**

/school/api/students/{email}?forename=forename
>Replaces forename of Student with specified email

/school/api/students/{email}/{subjectTitle}
>Adds student to subject with specified title

/school/api/subjects/{title}?newTitle=newTitle
>Replaces subject-title with new title

/school/api/teachers/{lastName}?newName=newName
>Replaces last name witch specified new name

/school/api/teachers/{lastName}/{subjectTitle}
>Adds teacher to subject with specified subject title

**DELETE:**

/school/api/students/{email}
>Deletes Student with specified email

/school/api/subjects/{title}
>Deletes Subject with specified title

/school/api/teachers/{lastName}
>Deletes teacher with specified last name

## Wildfly configuration

Install any Wildfly release you want. I use 18.

Add a user, and place the school.cli script under the bin folder.<br>
Create database school. The script will need a mysql connector under `C:\Users`
to work. 

The script is predefined for `mysql.connector-java-8.0.12.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.









 
