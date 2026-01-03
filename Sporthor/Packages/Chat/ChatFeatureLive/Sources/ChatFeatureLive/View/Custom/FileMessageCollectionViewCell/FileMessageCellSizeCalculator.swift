//
//  CustomCellSizeCalculator.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import MessageKit
import UIKit

final class FileMessageCellSizeCalculator: MessageSizeCalculator {
    
    override func messageContainerSize(for message: MessageType, at indexPath: IndexPath) -> CGSize {
        guard let messagesLayout = layout as? MessagesCollectionViewFlowLayout else {
            return .zero
        }
        
        let isFromCurrentSender = messagesLayout.messagesDataSource.isFromCurrentSender(message: message)
        let fixedWidth: CGFloat = 230
        
        let width = isFromCurrentSender ? fixedWidth : fixedWidth + 32
        let height: CGFloat = 64
        
        return CGSize(width: width, height: height)
    }
    
    override func messageTopLabelSize(
        for message: MessageType,
        at indexPath: IndexPath
    ) -> CGSize {
        guard let messagesLayout = layout as? MessagesCollectionViewFlowLayout else { return .zero }
        return !messagesLayout.messagesDataSource.isFromCurrentSender(message: message) 
            ? CGSize(width: messageContainerSize(for: message, at: indexPath).width, height: 20) 
            : .zero
    }
    
    override func avatarSize(for message: any MessageType, at indexPath: IndexPath) -> CGSize {
        CGSize(width: 32, height: 32)
    }
    
    override func messageBottomLabelSize(
        for message: MessageType,
        at indexPath: IndexPath
    ) -> CGSize {
        let width = messageContainerSize(for: message, at: indexPath).width
        return CGSize(width: width, height: 16)
    }
} 
