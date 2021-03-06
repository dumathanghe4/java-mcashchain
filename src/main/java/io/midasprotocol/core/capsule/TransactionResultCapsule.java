package io.midasprotocol.core.capsule;

import com.google.protobuf.InvalidProtocolBufferException;
import io.midasprotocol.core.exception.BadItemException;
import io.midasprotocol.protos.Protocol.Transaction;
import io.midasprotocol.protos.Protocol.Transaction.Result;
import io.midasprotocol.protos.Protocol.Transaction.Result.ContractResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "capsule")
public class TransactionResultCapsule implements ProtoCapsule<Transaction.Result> {

	private Transaction.Result transactionResult;

	/**
	 * constructor TransactionCapsule.
	 */
	public TransactionResultCapsule(Transaction.Result trxRet) {
		this.transactionResult = trxRet;
	}

	public TransactionResultCapsule(byte[] data) throws BadItemException {
		try {
			this.transactionResult = Transaction.Result.parseFrom(data);
		} catch (InvalidProtocolBufferException e) {
			throw new BadItemException("TransactionResult proto data parse exception");
		}
	}

	public TransactionResultCapsule() {
		this.transactionResult = Transaction.Result.newBuilder().build();
	}

	public TransactionResultCapsule(ContractResult code) {
		this.transactionResult = Transaction.Result.newBuilder().setContractResult(code).build();
	}

	public TransactionResultCapsule(Transaction.Result.Code code, long fee) {
		this.transactionResult = Transaction.Result.newBuilder().setCode(code).setFee(fee).build();
	}

	public void setStatus(long fee, Transaction.Result.Code code) {
		long oldValue = transactionResult.getFee();
		this.transactionResult = this.transactionResult.toBuilder()
			.setFee(oldValue + fee)
			.setCode(code).build();
	}

	public long getFee() {
		return transactionResult.getFee();
	}

	public void setFee(long fee) {
		this.transactionResult = this.transactionResult.toBuilder().setFee(fee).build();
	}

	public long getUnfreezeAmount() {
		return transactionResult.getUnfreezeAmount();
	}

	public void setUnfreezeAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder().setUnfreezeAmount(amount).build();
	}

	public long getAssetIssueId() {
		return transactionResult.getAssetIssueId();
	}

	public void setAssetIssueId(long id) {
		this.transactionResult = this.transactionResult.toBuilder().setAssetIssueId(id).build();
	}

	public long getWithdrawAmount() {
		return transactionResult.getWithdrawAmount();
	}

	public void setWithdrawAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder().setWithdrawAmount(amount).build();
	}

	public long getExchangeReceivedAmount() {
		return transactionResult.getExchangeReceivedAmount();
	}

	public void setExchangeReceivedAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder().setExchangeReceivedAmount(amount)
			.build();
	}

	public long getExchangeWithdrawAnotherAmount() {
		return transactionResult.getExchangeWithdrawAnotherAmount();
	}

	public void setExchangeWithdrawAnotherAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setExchangeWithdrawAnotherAmount(amount)
			.build();
	}

	public long getExchangeId() {
		return transactionResult.getExchangeId();
	}

	public void setExchangeId(long id) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setExchangeId(id)
			.build();
	}

	public long getExchangeInjectAnotherAmount() {
		return transactionResult.getExchangeInjectAnotherAmount();
	}

	public void setExchangeInjectAnotherAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setExchangeInjectAnotherAmount(amount)
			.build();
	}

	public void addFee(long fee) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setFee(this.transactionResult.getFee() + fee).build();
	}

	public void setErrorCode(Transaction.Result.Code code) {
		this.transactionResult = this.transactionResult.toBuilder().setCode(code).build();
	}

	public long getUnstakeAmount() {
		return transactionResult.getUnstakeAmount();
	}

	public void setUnstakeAmount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setUnstakeAmount(amount)
			.build();
	}

	public long getVoteCount() {
		return transactionResult.getVoteCount();
	}

	public void setVoteCount(long amount) {
		this.transactionResult = this.transactionResult.toBuilder()
			.setVoteCount(amount)
			.build();
	}

	@Override
	public byte[] getData() {
		return this.transactionResult.toByteArray();
	}

	@Override
	public Result getInstance() {
		return this.transactionResult;
	}
}