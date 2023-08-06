package sixman.stackoverflow.global.testhelper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationExtension;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest
@ExtendWith({RestDocumentationExtension.class})
public abstract class ControllerTest {
}
