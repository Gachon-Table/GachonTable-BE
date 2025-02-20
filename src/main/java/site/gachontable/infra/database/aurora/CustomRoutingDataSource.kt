package site.gachontable.infra.database.aurora

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class CustomRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any =
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly())
            SLAVE else MASTER

    companion object {
        private const val MASTER = "master"
        private const val SLAVE = "slave"
    }
}