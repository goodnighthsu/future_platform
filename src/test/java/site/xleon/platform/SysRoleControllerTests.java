package site.xleon.platform;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import site.xleon.platform.models.SysRoleEntity;

public class SysRoleControllerTests extends Base {
    @Test
    void listTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/sysRole")
                        .headers(getHeaders())
                )
                .andExpect(isSuccess())
                .andReturn();
    }

    @Test
    @Transactional
    void addTest() throws Exception {
        SysRoleEntity role = new SysRoleEntity();
        role.setTitle("测试角色");
        role.setDetail("role for test");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/sysRole")
                        .content(JSON.toJSONString(role))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getHeaders()))
                .andExpect(isSuccess())
                .andReturn();
    }

    @Test
    void listRolePermissionTest() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/sysRole/1/permission")
                                .headers(getHeaders()))
                .andExpect(isSuccess())
                .andReturn();
    }
}
