package site.xleon.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import site.xleon.platform.models.SysUserEntity;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    default SysUserEntity getUserById(Integer id) {
        SysUserEntity user = this.selectById(id);
        user.setPassword(null);
        return user;
    }
}
