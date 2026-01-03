//
//  ChatMediaAndDocumentRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import UIKit

final class ChatMediaAndDocumentRouter: BaseRouter {}

// MARK: - ChatMediaAndDocumentRouterProtocol

extension ChatMediaAndDocumentRouter: ChatMediaAndDocumentRouterProtocol {
    func handleRouter(_ router: ChatMediaAndDocumentRoutes) {
        switch router {
        case .openMedia(let item):
            // TODO: Implement media preview
            break
        case .openDocument(let item):
            // TODO: Implement document preview
            break
        }
    }
} 
