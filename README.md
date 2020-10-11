# suburb-service

This MS provides functionality for suburb information. It exposes following API's:
1. Get Suburbs by post code
2. Get Suburbs by name
3. Add a new suburb

## Get Suburbs by post code
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

## Get Suburbs by name
This is a public endpoint that returns a list of suburbs matching the suburb name.

<b>Http Method:</b> GET <br>
<b>Request Path:</b> /v1/suburb/postcode/{postCode} <br>

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

## Create a new suburb
This is a secured enpoint that creates a new suburb in the System.
<b>Http Method:</b> GET <br>
<b>Request Path:</b> /v1/suburb/postcode/{postCode} <br>
<b>Headers</b>: Baisc Authentication headers <br>
<b>Response:</b> A list of suburbs mathching the criteria.<br>
<b>Response Code:</b> 200 <br> <br>
Sample Request
<pre class-"prettyprint">
{
	"suburbName": "Cremorne",
	"postCode": "2090",
	"state": "NSW",
	"country": "Australia" 
}
</pre>


## Development and Build Guide
These instructions will get you a copy of the project up and running on your local machine for developemtn and testing.


### Prerequisites
1. Java 8 or higher
2. Gradle