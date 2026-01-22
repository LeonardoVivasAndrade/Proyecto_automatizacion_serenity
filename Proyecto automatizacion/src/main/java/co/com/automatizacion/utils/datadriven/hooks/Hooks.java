package co.com.automatizacion.utils.datadriven.hooks;


import co.com.automatizacion.utils.datadriven.models.ModelScenario;
import com.google.common.base.Splitter;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class Hooks {
    public static Actor actor;

    public Hooks() {
    }

    @Before
    public void prepareStage() {
        OnStage.setTheStage(new OnlineCast());
        actor = OnStage.theActorCalled("Usuario");
    }

    @Before
    public void firstSetTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Before
    public void setScenario(Scenario scenario) {
        ModelScenario.setScenario(scenario);
    }

    @ParameterType(".*")
    public Actor actor(String actor) {
        return OnStage.theActorCalled(actor);
    }

    @ParameterType(".*")
    public List<String> items(String listOfItems) {
        return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(listOfItems);
    }
}

