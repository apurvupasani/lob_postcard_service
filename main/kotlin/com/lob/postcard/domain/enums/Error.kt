package com.lob.postcard.domain.enums

enum class Error(val message: String) {
    InvalidQuery("Invalid query found"),
    EmptyQuery("Query cannot be empty"),
    UnExpectedError("Unexpected error"),
    Line1AndLine2CantBeEmpty("Both Line 1 and Line 2 can't be empty"),
    CityCannotBeEmpty("City cannot be empty"),
    StateCannotBeEmpty("State cannot be empty"),
    ZipCodeCannotBeEmpty("City cannot be empty"),
    ZipCodeMustBeANumber("ZipCode must be a 6 digit number"),
    StateMustBeTwoAlphabets("State must be 2 character alphabets")
}