package site.xleon.platform;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import site.xleon.platform.controllers.SysUserController;

class SysUserControllerTests extends Base {
//    @Test
//    void loginSuccessTest() throws Exception {
//        SysUserController.LoginParams  params = new SysUserController.LoginParams();
//        params.setAccount("admin");
//        params.setPassword("admin");
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/api/sysUser/login")
//                        .content(JSONObject.toJSONString(params))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(this.isSuccess())
//                .andExpect(MockMvcResultMatchers.jsonPath("data.token").exists())
//                .andReturn();
//    }
//
//
//    @Test
//    void listTest() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .get("/api/sysUser")
//                )
//                .andExpect(isSuccess())
//                .andReturn();
//    }
}
