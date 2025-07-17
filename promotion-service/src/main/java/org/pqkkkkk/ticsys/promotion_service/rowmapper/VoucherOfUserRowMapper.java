package org.pqkkkkk.ticsys.promotion_service.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pqkkkkk.ticsys.promotion_service.model.VoucherOfUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

public class VoucherOfUserRowMapper implements RowMapper<VoucherOfUser> {
    @Override
    public VoucherOfUser mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return VoucherOfUser.builder()
                .id(rs.getInt("id"))
                .voucherValue(rs.getInt("voucherValue"))
                .userId(rs.getString("userId"))
                .promotionId(rs.getInt("promotionId"))
                .status(rs.getString("status"))
                .build();
    }

}
