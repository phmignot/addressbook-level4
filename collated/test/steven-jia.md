# steven-jia
###### /java/seedu/address/storage/XmlAdaptedPersonTest.java
``` java
    @Test
    public void toModelType_invalidBalance_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        INVALID_BALANCE, VALID_TAGS);
        String expectedMessage = Balance.MESSAGE_BALANCE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullBalance_returnsPersonWithZeroBalance() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_TAGS);
        Object balance = new Object();
        Balance expectedBalance = new Balance(VALID_ZERO_BALANCE);
        try {
            balance = person.toModelType().getBalance();
        } catch (IllegalValueException e) {
            fail("Could not access balance");
        }
        assertEquals(balance, expectedBalance);
    }
```
###### /java/seedu/address/testutil/PersonBuilder.java
``` java
    /**
     * Sets the {@code Balance} of the {@code Person} that we are building.
     */
    public PersonBuilder withBalance(String balance) {
        this.balance = new Balance(balance);
        return this;
    }
```
