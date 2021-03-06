# LOB PostCard Service
This service contains the list of addresses and provides APIs to add and query on addresses respectively.
This service exposes 2 APIs

## APIs

### Add address API spec

Url: /postcard/address <br/>
Method: POST <br/>
Authentication: NA (currently not setup but can be managed using headers) <br/>
Headers: Content-Type: application/json <br/>

Request Body: 
````
{
    "line1": "1600 Holloway Ave",
    "line2": "Apt 200",
    "city": "San Francisco",
    "state": "CA",
    "zip": "94133"
}
````
Response Body:

```
{
    "id": 9 // Id in case  we need to update
}
```


Sample Request
```
curl -X POST \
  http://localhost:8080/postcard/address \
  -H 'accept: application/json' \
  -H 'content-type: application/json' \
  -d '        {
            "line1": "1600 Holloway Ave",
            "city": "San Francisco",
            "state": "CA",
            "zip": "94133"
        }'
```

|  <b>Field</b> |  <b>Mandatory</b>  |                  <b>Notes  </b>             |
|:------:|:-----------:|:---------------------------------------:|
| Line 1 | Conditional | Is mandatory if Line 2 is empty         |
| Line 2 | Conditional | Is mandatory if Line 1 is empty         |
|  City  |     Yes     |                                         |
|  State |     Yes     | Must be a two character alphabet string |
|   Zip  |     Yes     | Must be a. 5 digit number               |

### Update address API spec

Url: /postcard/address/{id} <br/>
Method: PUT <br/>
Authentication: NA (currently not setup but can be managed using headers) <br/>
Headers: Content-Type: application/json <br/>

Request Body: 
````
{
    "line1": "1600 Holloway Ave",
    "line2": "Apt 200",
    "city": "San Francisco",
    "state": "CA",
    "zip": "94133"
}
````

Response Body:

```
{
    "updatedRecord": {
        "line1": "1600 Holloway",
        "line2": "Ave",
        "city": "San Francisco",
        "state": "CA",
        "zip": "94131"
    }
}

```
 
Sample Request
```
curl -X PUT \
  http://localhost:8080/postcard/address/9 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 6a0e12fa-299f-5109-f71f-34dd9eed2221' \
  -d '{
	"line1": "1600 Holloway",
	"line2": "Ave"
}'
```
One of the field must be mandatory

|  <b>Field</b> |  <b>Mandatory</b>  |                  <b>Notes  </b>             |
|:------:|:-----------:|:---------------------------------------:|
| Line 1 | Conditional |          |
| Line 2 | Conditional |       |
|  City  | Conditional     |                                         |
|  State | Conditional     | Must be a two character alphabet string |
|   Zip  |  Conditional     | Must be a. 5 digit number               |



### Get addresses (query) API spec

Url: /postcard/addresses <br/>
Method: GET <br/>
Authentication: NA (currently not setup but can be managed using headers) <br/>
Headers: Content-Type: application/json <br/>

Sample Request

```
curl -X GET \
  'http://localhost:8080/postcard/addresses?query=94132' \
  -H 'content-type: application/json' \
```

## Build and run service locally (Instructions for mac)

### Gradle

This requires Gradle version 6.3 or higher

### Build

Use the following command to build the project locally on command line

```
./gradlew clean build
```

### Run service locally

```
./gradlew bootRun
```

The service would be run on 8080 endpoint. Health check for local run can be done using

```
http://localhost:8080/info
```

### Pre-populating set of properties

A set of properties (present in addresses.json) will be pre-populated during service
startup. This is to ensure that there are some properties that can be queried on.