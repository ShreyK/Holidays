# Federal Holiday Service

This is a service that allows users to get, add, update, and
delete federal holidays for each country.

Users can also upload CSV files to be able to perform a batch
update. Any dates that were previously saved before a file was uploaded
will be updated to match the new description. Any new dates will
be added to the DB.

## Get

### Get all holidays by country `/holiday/{country}`

- GET '/holidays/CA'
- GET '/holidays/US'

### Get all holidays by country and year `/holiday/{country}/{year}`

- GET '/holidays/CA/2025'
- GET '/holidays/US/2026'

### Get all remaining holidays by country in current year `/holiday/{country}/remaining`

- GET '/holidays/CA/remaining'
- GET '/holidays/US/remaining'

## Post
Add new holiday 
- POST '/holidays' with body
```
{
    "country": "US",
    "date": "2026-07-04",
    "description": "Independance Day"
}
```

## Put
Update holiday by ID
- PUT '/holidays/{id}' with body
```
{
    "country": "CA",
    "date": "2026-07-01",
    "description": "Independance Day"
}
```

## Delete
Delete holiday by ID
- DELETE '/holidays/{id}'

## Upload
Upload file (example data.csv provided in resources folder)
- POST '/holidays/upload'
```
{
    "file": {multi-part file}, 
}
```

