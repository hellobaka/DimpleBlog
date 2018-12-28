package com.dimple.repository;

import com.dimple.bean.Permission;
import com.dimple.bean.Role;
import com.dimple.bean.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : Dimple
 * @version : 1.0
 * @class : RolePermissionRepository
 * @description :
 * @date : 12/27/18 14:12
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    List<RolePermission> findAllByRoleId(Integer id);

    List<RolePermission> findAllByPermissionId(Integer id);

    void deleteAllByRoleId(Integer id);

}
