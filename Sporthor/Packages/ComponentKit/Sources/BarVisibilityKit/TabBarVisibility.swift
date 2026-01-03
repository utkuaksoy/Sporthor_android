//
//  TabBarVisibility.swift
//  
//
//  Created by Mesut Canbaz on 13.02.2025.
//

import UIKit

public protocol TabBarVisibility: NavigationBarVisibility {

    var tabBarController: UITabBarController? { get }
    var extendedLayoutIncludesOpaqueBars: Bool { get set }
    var edgesForExtendedLayout: UIRectEdge { get set }
}

public enum TabBarVisibilityAction {
    case willAppear(isHidden: Bool)
    case didAppear(isHidden: Bool)
    case willDisappear
    case didDisappear
}

public extension TabBarVisibility {

    func configureTabBarVisibility(at place: TabBarVisibilityAction) {
        switch place {
        case let .willAppear(isHidden):
            changeTabBarVisibilityForNavigation(isHidden: isHidden)
        case let .didAppear(isHidden):
            changeTabBarVisibilityForTab(isHidden: isHidden)
        case .willDisappear:
            restoreTabBarVisibilityIfNeededForNavigation()
        case .didDisappear:
            restoreTabBarVisibilityIfNeededForTab()
        }
    }

    func changeTabBarVisibilityForNavigation(isHidden: Bool) {
        guard let tabBarController else { return }
        if oldTabBarIndex == nil || oldTabBarIndex == tabBarController.selectedIndex {
            configureTabBarVisibility(isHidden: isHidden)
        }
        oldTabBarIndex = tabBarController.selectedIndex
        configureEdges(isBarHidden: isHidden)
    }

    func changeTabBarVisibilityForTab(isHidden: Bool) {
        guard let tabBarController else { return }
        configureTabBarVisibility(isHidden: isHidden)
        oldTabBarIndex = tabBarController.selectedIndex
    }

    func restoreTabBarVisibilityIfNeededForNavigation() {
        guard shouldRestoreNavigationBar else { return }
        restoreTabBarVisibility()
    }

    func restoreTabBarVisibilityIfNeededForTab() {
        guard shouldRestoreNavigationBar == false, let tabBarController, oldTabBarIndex != tabBarController.selectedIndex else { return }
        restoreTabBarVisibility()
    }
}

private struct AssociationKeys {
    static var isTabBarHiddenBefore = "ws_TabBarVisibility_isTabBarHiddenBefore"
    static var oldTabBarIndex = "ws_TabBarVisibility_oldTabBarIndex"
}

private extension TabBarVisibility {

    var isTabBarHiddenBefore: Bool? {
        get {
            return objc_getAssociatedObject(self, &AssociationKeys.isTabBarHiddenBefore) as? Bool
        }
        set {
            objc_setAssociatedObject(self, &AssociationKeys.isTabBarHiddenBefore, newValue, .OBJC_ASSOCIATION_COPY)
        }
    }

    var oldTabBarIndex: Int? {
        get {
            return objc_getAssociatedObject(self, &AssociationKeys.oldTabBarIndex) as? Int
        }
        set {
            objc_setAssociatedObject(self, &AssociationKeys.oldTabBarIndex, newValue, .OBJC_ASSOCIATION_COPY)
        }
    }

    private func configureTabBarVisibility(isHidden: Bool) {
        if isTabBarHiddenBefore == nil {
            isTabBarHiddenBefore = tabBarController?.tabBar.isHidden
        }
        tabBarController?.tabBar.tabsVisibility(isHidden)
        configureEdges(isBarHidden: isHidden)
    }

    private func restoreTabBarVisibility() {
        guard let isTabBarHiddenBefore = isTabBarHiddenBefore else { return }
        tabBarController?.tabBar.tabsVisibility(isTabBarHiddenBefore)
        configureEdges(isBarHidden: isTabBarHiddenBefore)
    }

    private func configureEdges(isBarHidden: Bool) {
        extendedLayoutIncludesOpaqueBars = isBarHidden
        edgesForExtendedLayout = isBarHidden ? .bottom : []
    }
}

private extension UITabBar {
    func tabsVisibility(_ isHidden: Bool){
        self.isHidden = isHidden
        layer.zPosition = isHidden ? -1 : 0
    }
}
