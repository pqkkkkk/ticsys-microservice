package org.pqkkkkk.ticsys.promotion_service.dao.voucherOfUser;

import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.promotion_service.model.VoucherOfUser;

public interface IVoucherOfUserDao {
    public int CreateVoucherOfUser(VoucherOfUser voucherOfUser);
    public int DeleteVoucherOfUser(int voucherId);
    public int UpdateVoucherOfUser(VoucherOfUser voucherOfUser);
    List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status);
    public VoucherOfUser GetVoucherOfUserById(int voucherId);
    public int UpdateVoucherOfUser(int id, Map<String, Object> newValues);
} 
