package io.nuls.rpc;

import io.nuls.Config;
import io.nuls.base.basic.AddressTool;
import io.nuls.core.core.annotation.Autowired;
import io.nuls.core.core.annotation.Component;
import io.nuls.core.exception.NulsException;
import io.nuls.core.model.StringUtils;
import io.nuls.core.parse.MapUtils;
import io.nuls.core.rpc.info.Constants;
import io.nuls.core.rpc.model.ModuleE;
import io.nuls.rpc.vo.Account;
import io.nuls.rpc.vo.AccountBalance;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: zhoulijun
 * @Time: 2019-06-12 14:06
 * @Description: 功能描述
 */
@Component
public class AccountTools implements CallRpc {

    @Autowired
    Config config;

    public Account getAccountByAddress(String address) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("chainId", config.getChainId());
        param.put("address", address);
        return callRpc(ModuleE.AC.name, "ac_getAccountByAddress", param, (Function<Map<String, Object>, Account>) res -> {
                    if (res == null) {
                        return null;
                    }
                    return MapUtils.mapToBean(res, new Account());
                }
        );
    }

    /**
     * 获取账户列表
     */
    public List<Account> getAccounts() {
        Map<String, Object> param = new HashMap<>(1);
        param.put("chainId", config.getChainId());
        return callRpc(ModuleE.AC.name, "ac_getAccountList", param, (Function<Map<String, Object>, List<Account>>) res -> {
                    if (res == null) {
                        return null;
                    }
                    return (List<Account>) res.get("list");
                }
        );
    }

    /**
     * 获取账户余额
     */
    public AccountBalance getAccountBalance(String address) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("chainId", config.getChainId());
        param.put("assetChainId", config.getChainId());
        param.put("assetId", config.getAssetId());
        param.put("address", address);
        return callRpc(ModuleE.LG.abbr, "getBalanceNonce", param, (Function<Map<String, Object>, AccountBalance>) res -> {
                    if (res == null) {
                        return null;
                    }
                    AccountBalance accountBalance = new AccountBalance();
                    accountBalance.setAvailable(new BigInteger((String.valueOf(res.get("available")))));
                    accountBalance.setNonce((String) res.get("nonce"));
                    accountBalance.setNonceType((Integer) res.get("nonceType"));
                    return accountBalance;
                }
        );
    }


    /**
     * 账户验证
     * account validate
     *
     * @param chainId
     * @param address
     * @param password
     * @return validate result
     */
    public boolean accountValid(int chainId, String address, String password) throws NulsException {
        Map<String, Object> callParams = new HashMap<>(4);
        callParams.put(Constants.CHAIN_ID, chainId);
        callParams.put("address", address);
        callParams.put("password", password);
        return callRpc(ModuleE.AC.abbr, "ac_getPriKeyByAddress", callParams, (Function<Map<String, Object>, Boolean>) res -> StringUtils.isNotBlank((String)res.get("priKey")));
    }


    /**
     * 获取账户私钥
     * account validate
     *
     * @param chainId
     * @param address
     * @param password
     * @return validate result
     */
    public String getAddressPriKey(int chainId, String address, String password) throws NulsException {
        Map<String, Object> callParams = new HashMap<>(4);
        callParams.put(Constants.CHAIN_ID, chainId);
        callParams.put("address", address);
        callParams.put("password", password);
        return callRpc(ModuleE.AC.abbr, "ac_getPriKeyByAddress", callParams, (Function<Map<String, Object>, String>) res -> (String) res.get("priKey"));
    }


}
