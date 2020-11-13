package com.lob.postcard.domain.enums

enum class Error(val message: String) {
    InvalidQuery("Invalid query found"),
    EmptyQuery("Query cannot be empty"),
    UnExpectedError("Unexpected error"),
    Line1AndLine2BothEmpty("Both Line 1 and Line 2 can't be empty"),
    CityEmpty("City cannot be empty"),
    StateEmpty("State cannot be empty"),
    ZipCodeEmpty("City cannot be empty"),
    ZipCodeNotAValidNumber("ZipCode must be a 5 digit number"),
    StateInvalid("State must be 2 character alphabets"),
    DuplicateRecord("Duplicate record exists in datastore"),
    AddressIdBlank("Address Id cannot be blank"),
    AddressIdNotANumber("Address Id must be number"),
    EmptyUpdateRequest("Update Request cannot be empty"),
    RecordNotFound("Address not found"),
    InvalidId("Invalid id found"),
    EmptyId("Id cannot be empty"),
}