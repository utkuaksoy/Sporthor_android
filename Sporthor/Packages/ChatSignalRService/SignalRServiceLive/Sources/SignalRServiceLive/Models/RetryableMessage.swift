//
//  RetryableMessage.swift
//  SignalRServiceLive
//
//  Created by Mesut Canbaz on 22.02.2025.
//

import Foundation

struct RetryableMessage {
    let type: Int
    let message: String
    let fileType: String?
    let retryCount: Int
    let timestamp: Date
    
    init(type: Int, message: String, fileType: String?, retryCount: Int = 0) {
        self.type = type
        self.message = message
        self.fileType = fileType
        self.retryCount = retryCount
        self.timestamp = Date()
    }
    
    func incrementRetry() -> RetryableMessage {
        RetryableMessage(
            type: type,
            message: message,
            fileType: fileType,
            retryCount: retryCount + 1
        )
    }
    
    var shouldRetry: Bool {
        retryCount < 3
    }
}
