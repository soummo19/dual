package com.example.zkconsumer.utils;

public interface Constants {
    String HDR_FAILURE_REASON = "FailureReason";
    String HDR_TRACE_ID = "traceId";
    String STATE = "StateOfProcessing";
    String STATE_CONSUME = "OriginalConsume";
    String STATE_RETRY = "Retry";
    String STATE_DEAD_LETTER = "DeadLetterTopic";
    String DLT_SUFFIX = "-dlt";
    String RETRY_SUFFIX = "-retry";
    String SUCCESS = "Success";
    String FAILURE = "Failure";
    String DURATION = "Duration";
}
