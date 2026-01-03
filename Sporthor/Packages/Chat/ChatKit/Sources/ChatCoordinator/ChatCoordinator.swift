//
//  ChatCoordinator.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import ChatKit
import Factory
import UIKit

public protocol ChatCoordinator {
    func start(
        navigationController: UINavigationController,
        delegate: ChatCoordinatorDelegate?,
        chatPartner: ChatUser,
        isNewCreated: Bool
    )
    func startFullScreenImage(
        presenter: UIViewController,
        image: UIImage
    )
    func startDocumentPreview(
        presenter: UIViewController,
        with fileURL: URL,
        fileType: String
    )
}

public extension ChatCoordinator {
    func start(
        navigationController: UINavigationController,
        delegate: ChatCoordinatorDelegate?,
        chatPartner: ChatUser,
        isNewCreated: Bool = false
    ) {
        start(
            navigationController: navigationController,
            delegate: delegate,
            chatPartner: chatPartner,
            isNewCreated: false
        )
    }
}

public extension Container {
    var chatCoordinator: Factory<ChatCoordinator?> {
        self { nil }
    }
}
