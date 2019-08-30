package com.noesysmobile.wdtlibrary

class WdtConstants {

    companion object {

        /**
         * Command to the service to register a client, receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client where callbacks should be sent.
         */
        internal val MSG_REGISTER_CLIENT = 1

        /**
         * Command to the service to unregister a client, ot stop receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client as previously given with MSG_REGISTER_CLIENT.
         */
        internal val MSG_UNREGISTER_CLIENT = 2

        /**
         * Command to service to set reset the WDT
         */
        internal val MSG_RESET = 3

        /**
         * Message from service, alerts WDT is expired
         */
        internal val MSG_WDT_EXPIRED = 1000

        /**
         * Initial timer delay
         */
        internal val TIMER_INITIAL_DELAY = 0L

        /**
         * Initial timer delay
         */
        internal val TIMER_PERIOD = 100L

        /**
         * Initial timer delay
         */
        internal val TIMER_CHECK_PERIOD = 1000L

        /**
         * WDT id key in bundle
         */
        internal val ID = "id"

    }

}