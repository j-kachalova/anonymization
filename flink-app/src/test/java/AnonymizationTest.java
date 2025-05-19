import com.kachalova.FlinkJobProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kachalova.model.Person;
public class AnonymizationTest {

    @Test
    public void testApplyMasking() {
        Person person = new Person("89990001122", "test@example.com");
        FlinkJobProcessor jobProcessor = new FlinkJobProcessor();

        // Применяем маску к номеру телефона
        person = jobProcessor.applyMasking(person, "phone", "****");

        // Проверяем, что номер телефона замаскирован
        assertEquals("899900****", person.getPhone());
    }

    @Test
    public void testApplyRemoval() {
        Person person = new Person("89990001122", "test@example.com");
        FlinkJobProcessor jobProcessor = new FlinkJobProcessor();

        // Применяем удаление email
        person = jobProcessor.applyRemoval(person, "email");

        // Проверяем, что email удален
        assertNull(person.getEmail());
    }
}
