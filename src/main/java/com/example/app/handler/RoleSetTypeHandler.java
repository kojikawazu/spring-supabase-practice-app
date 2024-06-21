package com.example.app.handler;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.app.model.Role;

public class RoleSetTypeHandler extends BaseTypeHandler<Set<Role>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Set<Role> parameter, JdbcType jdbcType)
			throws SQLException {
		
	}

	@Override
    public Set<Role> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String roleNames = rs.getString(columnName);
        return parseRoles(roleNames);
    }

    @Override
    public Set<Role> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String roleNames = rs.getString(columnIndex);
        return parseRoles(roleNames);
    }

    @Override
    public Set<Role> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String roleNames = cs.getString(columnIndex);
        return parseRoles(roleNames);
    }

    private Set<Role> parseRoles(String roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames != null) {
            for (String roleName : roleNames.split(",")) {
                roles.add(new Role(UUID.randomUUID(), roleName));
            }
        }
        return roles;
    }
}
