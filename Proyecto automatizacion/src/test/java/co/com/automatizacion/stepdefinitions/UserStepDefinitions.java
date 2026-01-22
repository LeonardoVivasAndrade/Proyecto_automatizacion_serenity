package co.com.automatizacion.stepdefinitions;

import io.cucumber.java.en.Given;

public class UserStepDefinitions {

    @Given("the user {word} and password {word}")
    public void theUserAndPassword(String username, String clave) {
        System.out.println("Usuario: "+username + " pass: " +clave);
    }
}
