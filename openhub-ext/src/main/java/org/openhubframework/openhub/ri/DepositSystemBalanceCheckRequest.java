package org.openhubframework.openhub.ri;

import java.math.BigDecimal;


/**
 * @author Karel Kovarik
 */
public class DepositSystemBalanceCheckRequest {

    private TraceHeader traceHeader;
    private String depositAccountNumber;
    private BigDecimal amount;
    private String currencyCode;
    private String messageForPayment;

    public TraceHeader getTraceHeader() {
        return traceHeader;
    }

    public void setTraceHeader(TraceHeader traceHeader) {
        this.traceHeader = traceHeader;
    }

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public void setDepositAccountNumber(String depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getMessageForPayment() {
        return messageForPayment;
    }

    public void setMessageForPayment(String messageForPayment) {
        this.messageForPayment = messageForPayment;
    }

    public static class TraceHeader {
        private String tppIdentityCode;
        private String transactionId;

        public String getTppIdentityCode() {
            return tppIdentityCode;
        }

        public void setTppIdentityCode(String tppIdentityCode) {
            this.tppIdentityCode = tppIdentityCode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }
}
