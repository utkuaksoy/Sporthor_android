//
//  ChatCoordinatorLive.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import Factory
import ChatCoordinator
import ChatKit
import UIKit

public final class ChatCoordinatorLive: ChatCoordinator {
    
    public init() {}
    
    public func start(
        navigationController: UINavigationController,
        delegate: ChatCoordinatorDelegate?,
        chatPartner: ChatUser,
        isNewCreated: Bool
    ) {
        let viewModel = ChatViewModelImpl(chatPartner: chatPartner, isNewCreated: isNewCreated)
        let chatVC = ChatViewController(viewModel: viewModel, delegate: delegate)
        chatVC.hidesBottomBarWhenPushed = true
        navigationController.pushViewController(chatVC, animated: true)
    }
    
    public func startFullScreenImage(presenter: UIViewController, image: UIImage) {
        let fullScreenImageVc = FullscreenImageViewController(image: image)
        fullScreenImageVc.modalPresentationStyle = .fullScreen
        fullScreenImageVc.modalTransitionStyle = .crossDissolve
        presenter.present(fullScreenImageVc, animated: true)
    }
    
    public func startDocumentPreview(
        presenter: UIViewController,
        with fileURL: URL,
        fileType: String
    ) {
        let previewCoordinator = ChatPreviewCoordinator(presenter: presenter)
        previewCoordinator.start(with: fileURL, fileType: fileType)
    }
}
