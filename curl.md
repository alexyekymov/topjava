## Curl API test

### getAll
```
GET localhost:8080/topjava/rest/meals/
Accept: application/json
```

### get
```
GET localhost:8080/topjava/rest/meals/100003
Accept: application/json
```

### delete
```
DELETE localhost:8080/topjava/rest/meals/100003
```

### create
```
POST localhost:8080/topjava/rest/meals/
Content-Type: application/json

{
"dateTime": "2023-05-06T22:10:00",
"description": "meal from IDEA",
"calories": 1234
}
```

### update
```
PUT localhost:8080/topjava/rest/meals/100004
Content-Type: application/json

{
"id": 100004,
"dateTime": "2023-05-06T15:00:00",
"description": "Обед PUT IDEA",
"calories": 1001
}
```

### getBetween
```
GET localhost:8080/topjava/rest/meals/filter?startDate=2023-05-06&endDate=2023-05-06&startTime=16:00&endTime=23:00
Accept: application/json
```
