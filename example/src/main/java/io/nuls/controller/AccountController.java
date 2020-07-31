package io.nuls.controller;

import io.nuls.controller.core.BaseController;
import io.nuls.controller.core.Result;
import io.nuls.controller.vo.MailContentData;
import io.nuls.core.core.annotation.Autowired;
import io.nuls.core.core.annotation.Component;
import io.nuls.core.log.Log;
import io.nuls.rpc.AccountTools;
import io.nuls.rpc.vo.Account;
import io.nuls.rpc.vo.AccountBalance;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

@Path("/")
@Component
public class AccountController implements BaseController {

    @Autowired
    AccountTools accountTools;

    @Path("getAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Result<List<Account>> getAccounts() {
        return call(() -> new Result(accountTools.getAccounts()));
    }

    @Path("getAccountByAddress")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Result<Account> getAccount(@QueryParam("address") String address) {
        return call(() -> new Result(accountTools.getAccountByAddress(address)));
    }

    @Path("getBalance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Result<AccountBalance> getBalance(@QueryParam("address") String address) {
        return call(() -> new Result(accountTools.getAccountBalance(address)));
    }

}
