package de.agiledojo.cdd.producer.rest;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;
import de.agiledojo.cdd.producer.calculator.PriceCalculator;
import de.agiledojo.cdd.producer.tax.TaxCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static de.agiledojo.cdd.producer.calculator.PriceCalculator.BOOKS.I;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorApiController.class)
public class CalculatorApiControllerIT {

    public static final String SINGLE_BOOK_ID = "[\"I\"]";
    public static final String STORE_CONTRACT = "de/agiledojo/cdd/price-api/store.yml";
    public static final String MARKETING_CONTRACT = "de/agiledojo/cdd/price-api/marketing.yml";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PriceCalculator calculator;

    @MockBean
    private TaxCalculator taxCalculator;

    @BeforeEach
    void setUp() {
        Mockito.when(calculator.priceFor(Mockito.anyList())).thenReturn(500L);
        Mockito.when(taxCalculator.taxFor(anyLong())).thenReturn(20L);
    }

    @Test
    void shouldFullfillConsumerContract() throws Exception {
        var validator = createValidatorForContract(STORE_CONTRACT);
        performApiCallWithRequestBody(SINGLE_BOOK_ID)
                .andExpect(status().isOk())
                .andExpect(openApi().isValid(validator));
    }

    @Test
    void shouldFullfillExternContract() throws Exception {
        var validator = createValidatorForContract(MARKETING_CONTRACT);
        performApiCallWithRequestBody(SINGLE_BOOK_ID)
                .andExpect(status().isOk())
                .andExpect(openApi().isValid(validator));
    }

    @Test
    void shouldReturnCalculatedPrice() throws Exception {
        performApiCallWithRequestBody(SINGLE_BOOK_ID)
                .andExpect(jsonPath("inCent", is(500)));
    }

    @Test
    void shouldReturnTax() throws Exception {
        performApiCallWithRequestBody(SINGLE_BOOK_ID)
                .andExpect(jsonPath("tax", is(20)));
    }

    @Test
    void shouldPerformCalculationForRequestedBooks() throws Exception {
        performApiCallWithRequestBody(SINGLE_BOOK_ID);
        verify(calculator).priceFor(singletonList(I));
    }

    @Test
    void shouldGetTaxForCalculatedPrice() throws Exception {
        performApiCallWithRequestBody(SINGLE_BOOK_ID);
        verify(taxCalculator).taxFor(500L);
    }

    private OpenApiInteractionValidator createValidatorForContract(String contractPath) {
        return OpenApiInteractionValidator.createFor(contractPath)
                .withLevelResolver(
                        LevelResolver.create()
                                .withLevel("validation.response.body.schema.additionalProperties", ValidationReport.Level.WARN)
                                .build())
                .build();
    }

    private ResultActions performApiCallWithRequestBody(String body) throws Exception {
        return mvc.perform(post("/priceFor").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(body));
    }
}
