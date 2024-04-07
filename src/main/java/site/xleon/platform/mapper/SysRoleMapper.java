package site.xleon.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import site.xleon.platform.core.cql.CommonMapper;
import site.xleon.platform.models.SysRoleEntity;

public interface SysRoleMapper extends BaseMapper<SysRoleEntity>, CommonMapper<SysRoleEntity> {
    @Select("select * from sys_role where id = #{id}")
    SysRoleEntity getById(Integer id);
}
