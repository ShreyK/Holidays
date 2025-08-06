# Federal Holiday Service

This is a service that allows users to get, add, update, and
delete federal holidays for each country.

Users can also upload CSV files to be able to perform a batch
update. Any dates that were previously saved before a file was uploaded
will be updated to match the new description. Any new dates will
be added to the DB.

## Get

### Get all holidays by country `/holiday/{country}`

- GET '/holiday/CA'
- GET '/holiday/US'

### Get all holidays by country and year `/holiday/{country}/{year}`

- GET '/holiday/CA/2025'
- GET '/holiday/US/2026'

### Get all remaining holidays by country in current year `/holiday/{country}/remaining`

- GET '/holiday/CA/remaining'
- GET '/holiday/US/remaining'

## Post
Add new holiday 
- POST '/holiday' with body
```
{
    "country": "US",
    "date": "2026-07-04",
    "description": "Independance Day"
}
```

## Put
Update holiday by ID
- PUT '/holiday/{id}' with body
```
{
    "country": "CA",
    "date": "2026-07-01",
    "description": "Independance Day"
}
```

## Delete
Delete holiday by ID
- DELETE '/holiday/{id}'

## Upload
Upload file (example data.csv provided in resources folder)
- POST '/holiday/upload'
```
{
    "file": {multi-part file}, 
}
```

