# suburb-service

This MS provides functionality for suburb information. It exposes following API's:
1. Add a new suburb
2. Get Suburbs by post code
3. Get Suburbs by nam

## API 1: Create a new suburb
This is a secured enpoint that creates a new suburb in the System. <br>
<b>Http Method:</b> POST <br>
<b>Request Path:</b> /v1/suburb/{postCode} <br>
<b>Headers</b>: Basic Authentication headers <br>
<b>Response:</b> No body <br>
<b>Response Code:</b> 200 <br> <br>
Sample Request Body
<pre class-"prettyprint">
{
	"suburbName": "Cremorne",
	"postCode": "2090",
	"state": "NSW",
	"country": "Australia" 
}
</pre>

Sample curl command
<pre class-"prettyprint">
curl -X POST \
  http://localhost:8080/v1/suburb \
  -H 'authorization: Basic XXXXXXXXXXXXXXXXXXXX' \
  -H 'content-type: application/json' \
  -d '{
	"suburbName": "Cremorne",
	"postCode": "2090",
	"state": "NSW",
	"country": "Australia" 
}'

Note - Please contact me for the Authorisation headers
</pre>

## API 2: Get Suburbs by post code
This is a public endpoint that returns a list of suburbs matching the post code.

<b>Http Method:</b> GET <br>
<b>Request Path:</b> /v1/suburb/postcode/{postCode} <br>
<b>Response:</b> A list of suburbs mathching the criteria.<br>
<b>Response Code:</b> 200 <br><br>
Sample Response:
<pre class-"prettyprint">
[
    {
        "id": 2,
        "suburbName": "Point Cook",
        "postCode": 3030,
        "state": "Victoria",
        "country": "Australia"
    },
    {
        "id": 4,
        "suburbName": "Werribee",
        "postCode": 3030,
        "state": "Victoria",
        "country": "Australia"
    }
]
</pre>

Sample curl command
<pre class-"prettyprint">
curl -X GET http://localhost:8080/v1/suburb/postcode/2090
</pre>

## API 3: Get Suburbs by name
This is a public endpoint that returns a list of suburbs matching the suburb name.

<b>Http Method:</b> GET <br>
<b>Request Path:</b> /v1/suburb/name/{postCode} <br>
<b>Response:</b> A list of suburbs matching the criteria<br>
<b>Response Code:</b> 200 <br><br>
Sample Response
<pre class-"prettyprint">
[
    {
        "id": 5,
        "suburbName": "Cremorne",
        "postCode": 3121,
        "state": "Victoria",
        "country": "Australia"
    },
    {
        "id": 6,
        "suburbName": "Cremorne",
        "postCode": 2090,
        "state": "NSW",
        "country": "Australia"
    }
]
</pre>

Sample curl command
<pre class-"prettyprint">
curl -X GET http://localhost:8080/v1/suburb/name/Cremorne
</pre>


## Development and Build Guide
The application uses an in-memory SQL database - HSQL v2.5.1 for persistence of the suburbs. At the start of the 
application there are no suburbs in the DB. Please add a few suburbs using the post api (API 1) mentioned above. 
Since it is an in-memory DB, all data will be lost once the application is shutdown.

Feel free to allocate initial and max heap size to the application depending upon the data you want to persist.

### Prerequisites
1. Java 8 or higher (the development and testing of the application has been done on Java 11 and Windows 7)
2. Gradle 5.6.4 

### Code Build
It is not mandatory to have a local installation of Gradle. There is a gradle wrapper committed in the repository that
enable us to build the code without have to install gradle locally on our system. Use below command to build the code.

<b>Windows:</b> gradlew clean build <br>
<b>Linux/MacOs:</b> ./gradlew clean build <br>

### Code Coverage
The code has jacoco gardle plugin integrated to calculate the code coverage. Jacoco report will be generated 
automatically as a part of code build. The Jacoco report can be viewed at following location under root directory:
<b>build/reports/jacoco/index.html</b>

### Application Execution 
After building the code the executable jar can be located at /build/libs/suburb-service-1.0.0-SNAPSHOT.jar under the 
root directory
1. Go to the location /build/libs/ under the root directory
2. Run following command to start the application <br>
<b>java -jar suburb-service-1.0.0-SNAPSHOT.jar </b>

The application also has spring boot actuators integration to check the health of the application. Use below command to 
check the application health: 

<b>curl -X GET http://localhost:8080/actuator/health </b>



