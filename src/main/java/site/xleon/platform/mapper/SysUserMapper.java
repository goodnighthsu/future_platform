package site.xleon.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.xleon.platform.core.cql.CommonMapper;
import site.xleon.platform.core.cql.CommonSqlProvider;
import site.xleon.platform.models.SysUserEntity;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUserEntity>, CommonMapper<SysUserEntity> {

    default SysUserEntity getUserById(Integer id) {
        SysUserEntity user = this.selectById(id);
        user.setPassword(null);
        return user;
    }

    /**
     * 通用查询
     * 可以使用 @Result 注解, 通过关联查询获取的字段，为关联对象赋值
     * 也可以 @One 注解获取管关联对象
     * 或者使用反射动态添加注解
     * {
     *      ...
     *      roleId: 1
     *     "role": {
     *          "id": 1
     *          "title": "guest"
     *      }
     *      ...
     * }
     * @param sql sql
     * @return list
     */
    @Results(value = {
            @Result(property = "role.id", column = "sys_role_id"),
            @Result(property = "role.title", column = "sys_role_title"),
//            @Result(property = "role", column = "role_id",
//                    one = @One(
//                            select = "site.xleon.platform.mapper.SysRoleMapper.getById",
//                            fetchType = FetchType.EAGER
//                    )
//            )
    })
    @SelectProvider(CommonSqlProvider.class)
    List<SysUserEntity> list(String sql);
}
