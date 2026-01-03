//
//  DisconnectionReason.swift
//  SignalRServiceKit
//
//  Created by Mesut Canbaz on 22.02.2025.
//

import Foundation

enum DisconnectionReason {
    case userInitiated
    case networkError
    case backgroundTimeout
    case none
}

enum ConnectionError: Error {
    case notConfigured
    case alreadyConnected
}
