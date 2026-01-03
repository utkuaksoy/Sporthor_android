//
//  SettingsRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Factory
import UIKit
import ThumbnailProviderKit

final class SettingsRouter: BaseRouter {}

// MARK: - SettingsRouterProtocol
extension SettingsRouter: SettingsRouterProtocol {
    func handleRouter(_ router: SettingsRoutes) {
        switch router {
        case .accountSettings:
            // Navigate to Account Settings
            let vc = AccountSettingsBuilder.build()
            viewController.show(vc, sender: nil)
        case .about:
            let vc = AboutBuilder.build()
            viewController.show(vc, sender: nil)
        case .termsOfUse:
            let vc = WebViewBuilder.build(title: "Kullanım Şartları",
                                          url: "https://accounts.sporthor.com/Agreement/TermsofUse",
                                          isPresent: true)
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalPresentationStyle = .overFullScreen
            viewController.present(navCon, animated: true)
        case .privacyPolicy:
            let vc = WebViewBuilder.build(title: "Gizlilik Politikası",
                                          url: "https://accounts.sporthor.com/Agreement/Privacy",
                                          isPresent: true)
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalPresentationStyle = .overFullScreen
            viewController.present(navCon, animated: true)
        case .contactCenter:
            let vc = WebViewBuilder.build(title: "İletişim Merkezi",
                                          url: "https://accounts.sporthor.com/pages/contact",
                                          isPresent: true)
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalPresentationStyle = .overFullScreen
            viewController.present(navCon, animated: true)
        case .logout:
            Container.shared.thumbnailProvider().clearCache()
            viewController.dismiss(animated: false)
            let vc = OnboardingBuilder.build()
            let nav = CustomNavigationController(rootViewController: vc)
            if let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate {
                sceneDelegate.window?.rootViewController = nav
            }
        }
    }
}
