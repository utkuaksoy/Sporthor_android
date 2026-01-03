//
//  ChatMessagesCollectionView.swift
//  ChatFeatureLive
//
//  Created by Mesut on 27.03.2025.
//

import Foundation
import MessageKit
import UIKit

protocol MessagesCollectionViewDelegate: AnyObject {
    func didTap()
}

class ChatMessagesCollectionView: MessagesCollectionView {
    weak var messagesCollectionViewDelegate: MessagesCollectionViewDelegate?
    
    override func handleTapGesture(_ gesture: UIGestureRecognizer) {
        super.handleTapGesture(gesture)
        messagesCollectionViewDelegate?.didTap()
    }
}
