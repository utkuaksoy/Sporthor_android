//
//  CustomTabBarController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.02.2025.
//

import ComponentKit
import DesignKit
import UIKit

extension Notification.Name {
    static let didChangeProfilePhoto = Notification.Name("didChangeProfilePhoto")
}

final class CustomTabBarController: UITabBarController {
    
    // MARK: - UI Elements
    
    private let profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.isUserInteractionEnabled = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
        configureTabBarAppearance()
        configureViewControllers()
        setupViews()
        setupConstraints()
        configureProfileImageView()
        
        NotificationCenter.default.addObserver(self, selector: #selector(didChangeProfilePhoto), name: .didChangeProfilePhoto, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - Configuration
    
    private func configureTabBarAppearance() {
        let appearance = UITabBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.shadowColor = .black.withAlphaComponent(0.4)
        tabBar.standardAppearance = appearance
        
        if #available(iOS 15.0, *) {
            tabBar.scrollEdgeAppearance = appearance
        }
        self.tabBar.isTranslucent = false
        self.tabBar.barTintColor = .white
        self.tabBar.tintColor = .black
        self.tabBar.unselectedItemTintColor = .gray
    }
    
    private func setupHorizontalSizeClass() {
        if #available(iOS 18.0, *), UIDevice.current.userInterfaceIdiom == .pad {
            traitOverrides.horizontalSizeClass = .unspecified
        }
    }
    
    private func configureViewControllers() {
        let homeVC = createNavController(for: Homev2Builder.build(), image: Asset.tabbarHome.image)
        let searchVC = createNavController(for: SearchBuilder.build(), image: Asset.tabbarSearch.image)
        let storyVC = createNavController(for: Homev2Builder.build(), image: Asset.tabbarPlus.image)
        let chatVC = createNavController(for: MessagesBuilder.build(), image: Asset.tabbarChat.image)
        let profileVC = createNavController(for: ProfileBuilder.build(), image: UIImage())
        
        viewControllers = [homeVC, searchVC, storyVC, chatVC, profileVC]
    }
    
    private func createNavController(for rootViewController: UIViewController, image: UIImage) -> CustomNavigationController {
        let navController = CustomNavigationController(rootViewController: rootViewController)
        navController.tabBarItem.image = image
        navController.tabBarItem.title = nil
        return navController
    }
    
    private func configureProfileImageView() {
        profileImageView.setImage(with: ApplicationContext.shared.profilePhoto, placeholder: .errorUserImage)
        profileImageView.layer.cornerRadius = 14
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(profileTapped))
        profileImageView.addGestureRecognizer(tapGesture)
    }
    
    @objc
    private func profileTapped() {
        selectedIndex = 4
    }
    
    @objc private func didChangeProfilePhoto() {
        configureProfileImageView()
    }
    
    // MARK: - Update Visibility
    
    private func updateProfileImageVisibility() {
        profileImageView.isHidden = self.tabBar.isHidden
    }
}

// MARK: - Setup

private extension CustomTabBarController {
    func setupViews() {
        self.tabBar.addSubview(profileImageView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            profileImageView.trailingAnchor.constraint(equalTo: self.tabBar.trailingAnchor, constant: -28),
            profileImageView.bottomAnchor.constraint(equalTo: self.tabBar.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            profileImageView.widthAnchor.constraint(equalToConstant: 28),
            profileImageView.heightAnchor.constraint(equalToConstant: 28)
        ])
    }
}

extension CustomTabBarController: UITabBarControllerDelegate {
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        if let navController = viewController as? CustomNavigationController,
           navController.viewControllers.first is Homev2ViewController,
           navController.tabBarItem.image == Asset.tabbarPlus.image {
            
            let vc = CreatePostBuilder.build()
            let nav = CustomNavigationController(rootViewController: vc)
            nav.modalPresentationStyle = .fullScreen
            self.present(nav, animated: true, completion: nil)
            
            return false
        }
        
        if let navController = viewController as? CustomNavigationController,
           navController.viewControllers.first is Homev2ViewController {
            if selectedViewController === viewController {
                if let homeVC = navController.viewControllers.first(where: { $0 is Homev2ViewController }) as? Homev2ViewController {
                    homeVC.triggerPullToRefresh()
                }
            }
        }
        
        return true
    }
}
