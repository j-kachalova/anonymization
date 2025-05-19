import com.kachalova.FlinkJobProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kachalova.model.PersonalData;
public class AnonymizationTest {

    @Test
    public void testApplyMasking() {
        PersonalData personalData = new PersonalData("89990001122", "test@example.com");
        FlinkJobProcessor jobProcessor = new FlinkJobProcessor();

        // Применяем маску к номеру телефона
        personalData = jobProcessor.applyMasking(personalData, "phone", "****");

        // Проверяем, что номер телефона замаскирован
        assertEquals("899900****", personalData.getPhone());
    }

    @Test
    public void testApplyRemoval() {
        PersonalData personalData = new PersonalData("89990001122", "test@example.com");
        FlinkJobProcessor jobProcessor = new FlinkJobProcessor();

        // Применяем удаление email
        personalData = jobProcessor.applyRemoval(personalData, "email");

        // Проверяем, что email удален
        assertNull(personalData.getEmail());
    }
}
