//
//  BaseRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.01.2025.
//

import UIKit
import Factory
import ComponentKit

class BaseRouter {
    unowned var viewController: BaseViewController
    
    init(viewController: BaseViewController) {
        self.viewController = viewController
    }
}

// MARK: - BaseRouterProtocol
extension BaseRouter: BaseRouterProtocol {
    func showAlert(delegate: AlertViewDelegate? = nil,
                   type: AlertType,
                   title: String = "",
                   message: String = "",
                   buttonTitle: String = "",
                   tag: Int = 0) {
        let alertBuilder = AlertViewBuilder(delegate: delegate,
                                            type: type,
                                            title: title,
                                            message: message,
                                            buttonTitle: buttonTitle,
                                            tag: tag)
        let vc = alertBuilder.make()
        vc.modalPresentationStyle = .overFullScreen
        vc.modalTransitionStyle = .crossDissolve
        viewController.present(vc, animated: true)
    }
    
    func baseLogout() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            Container.shared.thumbnailProvider().clearCache()
            viewController.dismiss(animated: false)
            let vc = OnboardingBuilder.build()
            let nav = UINavigationController(rootViewController: vc)
            if let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate {
                sceneDelegate.window?.rootViewController = nav
            }
        }
    }
    
    func showCKDefaultAlert(delegate: CKDefaultAlertDelegate?,
                            title: String,
                            message: String,
                            okTitle: String,
                            cancelTitle: String?) {
        let vc = CKDefaultAlertViewController(
            delegate: delegate,
            title: title,
            message: message,
            okTitle: okTitle,
            cancelTitle: cancelTitle)
        viewController.present(vc, animated: true)
    }
}
