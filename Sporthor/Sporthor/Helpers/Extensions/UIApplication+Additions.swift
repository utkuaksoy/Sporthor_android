//
//  UIApplication+Additions.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 13.02.2025.
//

import UIKit

public extension UIApplication {
    
    func topViewController(of viewController: UIViewController) -> UIViewController {
        if let presented = viewController.presentedViewController {
            return topViewController(of: presented)
        }
        else if let navigation = viewController as? UINavigationController {
            return topViewController(of: navigation.visibleViewController ?? navigation)
        }
        else if let tabBar = viewController as? UITabBarController {
            return topViewController(of: tabBar.selectedViewController ?? tabBar)
        }
        return viewController
    }
    
    @objc func topViewController() -> UIViewController? {
        guard let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let window = scene.windows.first,
              let root = window.rootViewController else { return nil }
        return topViewController(of: root)
    }
    
    var activeNavigationController: UINavigationController? {
        if let tabBar = topViewController() as? UITabBarController,
           let navController = tabBar.selectedViewController as? UINavigationController {
            return navController
        }
        if let navigation = topViewController()?.navigationController {
            return navigation
        }
        return nil
    }
}
