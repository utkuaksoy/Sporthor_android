//
//  ConnectionState.swift
//  SignalRServiceKit
//
//  Created by Mesut Canbaz on 19.02.2025.
//

import Foundation

public enum ConnectionState {
    case disconnected
    case connecting
    case connected
    case reconnecting
}
