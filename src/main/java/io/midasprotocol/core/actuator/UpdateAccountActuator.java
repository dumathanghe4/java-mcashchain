package io.midasprotocol.core.actuator;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.midasprotocol.core.Wallet;
import io.midasprotocol.core.capsule.AccountCapsule;
import io.midasprotocol.core.capsule.TransactionResultCapsule;
import io.midasprotocol.core.capsule.utils.TransactionUtil;
import io.midasprotocol.core.db.AccountIndexStore;
import io.midasprotocol.core.db.AccountStore;
import io.midasprotocol.core.db.Manager;
import io.midasprotocol.core.exception.ContractExeException;
import io.midasprotocol.core.exception.ContractValidateException;
import io.midasprotocol.protos.Contract.AccountUpdateContract;
import io.midasprotocol.protos.Protocol.Transaction.Result.code;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "actuator")
public class UpdateAccountActuator extends AbstractActuator {

	UpdateAccountActuator(Any contract, Manager dbManager) {
		super(contract, dbManager);
	}

	@Override
	public boolean execute(TransactionResultCapsule ret) throws ContractExeException {
		final AccountUpdateContract accountUpdateContract;
		final long fee = calcFee();
		try {
			accountUpdateContract = contract.unpack(AccountUpdateContract.class);
		} catch (InvalidProtocolBufferException e) {
			logger.debug(e.getMessage(), e);
			ret.setStatus(fee, code.FAILED);
			throw new ContractExeException(e.getMessage());
		}

		byte[] ownerAddress = accountUpdateContract.getOwnerAddress().toByteArray();
		AccountStore accountStore = dbManager.getAccountStore();
		AccountIndexStore accountIndexStore = dbManager.getAccountIndexStore();
		AccountCapsule account = accountStore.get(ownerAddress);

		account.setAccountName(accountUpdateContract.getAccountName().toByteArray());
		accountStore.put(ownerAddress, account);
		accountIndexStore.put(account);

		ret.setStatus(fee, code.SUCCESS);

		return true;
	}

	@Override
	public boolean validate() throws ContractValidateException {
		if (this.contract == null) {
			throw new ContractValidateException("No contract!");
		}
		if (this.dbManager == null) {
			throw new ContractValidateException("No dbManager!");
		}
		if (!this.contract.is(AccountUpdateContract.class)) {
			throw new ContractValidateException(
				"contract type error,expected type [AccountUpdateContract],real type[" + contract
					.getClass() + "]");
		}
		final AccountUpdateContract accountUpdateContract;
		try {
			accountUpdateContract = contract.unpack(AccountUpdateContract.class);
		} catch (InvalidProtocolBufferException e) {
			logger.debug(e.getMessage(), e);
			throw new ContractValidateException(e.getMessage());
		}
		byte[] ownerAddress = accountUpdateContract.getOwnerAddress().toByteArray();
		byte[] accountName = accountUpdateContract.getAccountName().toByteArray();
		if (!TransactionUtil.validAccountName(accountName)) {
			throw new ContractValidateException("Invalid accountName");
		}
		if (!Wallet.addressValid(ownerAddress)) {
			throw new ContractValidateException("Invalid ownerAddress");
		}

		AccountCapsule account = dbManager.getAccountStore().get(ownerAddress);
		if (account == null) {
			throw new ContractValidateException("Account has not existed");
		}

		if (account.getAccountName() != null && !account.getAccountName().isEmpty()
			&& dbManager.getDynamicPropertiesStore().getAllowUpdateAccountName() == 0) {
			throw new ContractValidateException("This account name already exist");
		}

		if (dbManager.getAccountIndexStore().has(accountName)
			&& dbManager.getDynamicPropertiesStore().getAllowUpdateAccountName() == 0) {
			throw new ContractValidateException("This name has existed");
		}

		return true;
	}

	@Override
	public ByteString getOwnerAddress() throws InvalidProtocolBufferException {
		return contract.unpack(AccountUpdateContract.class).getOwnerAddress();
	}

	@Override
	public long calcFee() {
		return 0;
	}
}
