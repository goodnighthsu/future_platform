package site.xleon.platform;

//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import site.xleon.platform.core.ResultCodeEnum;

//@SpringBootTest
//public class Base {
//
//    protected MockMvc mockMvc;
//
//    @BeforeEach
//    void setup(WebApplicationContext wac) {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//                .alwaysExpect(MockMvcResultMatchers.status().isOk())
//                .alwaysExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .alwaysDo(MockMvcResultHandlers.print())
//                .build();
//    }
//
//    public ResultMatcher isSuccess()  {
//        return MockMvcResultMatchers.jsonPath("code").value(ResultCodeEnum.SUCCESS.getValue());
//    }
//
//    public ResultMatcher isFailure()  {
//        return MockMvcResultMatchers.jsonPath("code").value(ResultCodeEnum.ERROR.getValue());
//    }
//
//    public HttpHeaders getHeaders() {
//        String token = "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJ4bGVvbi5zaXRlIiwianRpIjoiNDIiLCJhdWQiOiJ0b2tlbiIsImlhdCI6MTY2NjIzMjk3NCwiZXhwIjoxNjY2MzE5Mzc0fQ.FGj1Js23IH2cUoGqep4qusqby3ZgAFkNTeJVjuQ9E4SVooveDvl_uGTB8wWS6OUL";
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token);
//        return headers;
//    }
//}
