//
//  ChatUserInfoViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import AutoLayout
import BarVisibilityKit
import ComponentBaseKit
import ComponentKit
import DesignKit
import UIKit

final class ChatUserInfoViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ChatUserInfoPresenterProtocol {
        get { return self.basePresenter as! ChatUserInfoPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
    
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        scroll.translatesAutoresizingMaskIntoConstraints = false
        scroll.showsVerticalScrollIndicator = false
        return scroll
    }()

    private lazy var contentView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var navigationBar: ChatUserInfoNavigationBar = {
        let bar = ChatUserInfoNavigationBar(delegate: self)
        bar.translatesAutoresizingMaskIntoConstraints = false
        return bar
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [
            profileHeaderView,
            statsContainerView,
            actionButtonsView,
            menuIndicatorView,
            menuView
        ])
        stack.axis = .vertical
        stack.spacing = 24
        stack.setCustomSpacing(8, after: profileHeaderView)
        stack.setCustomSpacing(36, after: actionButtonsView)
        stack.setCustomSpacing(.zero, after: menuIndicatorView)
        return stack
    }()
    
    private lazy var profileHeaderView: ChatUserInfoProfileHeaderView = {
        let view = ChatUserInfoProfileHeaderView()
        return view
    }()
    
    private lazy var statsContainerView: UIView = {
        let view = UIView()
        view.layout {
            $0.height == 20
        }
        return view
    }()
    
    private lazy var statsView: ChatUserInfoStatsView = {
        let view = ChatUserInfoStatsView()
        return view
    }()
    
    private lazy var actionButtonsView: ChatUserInfoActionButtonsView = {
        let view = ChatUserInfoActionButtonsView(delegate: self)
        return view
    }()
    
    private lazy var menuIndicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.layout {
            $0.height == 1
        }
        return view
    }()
    
    private lazy var menuView: ChatUserInfoMenuView = {
        let view = ChatUserInfoMenuView(delegate: self)
        return view
    }()
    
    // MARK: - Lifecycles
    
    deinit {
        removeNotificationObservers()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        presenter.viewDidLoad()
        setupNotificationObservers()
    }
}

// MARK: - Setup

private extension ChatUserInfoViewController {
    func setupUI() {
        view.backgroundColor = .white
        setupNavigationBar()
        setupViews()
    }
    
    func setupNavigationBar() {
        navigationItem.titleView = navigationBar
        navigationItem.leftBarButtonItem = navigationBar.backButton
    }
    
    func setupViews() {
        view.addSubview(scrollView) {
            $0.pin(to: view)
        }
        
        scrollView.addSubview(contentView) {
            $0.pin(to: scrollView)
            $0.width == scrollView.widthAnchor
        }
        
        contentView.addSubview(containerStackView) {
            $0.top == contentView.topAnchor + 16
            $0.leading == contentView.leadingAnchor + 16
            $0.trailing == contentView.trailingAnchor - 16
            $0.bottom == contentView.bottomAnchor - 32
        }
        
        statsContainerView.addSubview(statsView) {
            $0.centerX == statsContainerView.centerXAnchor
            $0.centerY == statsContainerView.centerYAnchor
        }
    }
}

// MARK: - Notification Handling

private extension ChatUserInfoViewController {
    func setupNotificationObservers() {
        FollowingManager.shared.addObserver(self, selector: #selector(handleFollowingStatusChange(_:)))
    }
    
    func removeNotificationObservers() {
        FollowingManager.shared.removeObserver(self)
    }
    
    @objc
    private func handleFollowingStatusChange(_ notification: Notification) {
        
        guard let userInfo = notification.userInfo,
              let userId = userInfo["userId"] as? String,
              let followStatus = userInfo["followStatus"] as? ProfileActionButtonType
        else {
            return
        }
        presenter.handleFollowingStatusChange(userId: userId, isFollowing: followStatus == .following)
    }
}

// MARK: - ChatUserInfoPresenterDelegate

extension ChatUserInfoViewController: ChatUserInfoPresenterDelegate {
    func updateUI(with response: ChatUserInfoResponse) {
        DispatchQueue.main.async { [weak self] in
            self?.profileHeaderView.configure(with: response)
            self?.statsView.configure(with: response)
            self?.actionButtonsView.configure(with: response)
            self?.menuView.configure(with: response)
        }
    }
    
    func updateFollowButton(isFollowing: Bool) {
        DispatchQueue.main.async { [weak self] in
            self?.actionButtonsView.updateFollowButton(isFollowing: isFollowing)
        }
    }
    
    func updateFollowStatus(userId: String, isFollowing: Bool) {
        DispatchQueue.main.async { [weak self] in
            self?.actionButtonsView.updateFollowButton(isFollowing: isFollowing)
        }
    }
}

// MARK: - ChatUserInfoNavigationBarDelegate

extension ChatUserInfoViewController: ChatUserInfoNavigationBarDelegate {
    func didTapBackButton() {
        navigationController?.popViewController(animated: true)
    }
}

// MARK: - ChatUserInfoActionButtonsViewDelegate

extension ChatUserInfoViewController: ChatUserInfoActionButtonsViewDelegate {
    func didTapFollowButton() {
        presenter.followButtonTapped()
    }
    
    func didTapProfileButton() {
        presenter.profileButtonTapped()
    }
}

// MARK: - ChatUserInfoMenuViewDelegate
extension ChatUserInfoViewController: ChatUserInfoMenuViewDelegate {
    func didTapMediaButton() {
        presenter.mediaButtonTapped()
    }
    
    func didTapClearChatButton() {
        presenter.clearChatButtonTapped()
    }
}
