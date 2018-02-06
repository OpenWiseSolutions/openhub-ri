package org.openhubframework.openhub.ri;


import java.math.BigDecimal;


/**
 * @author Karel Kovarik
 */
public class BalanceCheckRequest {

    private TraceHeader traceHeader;
    private String accountNumber;
    private BigDecimal amount;
    private String currencyId;
    private String message;

    public TraceHeader getTraceHeader() {
        return traceHeader;
    }

    public void setTraceHeader(TraceHeader traceHeader) {
        this.traceHeader = traceHeader;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
