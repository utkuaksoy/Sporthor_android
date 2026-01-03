//
//  ChatViewControllerProtocol.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 23.04.2025.
//

import UIKit

public protocol ChatViewControllerProtocol: UIViewController {
    var isChatViewController: Bool { get }
    var senderId: String { get }
}

public extension ChatViewControllerProtocol {
    var isChatViewController: Bool { true }
} 
