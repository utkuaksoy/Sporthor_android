//
//  NavigationBarVisibility.swift
//  
//
//  Created by Mesut Canbaz on 13.02.2025.
//

import UIKit

public protocol NavigationBarVisibility: AnyObject {

    var navigationController: UINavigationController? { get }
    var isMovingFromParent: Bool { get }
}

public enum NavigationBarVisibilityAction {
    case willAppear(isHidden: Bool)
    case willDisappear
}

public extension NavigationBarVisibility {

    var shouldRestoreNavigationBar: Bool { isMovingFromParent || isRestoreNeededForNavigator }

    private var isNavigationBarHiddenBefore: Bool? {
        get {
            return objc_getAssociatedObject(self, &AssociationKeys.isNavigationBarHiddenBefore) as? Bool
        }
        set {
            objc_setAssociatedObject(self, &AssociationKeys.isNavigationBarHiddenBefore, newValue, .OBJC_ASSOCIATION_COPY)
        }
    }

    private var isRestoreNeededForNavigator: Bool {
        guard let controller = self as? UIViewController,
              let navigator = navigationController else { return false }
        return navigator.viewControllers.last != controller
    }

    func configureNavigationBarVisibility(at place: NavigationBarVisibilityAction) {
        switch place {
        case let .willAppear(isHidden):
            changeNavigationBarVisibility(isHidden: isHidden)
        case .willDisappear:
            restoreNavigationBarVisibilityIfNeeded()
        }
    }

    func changeNavigationBarVisibility(isHidden: Bool) {
        if isNavigationBarHiddenBefore == nil {
            isNavigationBarHiddenBefore = navigationController?.isNavigationBarHidden
        }
        navigationController?.isNavigationBarHidden = isHidden
    }

    func restoreNavigationBarVisibilityIfNeeded() {
        guard shouldRestoreNavigationBar else { return }

        restoreNavigationBarVisibility()
    }

    private func restoreNavigationBarVisibility() {
        guard let isNavigationBarHiddenBefore = isNavigationBarHiddenBefore else { return }
        navigationController?.isNavigationBarHidden = isNavigationBarHiddenBefore
    }
}

private struct AssociationKeys {
    static var isNavigationBarHiddenBefore = "ws_NavigationBarVisibility_isNavigationBarHiddenBefore"
}
